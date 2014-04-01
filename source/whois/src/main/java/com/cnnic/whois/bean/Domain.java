package com.cnnic.whois.bean;
/**
 * domain bean
 * @author nic
 *
 */
public class Domain {
	private String id;
	private String docType;
	private String handle;
	private String ldhName;
	private String status;
	private String port43;
	private String lang;
	private String unicodeName;

	/**
	 * get domain id
	 * @return domain id
	 */
	public String getId() {
		return id;
	}

	/**
	 * set domain id
	 * @param id:domain id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * get doc type,for search
	 * @return doc type
	 */
	public String getDocType() {
		return docType;
	}

	/**
	 * set doc type
	 * @param docType:doc type
	 */
	public void setDocType(String docType) {
		this.docType = docType;
	}

	/**
	 * get domain handle
	 * @return domain handle
	 */
	public String getHandle() {
		return handle;
	}

	/**
	 * set domain handle
	 * @param handle:domain handle
	 */
	public void setHandle(
			String handle) {
		this.handle = handle;
	}

	public String getLdhName() {
		return ldhName;
	}

	public void setLdhName(
			String ldhName) {
		this.ldhName = ldhName;
	}

	/**
	 * get status
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * set status
	 * @param status:status
	 */
	public void setStatus(
			String status) {
		this.status = status;
	}

	/**
	 * get port43
	 * @return port43
	 */
	public String getPort43() {
		return port43;
	}

	/**
	 * set port43
	 * @param port43:port43
	 */
	public void setPort43(
			String port43) {
		this.port43 = port43;
	}

	/**
	 * get language
	 * @return language
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * set language
	 * @param lang:language
	 */
	public void setLang(
			String lang) {
		this.lang = lang;
	}

	/**
	 * get unicode name
	 * @return unicode name
	 */
	public String getUnicodeName() {
		return unicodeName;
	}

	/**
	 * set unicode name
	 * @param unicodeName:unicode name
	 */
	public void setUnicodeName(
			String unicodeName) {
		this.unicodeName = unicodeName;
	}
}
