package com.cnnic.whois.view;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
/**
 * write responce to client 
 * @author nic
 *
 */
public interface ResponseWriter {
	/**
	 * check if support format type
	 * @param formatType:client format type
	 * @return true if support,false if not
	 */
	boolean support(FormatType formatType);

	/**
	 * do format:format key(eg html/json is different) and value(eg ns/entity)
	 * @param map
	 * @return
	 */
	Map<String, Object> format(Map<String, Object> map);

	/**
	 * write response to client
	 * @param request:http request
	 * @param response:http response
	 * @param map:query result
	 * @throws IOException
	 * @throws ServletException
	 */
	void writeResponse(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> map)
			throws IOException, ServletException;

	/**
	 * display over time message
	 * @param request:http request
	 * @param response:http response
	 * @param role :user role
	 * @param queryParam:query param
	 * @throws IOException
	 * @throws ServletException
	 */
	void displayOverTimeMessage(HttpServletRequest request,
			HttpServletResponse response, String role, QueryParam queryParam)
			throws IOException, ServletException;

	/**
	 * 
	 * @param request:http request
	 * @param response:http response
	 * @param queryParam:query param
	 * @throws IOException
	 * @throws ServletException
	 */
	void displayError400(HttpServletRequest request,
			HttpServletResponse response, QueryParam queryParam)
			throws IOException, ServletException;

	/**
	 * 
	 * @param queryType
	 * @param map:query result
	 * @return query result
	 */
	Map<String, Object> getMapKey(QueryType queryType, Map<String, Object> map);
}