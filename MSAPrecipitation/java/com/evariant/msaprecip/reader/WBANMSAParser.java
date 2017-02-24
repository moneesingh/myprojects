package com.evariant.msaprecip.reader;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.evariant.msaprecip.MSA;
import com.opencsv.CSVReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Parse stations file (station.txt) to construct WBAN-MSA ie (id, msa) map. 
 * Assumptions: 
 * 1. Records without WBAN id are skipped and considered invalid data. 
 * 2. Data file has first record as header which is skipped while reading 
 * 3. Data in station file are separated by 
 **/
public class WBANMSAParser extends AbstractParser implements IParser <Long, MSA>{
	private static final Logger log = LogManager.getLogger(WBANMSAParser.class);
	private int msaCityField;
	private int msaStateField;
	private Map <Long,MSA> wbanMSAMap;
	
	
  /**	
    * Constructor
    * @param wbanCol field that holds WBAN(ID) for Metropolital Statistical Area(MSA)
    * @param cityCol field that holds City name for MSA
    * @param stateCol field that holds State name for MSA
    * @param skip Number of records to skip from header section of csv file
    * @param delimit field separator character
    * Example: wbanCol = 0, cityCol = 6, stateCol = 7, skip=1, delimit = '|' in station.txt
    **/
	public WBANMSAParser(int wbanCol, int cityCol, int stateCol, int skip, char delimit) {
		super(wbanCol, delimit, skip);
		msaCityField = cityCol;
		msaStateField = stateCol;
	}
	
  /**	
    * Parse station file which holds information about WBAN, metropolitan city and state and return a map.
    * @param file csv file with wsa-msa data
    * @return Map with WBAN as keys and MSA(city,state) as values. 
    * 
    **/
	public Map<Long,MSA> parse (String file) throws IOException {
		wbanMSAMap = new HashMap<Long, MSA>();
		try {
			 freader = new FileReader(file); 
			 csvreader = new CSVReader(freader, delimiter, '\'', skiplines);
		     String [] nextLine;
		     while ((nextLine = csvreader.readNext()) != null) {
		    	if (nextLine[WBANField].isEmpty())
		    		continue;
		    	Long key = Long.parseLong(nextLine[WBANField]);
		    	MSA msa = new MSA(nextLine[msaCityField], nextLine[msaStateField]);
		        wbanMSAMap.put(key, msa);
		     }
		}  
		catch (IOException e) {
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
		return wbanMSAMap;
	}
}
