package com.evariant.msaprecip;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evariant.msaprecip.reader.MSAPolulationParser;
import com.evariant.msaprecip.reader.MSAPrecipitationParser;
import com.evariant.msaprecip.reader.WBANMSAParser;

public class Driver {
	private static final Logger log = LogManager.getLogger(MSAWetness.class);
	
	
	
	public Map<Long, Float> getWbanPrecipMap() throws IOException {
		MSAPrecipitationParser precipParser = new MSAPrecipitationParser(0, 40, 1, 2, 1, ',');
		try {
			//return precipParser.parse("java/resource/hourly.csv");
			return precipParser.parse("java/tempresource/hourly.txt");
		} catch (IOException e) {
			throw e;
		}
	}
	
	public Map<MSA,Long> getWbanPopulMap() throws IOException {
		MSAPolulationParser populParser = new MSAPolulationParser(0, 2, 4, 8, ',');
		try {
			//return populParser.parse("java/resource/population.csv");
			return populParser.parse("java/tempresource/population.csv");
		} catch (IOException e) {
			throw e;
		}
	}
	
	public Map<Long,MSA> getWbanMSAMap() throws IOException{
		WBANMSAParser wbanParser = new WBANMSAParser(0, 6, 7, 1, '|');
		try {
			//return wbanParser.parse("java/resource/201505station.txt");
			return wbanParser.parse("java/tempresource/station.txt");
		} catch (IOException e) {
			throw e;
		}
	}
	
	public List <MSAWetness> getMsaWetnessList() throws IOException {
		List <MSAWetness> wetnessL = new ArrayList<MSAWetness>(); 
		Map<Long,MSA> wbanMSAMap = null;
		Map<MSA,Long> wbanPopulMap = null;
		Map<Long, Float> wbanPrecipMap = null;
		try {
			wbanMSAMap = getWbanMSAMap();
			wbanPopulMap = getWbanPopulMap();
			wbanPrecipMap = getWbanPrecipMap();
		} catch (IOException e) {
			throw e;
		}
		
		if (wbanPrecipMap == null || wbanPopulMap == null || wbanMSAMap == null) {
			log.error("Data maps are null. Cannot process..");
		}
		for (Map.Entry<Long,MSA> entry: wbanMSAMap.entrySet()) {
			MSA msa = entry.getValue();
			Long wban = entry.getKey();
			if (wbanPopulMap.containsKey(msa)) {
			 if (wbanPrecipMap.containsKey(wban))	{
				 float wetness = wbanPopulMap.get(msa) * wbanPrecipMap.get(wban);
				 wetnessL.add(new MSAWetness(msa, wetness));
			 }
			}
		}
		return wetnessL;
	}
	
	public static void main(String [] args) {
		Driver driver = new Driver();
		List <MSAWetness> wetnessL = null;
		try {
			wetnessL = driver.getMsaWetnessList();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		if(wetnessL.size()>0){
			System.out.println("Before sorting");
			for (MSAWetness wetness: wetnessL)
				System.out.println(wetness);
			
			Collections.sort(wetnessL);
			System.out.println("After sorting");
			for (MSAWetness wetness: wetnessL)
				System.out.println(wetness);
		}
		else 
			log.warn("No matching MSA and precipitation in MSA found. Wetness List is empty.");
	}
	
	
}
