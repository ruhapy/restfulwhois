package com.cnnic.whois.bean.index;

/**
 * search condition bean used for search params
 * @author nic
 *
 */
public class SearchCondition {

	private int start = 0;//start row
	private int row = 25;//row num every page
	private String searchword;//search word

	/**
	 * construction
	 * @param searchword:search word
	 */
	public SearchCondition(
			String searchword) {
		this.searchword = searchword;
	}

	/**
	 * get search word
	 * @return search word
	 */
	public String getSearchword() {
		return searchword;
	}

	/**
	 * get start row
	 * @return start row
	 */ 
	public int getStart() {
		return start;
	}

	/**
	 * set start row
	 * @param start:start row
	 */
	public void setStart(
			int start) {
		this.start = start;
	}

	/**
	 * get row num
	 * @return row num
	 */
	public int getRow() {
		return row;
	}

	/**
	 * set row num
	 * @param row:row num
	 */
	public void setRow(
			int row) {
		this.row = row;
	}
}
