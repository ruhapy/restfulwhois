package com.cnnic.whois.bean;

public class IpQueryParam extends QueryParam {
	private String startHighAddr;
	private String endHighAddr;
	private String startLowAddr;
	private String endLowAddr;

	public String getStartHighAddr() {
		return startHighAddr;
	}

	public void setStartHighAddr(String startHighAddr) {
		this.startHighAddr = startHighAddr;
	}

	public String getEndHighAddr() {
		return endHighAddr;
	}

	public void setEndHighAddr(String endHighAddr) {
		this.endHighAddr = endHighAddr;
	}

	public String getStartLowAddr() {
		return startLowAddr;
	}

	public void setStartLowAddr(String startLowAddr) {
		this.startLowAddr = startLowAddr;
	}

	public String getEndLowAddr() {
		return endLowAddr;
	}

	public void setEndLowAddr(String endLowAddr) {
		this.endLowAddr = endLowAddr;
	}
}