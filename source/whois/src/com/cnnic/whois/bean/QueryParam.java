package com.cnnic.whois.bean;

public class QueryParam {
	private String q;
	private boolean isFuzzyQ = false;

	public QueryParam(String q) {
		this.q = q;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public boolean isFuzzyQ() {
		return isFuzzyQ;
	}

	public void setFuzzyQ(boolean isFuzzyQ) {
		this.isFuzzyQ = isFuzzyQ;
	}

}