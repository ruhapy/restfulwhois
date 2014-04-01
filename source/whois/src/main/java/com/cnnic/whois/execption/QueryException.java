package com.cnnic.whois.execption;
/**
 * query service exception
 * @author nic
 *
 */
public class QueryException extends Exception {
	/**
	 * constructed a object
	 */
	public QueryException() {
	}

	/**
	 * Use msg constructed a new object
	 * 
	 * @param msg : exception msg
	 */

	public QueryException(String msg) {
		super(msg);
	}

	/**
	 * Use msg constructed a new object
	 * 
	 * @param msg:exception msg
	 */
	public QueryException(Exception msg) {
		super(msg);
	}
}