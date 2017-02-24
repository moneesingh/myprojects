package com.evariant.msaprecip.reader;

import java.io.FileReader;

import com.opencsv.CSVReader;

public class AbstractParser {
	protected FileReader freader;
	protected CSVReader csvreader;
	
	protected int WBANField; //Column number in the csv files for WBAN
	protected int skiplines; //Number of header lines to be skipped by CSVParser.
	protected char delimiter;

	public AbstractParser(int wban, char delimit, int skip) {
		WBANField = wban;
		delimiter = delimit;
		skiplines = skip;
	}
}
