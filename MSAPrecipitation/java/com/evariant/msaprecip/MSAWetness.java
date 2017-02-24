package com.evariant.msaprecip;

public class MSAWetness implements Comparable<MSAWetness>{
	private MSA msa;
	private float wetness;
	
	public MSAWetness(MSA msa, float wetness) {
		this.msa = msa;
		this.wetness = wetness;
	}

	@Override
	public int compareTo(MSAWetness m) {
		// TODO Auto-generated method stub
		if (this == m)
			return 0;
		if (this.wetness == m.wetness) {
			return this.msa.getCity().trim().compareTo(m.msa.getCity().trim());
		}
		if (this.wetness < m.wetness) 
			return -1;
		else
			return 1;
	}
	
	@Override
    public String toString() {
        return "MSAWetness{" + msa.getCity() + ", "  + msa.getState() + ": " + wetness + "}";
    }
}
