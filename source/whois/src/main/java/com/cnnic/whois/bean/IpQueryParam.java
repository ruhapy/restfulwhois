package com.cnnic.whois.bean;

import com.cnnic.whois.view.FormatType;

public class IpQueryParam extends QueryParam {
	private String ipInfo;
	private int ipLength;
	
	private long startHighAddr;
	private long endHighAddr;
	private long startLowAddr;
	private long endLowAddr;

	/**
	 * construction
	 * @param q:ip query string
	 * @param startHighAddr: start high ip address
	 * @param endHighAddr:end high ip address
	 * @param startLowAddr:start low ip address
	 * @param endLowAddr:end low ip address
	 */
	public IpQueryParam(String q, long startHighAddr, long endHighAddr,
			long startLowAddr, long endLowAddr) {
		super(q);
		this.startHighAddr = startHighAddr;
		this.endHighAddr = endHighAddr;
		this.startLowAddr = startLowAddr;
		this.endLowAddr = endLowAddr;
	}

	
	/**
	 * construction
	 * @param formatType:response format type
	 * @param page:page param
	 */
	public IpQueryParam(FormatType formatType, PageBean page) {
		super();
		super.setFormat(formatType);
	}

	/**
	 * get ip query string
	 * @return ip query string
	 */
	public String getIpInfo() {
		return ipInfo;
	}

	/**
	 * set ip query string
	 * @param ipInfo:ip query string
	 */
	public void setIpInfo(String ipInfo) {
		this.ipInfo = ipInfo;
	}

	/**
	 * get ip lengh
	 * @return ip lengh
	 */
	public int getIpLength() {
		return ipLength;
	}

	/**
	 * set ip lengh
	 * @param ipLength
	 */
	public void setIpLength(int ipLength) {
		this.ipLength = ipLength;
	}

	/**
	 * get start high address
	 * @return start high address
	 */
	public long getStartHighAddr() {
		return startHighAddr;
	}

	/**
	 * set start high address
	 * @param startHighAddr:start high address
	 */
	public void setStartHighAddr(long startHighAddr) {
		this.startHighAddr = startHighAddr;
	}

	/**
	 * get end high address
	 * @return:end high address
	 */
	public long getEndHighAddr() {
		return endHighAddr;
	}

	/**
	 * set end high address
	 * @param endHighAddr:end high address
	 */
	public void setEndHighAddr(long endHighAddr) {
		this.endHighAddr = endHighAddr;
	}

	/**
	 * get start low address
	 * @return start low address
	 */
	public long getStartLowAddr() {
		return startLowAddr;
	}

	/**
	 * set start low address
	 * @param startLowAddr:start low address
	 */
	public void setStartLowAddr(long startLowAddr) {
		this.startLowAddr = startLowAddr;
	}

	/**
	 * get end low address
	 * @return end low address
	 */
	public long getEndLowAddr() {
		return endLowAddr;
	}

	/**
	 * set end low address
	 * @param endLowAddr:end low address
	 */
	public void setEndLowAddr(long endLowAddr) {
		this.endLowAddr = endLowAddr;
	}
}