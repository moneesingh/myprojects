package com.evariant.msaprecip.reader;


import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.evariant.msaprecip.MSA;
import com.opencsv.CSVParser;
import com.opencsv.CSVReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Parse stations file (station.txt) to construct WBAN-MSA ie (id, msa) map. 
 * Assumptions: 
 * 1. Assuming linear percentage population growth per year. eg. If 10 year % growth is 3%, 5 years growth is 3/2=1.5%
 * 2. Assuming there are no other special characters in population field, other than comma.
 * 3. Records without MSA are skipped and considered invalid data. 
 * 4. Data file has first record as header which is skipped while reading 
 * 5. Data in station file are separated by delimiter
 **/
public class MSAPolulationParser extends AbstractParser implements IParser <MSA, Long>{
	private static final Logger log = LogManager.getLogger(MSAPolulationParser.class);
	private int populationField;
	private int percentIncField;
	private Map <MSA,Long> wbanPopulMap;
	
  /**	
    * Constructor
    * @param wbanCol field that MSA(City, State) for Metropolitan Statistical Area(MSA)
    * @param popCol field that holds population for MSA from 2010
    * @param percentIncCol field that holds percent increase in population from 2000-2010 for MSA
    * @param skip Number of records to skip from header section of csv file
    * @param delimit field separator character
    * Example: wbanCol = 0, popCol = 2, percentIncCol = 4, skip 8 lines from top in population.csv
    **/
	public MSAPolulationParser(int wbanCol, int popCol, int percentIncCol, int skip, char delimit) {
		super(wbanCol, delimit, skip);
		populationField = popCol;
		percentIncField = percentIncCol;
	}
	
  /**	
    * Parse station file which holds information about WBAN, metropolitan city and state and return a map.
    * @param file csv file with wsa-msa data
    * @return Map with WBAN as keys and MSA(city,state) as values. 
    * 
    **/
	public Map<MSA,Long> parse (String file) throws IOException {
		wbanPopulMap = new HashMap<MSA, Long>();
		try {
			 freader = new FileReader(file); 
			// csvreader = new CSVReader(freader, delimiter, '\'', skiplines);
			 csvreader = new CSVReader(freader, delimiter);
		     String [] nextLine;
		     int lines = 0;
		     Long population = 0L;
		     float percentIncPopul = 0;
		     while ((nextLine = csvreader.readNext()) != null) {
		    	if (lines++ < skiplines)
		    		continue;
		    	
		    	if (nextLine[WBANField].isEmpty())
		    		continue;
		    	//String[] tokens = nextLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		    	MSA msa = msaFromLine(nextLine[WBANField]);
		    	try {
			    	//get 2010 population from pupulation file
			    	population = Long.parseLong(nextLine[populationField].replace(",", ""));
			    	//Get percent increase in population from 2000-to-2010 which is 10 years percent increase
			    	percentIncPopul = Float.valueOf(nextLine[percentIncField]);
			    	
		    	}catch (NumberFormatException ne) {
		    		log.debug("Population format is incorrect in file: " + file);
		    		continue;
		    	}
		    	//Assumption 2 applied here, percentIncPopul/2 is the population percentage increment in 5 years
		    	wbanPopulMap.put(msa, getMay2015popul(population, percentIncPopul/2));
		     }
		}catch (IOException e) {
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
		return wbanPopulMap;
	}
	
	//Construct MSA from line which is in format 'Abilene, TX'. Trim whitespaces.
	private MSA msaFromLine(String line) {
		MSA msa = null;
		String [] strArr = line.split(",");
		if (strArr.length > 1 )
			msa = new MSA(strArr[0], strArr[1]); //opencsv trims spaces.
		else 
			msa = new MSA(strArr[0], "");
		return msa;
	}
	
	//Find the population 
	private Long getMay2015popul(long population, float percentIncrease) {
		population += (long) (population*percentIncrease/100);
		return population;
	}
}
