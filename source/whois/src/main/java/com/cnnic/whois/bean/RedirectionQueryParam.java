package com.cnnic.whois.bean;
/**
 * query redirection param 
 * @author nic
 *
 */
public class RedirectionQueryParam extends QueryParam {
	private String tableName;

	/**
	 * construction
	 * @param tableName:redirection table name
	 * @param q:query param string
	 */
	public RedirectionQueryParam(String tableName, String q) {
		super(q);
		this.tableName = tableName;
	}

	/**
	 * table name
	 * @return table name
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * set table name
	 * @param tableName:table name
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}