package com.cnnic.whois.bean;

public enum QueryType {
	NONE("none"),DOMAIN("domain"), ENTITY("entity"), NAMESERVER("nameserver"), AUTNUM(
			"autnum"), DSDATA("dsData"), EVENTS("events"), HELP("help"), IP(
			"ip"), KEYDATA("keyData"), LINKS("links"), NOTICES("notices"), PHONES(
			"phones"), POSTALADDRESS("postalAddress"), REMARKS("remarks"), SECUREDNS(
			"secureDNS"), VARIANTS("variants"), DNRDOMAIN("dnrDomain"), RIRDOMAIN(
			"rirDomain"), DNRENTITY("dnrEntity"), RIRENTITY("rirEntity"), ENTITYSEARCH(
			"entitySearch"),DELETATIONKEY("delegationKey"),ERRORMSG("errorMsg"),
			FUZZYDOMAIN("fuzzyDomain"),FUZZYENTITY("fuzzyEntity"),FUZZYNS("fuzzyNs")
			,IPREDIRECTION("ipRedirection");
	private String name;

	private QueryType(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
