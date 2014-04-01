package com.cnnic.whois.bean;

import com.cnnic.whois.view.FormatType;
/**
 * entity query param
 * @author nic
 *
 */
public class EntityQueryParam extends QueryParam {
	private String fuzzyQueryParamName;//fuzzy query name

	/**
	 * construction
	 * @param q:query string
	 * @param fuzzyQueryParamName:fuzy query string
	 */
	public EntityQueryParam(String q, String fuzzyQueryParamName) {
		super(q);
		this.fuzzyQueryParamName = fuzzyQueryParamName;
	}

	/**
	 * construction
	 * @param formatType:response format type
	 * @param page:page param
	 */
	public EntityQueryParam(FormatType formatType, PageBean page) {
		super.setFormat(formatType);
		super.setPage(page);
	}

	/**
	 * get fuzy query string
	 * @return
	 */
	public String getFuzzyQueryParamName() {
		return fuzzyQueryParamName;
	}

	/**
	 * set fuzy query string
	 * @param fuzzyQueryParamName:query string
	 */
	public void setFuzzyQueryParamName(String fuzzyQueryParamName) {
		this.fuzzyQueryParamName = fuzzyQueryParamName;
	}
}