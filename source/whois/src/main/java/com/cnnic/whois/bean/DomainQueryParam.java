package com.cnnic.whois.bean;

import com.cnnic.whois.view.FormatType;
/**
 * domain query param
 * @author nic
 *
 */
public class DomainQueryParam extends QueryParam {
	private String domainPuny;//domain punyname

	/**
	 * constuction
	 * @param q:query param
	 * @param domainPuny:domain punycode
	 */
	public DomainQueryParam(String q, String domainPuny) {
		super(q);
		this.domainPuny = domainPuny;
	}

	/**
	 * constuction
	 * @param formatType:response format type
	 * @param page:page param
	 */
	public DomainQueryParam(FormatType formatType, PageBean page) {
		super.setFormat(formatType);
		super.setPage(page);
	}

	/**
	 * get domain punycode
	 * @return punycode
	 */
	public String getDomainPuny() {
		return domainPuny;
	}

	/**
	 * set domain punycode
	 * @param domainPuny:domain punycode
	 */
	public void setDomainPuny(String domainPuny) {
		this.domainPuny = domainPuny;
	}
}