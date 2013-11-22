package com.cnnic.whois.view;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HtmlResponseWriter extends AbstractResponseWriter {
	private static HtmlResponseWriter writer = new HtmlResponseWriter();

	public static ResponseWriter getWriter() {
		return writer;
	}

	public String formatKey(String keyName) {
		return keyName.replaceAll("_", " ");
	}

	@Override
	public void writeResponse(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> map) {

	}

	@Override
	protected boolean isUnusedEntry(Entry<String, Object> entry) {
		return false;
	}

	@Override
	public boolean support(FormatType formatType) {
		return FormatType.HTML.equals(formatType);
	}
}