package com.cnnic.whois.bean;

public class DomainQueryParam extends QueryParam {
	private String domainPuny;

	public DomainQueryParam(String q, String domainPuny) {
		super(q);
		this.domainPuny = domainPuny;
	}

	public String getDomainPuny() {
		return domainPuny;
	}

	public void setDomainPuny(String domainPuny) {
		this.domainPuny = domainPuny;
	}
}