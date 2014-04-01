package com.cnnic.whois.bean.index;

/**
 * index bean interface for search
 * 
 * @author nic
 * 
 */
public interface Index {
	/**
	 * get prop value by prop name
	 * 
	 * @param key
	 *            :prop name
	 * @return prop value
	 */
	public String getPropValue(String key);

	/**
	 * init prop map
	 */
	public void initPropValueMap();

	/**
	 * get handle of index
	 * 
	 * @return
	 */
	public String getHandle();

	/**
	 * get doc type,each index type has its own doc type 
	 * 
	 * @return
	 */
	public String getDocType();
}