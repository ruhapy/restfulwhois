package com.cnnic.whois.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewResolver {
	private static ViewResolver resolver = new ViewResolver();

	public static ViewResolver getResolver() {
		return resolver;
	}

	private List<ResponseWriter> responseWriters = new ArrayList<ResponseWriter>();

	public ViewResolver() {
		super();
		init();
	}

	private void init() {
		responseWriters.add(new JsonResponseWriter());
		responseWriters.add(new HtmlResponseWriter());
	}

	public Map<String, Object> format(Map<String, Object> map, FormatType formatType) {
		for (ResponseWriter writer : responseWriters) {
			if (writer.support(formatType)) {
				return writer.format(map);
			}
		}
		return map;
	}
}