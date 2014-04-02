package com.cnnic.whois.bean.index;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.solr.client.solrj.beans.Field;

import com.cnnic.whois.bean.Domain;
/**
 * domain for search
 * @author nic
 *
 */
public class DomainIndex implements Index {

	private static String DNRDOMAIN_TYPE = "dnrDomain";
	private static String RIRDOMAIN_TYPE = "rirDomain";
	@Field("id")
	private String id;
	@Field("docType")
	private String docType;
	@Field("handle")
	private String handle;
	@Field("ldhName")
	private String ldhName;
	@Field("status")
	private String status;
	@Field("port43")
	private String port43;
	@Field("lang")
	private String lang;
	@Field("unicodeName")
	private String unicodeName;
	private Map<String, String> propValueMap = new HashMap<String, String>();

	private Domain domain;

	/**
	 * check domain is dnr
	 * @return true if dnr,false if not dnr
	 */
	public boolean isDnrDomain() {
		return DNRDOMAIN_TYPE.equals(this.docType);
	}
	
	/**
	 * get bean property value from cached map
	 */
	public String getPropValue(String key) {
		return propValueMap.get(key);
	}

	/**
	 * construction
	 * @param domain:domain name
	 */
	public DomainIndex(Domain domain) {
		this.domain = domain;
		try {
			BeanUtils.copyProperties(this, domain);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * default construction
	 */
	public DomainIndex() {
	}

	/**
	 * set properties values to map
	 */
	public void initPropValueMap() {
		propValueMap.put("Handle", this.handle);
		propValueMap.put("Ldh_Name", this.ldhName);
		propValueMap.put("Unicode_Name", this.unicodeName);
		propValueMap.put("Lang", this.lang);
		propValueMap.put("Port43", this.port43);
		propValueMap.put("Status", this.status);
	}

	/**
	 * get domain bean from domain index
	 * @return
	 */
	public Domain getDomainBean() {
		if (domain == null) {
			domain = new Domain();
			domain.setId(id);
			domain.setDocType(docType);
			domain.setHandle(handle);
			domain.setLang(lang);
			domain.setLdhName(ldhName);
			domain.setPort43(port43);
			domain.setStatus(status);
			domain.setUnicodeName(unicodeName);
			propValueMap.put("Handle", this.handle);
			propValueMap.put("Ldh_Name", this.ldhName);
			propValueMap.put("Unicode_Name", this.unicodeName);
			propValueMap.put("Lang", this.lang);
			propValueMap.put("Port43", this.port43);
		}
		return domain;
	}

	/**
	 * get domain handle
	 * @return handle
	 */
	public String getHandle() {
		return handle;
	}

	/**
	 * set handle
	 * @param handle
	 */
	public void setHandle(String handle) {
		this.handle = handle;
	}

	/**
	 * get domain name
	 * @return domain name
	 */
	public String getLdhName() {
		return ldhName;
	}

	/**
	 * set domain name
	 * @param ldhName:domain name
	 */
	public void setLdhName(String ldhName) {
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
	 * @param status:domain status
	 */
	public void setStatus(String status) {
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
	 * @param port43:port 43
	 */
	public void setPort43(String port43) {
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
	public void setLang(String lang) {
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
	public void setUnicodeName(String unicodeName) {
		this.unicodeName = unicodeName;
	}

	/**
	 * get domain 
	 * @return domain
	 */
	public Domain getDomain() {
		return domain;
	}

	/**
	 * set domain
	 * @param domain:domain
	 */
	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	/**
	 * get domain handle
	 * @return:domain handle
	 */
	public String getId() {
		return id;
	}

	/**
	 * set handle
	 * @param id:handle
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * get doc type,'dnrDomain' or 'rirDomain'
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
}