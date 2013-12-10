package com.cnnic.whois.view;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class ViewResolver {
	private static ViewResolver resolver = new ViewResolver();

	public static ViewResolver getResolver() {
		return resolver;
	}

	@Autowired
	private List<ResponseWriter> responseWriters;

	public Map<String, Object> format(Map<String, Object> map,
			FormatType formatType) {
		for (ResponseWriter writer : responseWriters) {
			if (writer.support(formatType)) {
				return writer.format(map);
			}
		}
		return map;
	}

	public void writeResponse(FormatType formatType,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> map, int queryType) throws IOException,
			ServletException {
		for (ResponseWriter writer : responseWriters) {
			if (writer.support(formatType)) {
				writer.writeResponse(request, response, map,
						formatType.getName(), queryType);
			}
		}
	}

	public void displayErrorMessage(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain,
			FormatType formatType, String queryType, String role)
			throws IOException, ServletException {
		for (ResponseWriter writer : responseWriters) {
			if (writer.support(formatType)) {
				writer.displayErrorMessage(request, response, chain,
						formatType.getName(), queryType, role);
			}
		}
	}

	public void displayOverTimeMessage(HttpServletRequest request,
			HttpServletResponse response, FormatType formatType, String role)
			throws IOException, ServletException {
		for (ResponseWriter writer : responseWriters) {
			if (writer.support(formatType)) {
				writer.displayOverTimeMessage(request, response,
						formatType.getName(), role);
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
