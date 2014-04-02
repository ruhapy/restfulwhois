package com.cnnic.whois.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
/**
 * resolve view
 * @author nic
 *
 */
@Service
public class ViewResolver {
	private static ViewResolver resolver = new ViewResolver();

	public static ViewResolver getResolver() {
		return resolver;
	}

	/**
	 * call response writer for resolve
	 */
	@Autowired
	private List<ResponseWriter> responseWriters  = new ArrayList<ResponseWriter>();
	
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
	
	/**
	 * formate key/value of result map
	 * @param map :query result map
	 * @param formatType:format type
	 * @return query result after formated
	 */
	public Map<String, Object> format(Map<String, Object> map,
			FormatType formatType) {
		for (ResponseWriter writer : responseWriters) {
			if (writer.support(formatType)) {
				return writer.format(map);
			}
		}
		return map;
	}

	/**
	 * write response to client
	 * @param formatType:format type
	 * @param queryType:query type
	 * @param request:http request
	 * @param response:http response
	 * @param map:query result map
	 * @throws IOException
	 * @throws ServletException
	 */
	public void writeResponse(FormatType formatType, QueryType queryType,
			HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> map) throws IOException,
			ServletException {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		for (ResponseWriter writer : responseWriters) {
			if (writer.support(formatType)) {
				result = writer.getMapKey(queryType, map);
			    writer.writeResponse(request, response, result);
			}
		}
	}

	/**
	 * display over time message 
	 * @param request:http request
	 * @param response:http response
	 * @param formatType:format type
	 * @param role:user role
	 * @param queryParam:query param
	 * @throws IOException
	 * @throws ServletException
	 */
	public void displayOverTimeMessage(HttpServletRequest request,
			HttpServletResponse response, FormatType formatType, String role,QueryParam queryParam)
			throws IOException, ServletException {
		for (ResponseWriter writer : responseWriters) {
			if (writer.support(formatType)) {
				writer.displayOverTimeMessage(request, response, role,queryParam);
				return;
			}
		}
	}
	
	/**
	 * display 400 error
	 * @param request:http request
	 * @param response:http response
	 * @param queryParam:query param
	 * @throws IOException
	 * @throws ServletException
	 */
	public void displayError400(HttpServletRequest request,
			HttpServletResponse response,QueryParam queryParam)
			throws IOException, ServletException {
		for (ResponseWriter writer : responseWriters) {
			if (writer.support(queryParam.getFormat())) {
				writer.displayError400(request, response, queryParam);
				return;
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
