package com.cnnic.whois.execption;

public class PoxyException extends Exception {
	/**
	 * constructed a object
	 */
	public PoxyException() {
	}

	/**
	 * Use msg constructed a new object
	 * 
	 * @param msg
	 */
	public PoxyException(String msg) {
		super(msg);
	}

	/**
	 * Use msg constructed a new object
	 * 
	 * @param msg
	 */
	public PoxyException(Exception msg) {
		super(msg);
	}
}
