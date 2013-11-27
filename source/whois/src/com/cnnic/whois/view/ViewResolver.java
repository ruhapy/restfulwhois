package com.cnnic.whois.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		responseWriters.add(new XmlResponseWriter());
		responseWriters.add(new TextResponseWriter());
	}

	public Map<String, Object> format(Map<String, Object> map,
			FormatType formatType) {
		for (ResponseWriter writer : responseWriters) {
			if (writer.support(formatType)) {
				return writer.format(map);
			}
		}
		return map;
	}
	
	public void writeResponse(FormatType formatType, HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> map, int queryType)
					throws IOException, ServletException {
		for (ResponseWriter writer : responseWriters) {
			if (writer.support(formatType)) {
				writer.writeResponse(request, response, map, formatType.getName(), queryType);
			}
		}
	}
	
	public List<ResponseWriter> getResponseWriters() {
		return responseWriters;
	}

	public void setResponseWriters(List<ResponseWriter> responseWriters) {
		this.responseWriters = responseWriters;
	}
}