package com.cnnic.whois.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TextResponseWriter extends AbstractResponseWriter {
	private static TextResponseWriter writer = new TextResponseWriter();

	public static ResponseWriter getWriter() {
		return writer;
	}

	@Override
	public String formatKey(String keyName) {
		return keyName;
	}

	@Override
	public void writeResponse(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> map) {

	}

	@Override
	public boolean support(FormatType formatType) {
		return FormatType.TEXTPLAIN.equals(formatType);
	}
}