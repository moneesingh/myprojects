package com.evariant.msaprecip;

import java.util.Objects;

public class MSA {
	private String city;
	private String state;
	
	public MSA (String ct, String st) {
		city = ct.trim();
		state = st.trim();
	}
	
	public String getCity() {
		return city;
	}
	
	public String getState() {
		return state;
	}
	
	@Override
	public String toString() {
		return city + ", " + state;
	}
	
	@Override
	public boolean equals(Object O){
		if ((O == null) || !(O instanceof MSA))
			return false;
		MSA msa = (MSA)O;
		if (city.trim().equalsIgnoreCase(msa.getCity().trim()) && state.trim().equalsIgnoreCase(msa.getState().trim()))
			return true;
		else
			return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(city, state); 
	}
}