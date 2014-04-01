package com.cnnic.whois.bean;
/**
 * query type
 * @author nic
 *
 */
public enum QueryType {
	NONE("none"), DOMAIN("domain"), ENTITY("entity"), NAMESERVER("nameServer"), AUTNUM(
			"autnum"), DSDATA("dsData"), EVENTS("events"), HELP("help"), IP(
			"ip"), KEYDATA("keyData"), LINKS("links"), NOTICES("notices"), PHONES(
			"phones"), POSTALADDRESS("postalAddress"), REMARKS("remarks"), SECUREDNS(
			"secureDNS"), VARIANTS("variants"), DNRDOMAIN("dnrDomain"), RIRDOMAIN(
			"rirDomain"), DNRENTITY("dnrEntity"), RIRENTITY("rirEntity"), ENTITYSEARCH(
			"entitySearch"), DELETATIONKEY("delegationKey"), ERRORMSG(
			"errorMsg"), SEARCHDOMAIN("fuzzyDomain"), SEARCHENTITY(
			"fuzzyEntity"), SEARCHNS("fuzzyNs"), IPREDIRECTION("ipRedirection"), REDIRECTION(
			"redirection"),PUBLICIDS("publicIds");
	private String name;

	/**
	 * get query type be name
	 * @param name:query type name
	 * @return query type name
	 */
	public static QueryType getQueryType(String name) {
		QueryType[] queryTypes = QueryType.values();
		for (QueryType joinType : queryTypes) {
			if (joinType.getName().equals(name)) {
				return joinType;
			}
		}
		return null;
	}
	
	/**
	 * construction
	 * @param name:query type name
	 */
	private QueryType(String name) {
		this.setName(name);
	}

	/**
	 * get type name
	 * @return type name
	 */
	public String getName() {
		return name;
	}

	/**
	 * set type name
	 * @param name:type name
	 */
	public void setName(String name) {
		this.name = name;
	}
}