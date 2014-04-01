package com.cnnic.whois.bean;

import com.cnnic.whois.view.FormatType;
/**
 * query param bean
 * @author nic
 *
 */
public class QueryParam {
	private QueryType queryType;
	private FormatType format;
	private String q;
	private boolean isFuzzyQ = false;
	private PageBean page;

	/**
	 * construction
	 * @param q:query string
	 */
	public QueryParam(String q) {
		this.q = q;
	}
	
	/**
	 * construction
	 */
	public QueryParam() {
	}

	/**
	 * construction
	 * @param format:response type
	 * @param q:query string
	 * @param isFuzzyQ:is fuzzy query
	 * @param page:page param
	 */
	public QueryParam(FormatType format, String q, boolean isFuzzyQ,
			PageBean page) {
		super();
		this.format = format;
		this.q = q;
		this.isFuzzyQ = isFuzzyQ;
		this.page = page;
	}

	/**
	 * construction
	 * @param format:response type
	 * @param page:page param
	 */
	public QueryParam(FormatType format, PageBean page) {
		super();
		this.format = format;
		this.page = page;
	}

	/**
	 * get query type
	 * @return:query type
	 */
	public QueryType getQueryType() {
		return queryType;
	}

	/**
	 * set query type
	 * @param queryType:query type
	 */
	public void setQueryType(QueryType queryType) {
		this.queryType = queryType;
	}

	/**
	 * get page param
	 * @return page param
	 */
	public PageBean getPage() {
		return page;
	}

	/**
	 * set page param
	 * @param page:page param
	 */
	public void setPage(PageBean page) {
		this.page = page;
	}

	/**
	 * get response format
	 * @return response format
	 */
	public FormatType getFormat() {
		return format;
	}

	/**
	 * set response format
	 * @param format:response format
	 */
	public void setFormat(FormatType format) {
		this.format = format;
	}

	/**
	 * get query string
	 * @return query string
	 */
	public String getQ() {
		return q;
	}

	/**
	 * set query string
	 * @param q:query string
	 */
	public void setQ(String q) {
		this.q = q;
	}

	/**
	 * is fuzzy query
	 * @return fuzzy query
	 */
	public boolean isFuzzyQ() {
		return isFuzzyQ;
	}
	/**
	 * set fuzzy query
	 * @param isFuzzyQ:fuzzy query
	 */
	public void setFuzzyQ(boolean isFuzzyQ) {
		this.isFuzzyQ = isFuzzyQ;
	}
}