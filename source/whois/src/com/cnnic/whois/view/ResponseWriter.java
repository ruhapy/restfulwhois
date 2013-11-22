package com.cnnic.whois.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ResponseWriter {
	boolean support(FormatType formatType);

	Map<String, Object> format(Map<String, Object> map);

	void writeResponse(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> map);
}