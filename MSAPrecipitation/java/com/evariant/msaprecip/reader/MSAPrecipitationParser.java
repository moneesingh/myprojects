package com.evariant.msaprecip.reader;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.CSVReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Parse hourly precipitation file(201505hourly.txt) to construct WBAN-Precipitation ie (MSA id, Precipitaiton) map. 
 * Assumptions: 
 * 1. Assuming input file is sorted by date and hour
 * 2. Assuming hour fields 1=12:01am, 101=1:01am, 124=1:24am, 201=2:01am, 301=3am ..., 2301=11pm
 * 3. Records without MSA are skipped and considered invalid data. 
 * 4. Records with non-numeric characters in precipitation field are considered 0 precipitation.
 * 5. Records between time=1 and time=700 are ingnored as per business requirement.
 **/
public class MSAPrecipitationParser extends AbstractParser implements IParser<Long, Float>{
	private static final Logger log = LogManager.getLogger(MSAPrecipitationParser.class);
	private int precipField;
	private int dateField;
	private int hourField;
	private Map <Long, Float> wbanPrecipMap;
	
  /**	
    * Constructor
    * @param wbanCol MSA id field for Metropolital Statistical Area(MSA)
    * @param precipCol field that holds precipitation for MSA
    * @param dateCol field that holds precipitation date for MSA
    * @param hourCol field that holds precipitation hour for MSA
    * @param skip Number of records to skip from header section of csv file
    * @param delimit field separator character
    * Example: wbanCol = 0, precipCol = 40, dateCol = 1, hourCol=2, skip=1 lines from top in hourly.txt
    **/
	public MSAPrecipitationParser(int wbanCol, int precipCol, int dateCol, int hourCol, int skip, char delimit) {
		super(wbanCol, delimit, skip);
		precipField = precipCol;
		dateField = dateCol;
		hourField = hourCol;
	}
	
  /**	
    * Parse station file which holds information about WBAN, metropolitan city and state and return a map.
    * @param file csv file with wsa-msa data
    * @return Map with WBAN as keys and monthly Precipitation as values. 
    * 
    **/
	public Map<Long,Float> parse (String file) throws IOException {
		wbanPrecipMap = new HashMap<Long, Float>();
		try {
			 freader = new FileReader(file); 
			 // csvreader = new CSVReader(freader, delimiter, '\'', skiplines);
			 csvreader = new CSVReader(freader, delimiter, '\'', skiplines);
		     String [] nextLine;
		     Long prevKey = 0L;
		     Long key = 0L;
		     int date = 0;
		     int hour = 0;
		     float totalPrecipitationForMonth = 0;
		     float precipitation = 0;
		     while ((nextLine = csvreader.readNext()) != null) {
		    	if (nextLine[WBANField].isEmpty()) {
		    		prevKey = key;
			    	continue;
		    	}
		    	try {
		    		key = Long.parseLong(nextLine[WBANField]);
			    	
			    	//skip dates before and after May2015
			    	date = Integer.parseInt(nextLine[dateField]);
			    	if (date < 20150501 || date > 20150531) {
			    		if ((prevKey != 0) && (key != prevKey))
			    			updateMap(prevKey, totalPrecipitationForMonth);
			    		prevKey = key;
				    	continue;
			    	}
			    	//Ignore hours between morning 12am - 7am. This is the business condition.
			    	hour = Integer.parseInt(nextLine[hourField]);
			    	if (hour < 701 || hour > 2400) {
			    		if(prevKey != 0)
			    			updateMap(prevKey, totalPrecipitationForMonth);
			    		prevKey = key;
				    	continue;
			    	}
			    	
			    } catch (NumberFormatException ne) {
			    	if ((prevKey != 0) && (key != prevKey))
			    		updateMap(prevKey, totalPrecipitationForMonth);
		    		prevKey = key;
			    	continue;
				}
		    	try {
		    		precipitation = Float.valueOf(nextLine[precipField]);
		    	} catch (NumberFormatException ne) {
		    		precipitation = 0;
		    	}
			    
		    	//Reset totalprecipitation for new record for different WBAN
		    	if (key != prevKey) {
		    		updateMap(prevKey, totalPrecipitationForMonth);
		    		totalPrecipitationForMonth = precipitation;
		    	}
		    	//if current record and previous record have same WBAN id and date, add precipitations from previous record.
		    	if (key == prevKey)
		    		totalPrecipitationForMonth += precipitation;
		    	
		    	prevKey = key;
		     }
		     wbanPrecipMap.put(key, totalPrecipitationForMonth);
		} catch (IOException e) {
			log.error("File read error: " + file);
			throw e;
		}
		finally {
	        try {if(freader !=null){
	        	    if (csvreader != null) {
	        	    	csvreader.close();
	        	    }
	        		freader.close();
	        	}
	        } catch (IOException e) {
	        	log.warn("File close error: " + file, e);
	        }
	    }
		return wbanPrecipMap;
	}
	
	private void updateMap(Long key, Float value) {
		wbanPrecipMap.put(key, value);
	}
}
