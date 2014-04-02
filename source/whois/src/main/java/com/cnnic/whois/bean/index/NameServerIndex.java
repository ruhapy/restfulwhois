package com.cnnic.whois.bean.index;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.beans.Field;
/**
 * ns index for search
 * @author nic
 *
 */
public class NameServerIndex implements Index{

	@Field("docType")
	private String docType;
	@Field("id")
	private String id;
	@Field("handle")
	private String handle;
	@Field("ldhName")
	private String ldhName;
	@Field("ipV4Address")
	private String ipV4Address;
	@Field("status")
	private String status;
	@Field("port43")
	private String port43;
	@Field("lang")
	private String lang;
	@Field("unicodeName")
	private String unicodeName;
	@Field("ipV6Address")
	private String ipV6Address;
	private Map<String,String> propValueMap = new HashMap<String,String>();

	/**
	 * get prop value from cached map
	 * @return prop value
	 */
	public String getPropValue(String key){
		return propValueMap.get(key);
	}

	/**
	 * default construction
	 */
	public NameServerIndex() {
	}

	/**
	 * init prop map
	 */
	public void initPropValueMap() {
		propValueMap.put("Handle", this.handle);
		propValueMap.put("Ldh_Name", this.ldhName);
		propValueMap.put("IPV4_Addresses", this.ipV4Address);
		propValueMap.put("Status", this.status);
		propValueMap.put("Port43", this.port43);
		propValueMap.put("Lang", this.lang);
		propValueMap.put("Unicode_Name", this.unicodeName);
		propValueMap.put("IPV6_Addresses", this.ipV6Address);
	}

	@Override
	public String getHandle() {
		return handle;
	}

	/**
	 * get doc type
	 * return 'nameserver'
	 */
	public String getDocType() {
		return docType;
	}

	/**
	 * set doc type
	 * @param docType
	 */
	public void setDocType(String docType) {
		this.docType = docType;
	}
}