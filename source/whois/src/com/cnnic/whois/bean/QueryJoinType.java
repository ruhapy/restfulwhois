package com.cnnic.whois.bean;

public enum QueryJoinType {
	ENTITIES("entities"), NAMESERVER("nameserver"), DSDATA("dsData"), EVENTS(
			"events"), KEYDATA("keyData"), DALEGATIONKEYS("delegationKeys"), PUBLICIDS(
			"publicIds"), LINKS("links"), NOTICES("notices"), PHONES("phones"), POSTALADDRESS(
			"postalAddress"), REMARKS("remarks"), SECUREDNS("secureDNS"), VARIANTS(
			"variants");
	private String name;

	private QueryJoinType(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
