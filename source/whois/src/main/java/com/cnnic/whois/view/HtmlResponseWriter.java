package com.cnnic.whois.view;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.util.DataFormat;
import com.cnnic.whois.util.WhoisUtil;
/**
 * html response writer
 * @author nic
 *
 */
@Component("htmlResponseWriter")
public class HtmlResponseWriter extends AbstractResponseWriter {
	private static HtmlResponseWriter writer = new HtmlResponseWriter();

	public static ResponseWriter getWriter() {
		return writer;
	}

	/**
	 * html format key, replace _ by space
	 */
	public String formatKey(String keyName) {
		return keyName.replaceAll("_", " ");
	}

	@Override
	public void writeResponse(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> map)
		throws IOException, ServletException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		Map<String, Object> htmlMap = new LinkedHashMap<String, Object>();
		htmlMap.putAll(map);
		
		String errorCode = "200"; 
		
		request.setAttribute("queryFormat", FormatType.HTML .getName());
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		//set response status
		htmlMap.remove("queryType");
		errorCode = writeResponseCode(response, map, errorCode);
		
		//multi-responses
		Iterator<String> iterr = map.keySet().iterator();
		String multiKey = null;
		while(iterr.hasNext()){
			String key =  iterr.next();
			if(key.startsWith(WhoisUtil.MULTIPRX)){
				multiKey = key;
			}
		}		
		if(multiKey != null){
			Object jsonObj = map.get(multiKey);
			map.remove(multiKey);
			map.put(multiKey.substring(WhoisUtil.MULTIPRX.length()), jsonObj);
		}
		
		if(!errorCode.equals(WhoisUtil.ERRORCODE) && !errorCode.equals(WhoisUtil.COMMENDRRORCODE)){
			if(htmlMap.containsKey(WhoisUtil.RDAPCONFORMANCEKEY))
				htmlMap.remove(WhoisUtil.RDAPCONFORMANCEKEY);
			if(htmlMap.containsKey(WhoisUtil.SEARCH_RESULTS_TRUNCATED_EKEY))
				htmlMap.remove(WhoisUtil.SEARCH_RESULTS_TRUNCATED_EKEY);
			request.setAttribute("JsonObject", DataFormat.getJsonObject(htmlMap));
			RequestDispatcher rdsp = request.getRequestDispatcher("/index.jsp");
			response.setContentType(FormatType.HTML.getName());
			rdsp.forward(request, response); 
		}else{
			response.sendError(Integer.parseInt(errorCode));
		}
	}

	public void displayErrorMessage(HttpServletRequest request, HttpServletResponse response, FilterChain chain, 
			String queryType, String role) throws IOException, ServletException{
		chain.doFilter(request, response);
	}
	
	/**
	 * display overtime message
	 */
	public void displayOverTimeMessage(HttpServletRequest request, HttpServletResponse response, 
			String role,QueryParam queryParam) throws IOException, ServletException{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		request.setAttribute("queryFormat", FormatType.HTML.getName());
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setStatus(429);
		response.sendError(429);
	}
	
	@Override
	protected boolean isUnusedEntry(Entry<String, Object> entry) {
		return false;
	}

	@Override
	public boolean support(FormatType formatType) {
		return null != formatType && formatType.isHtmlFormat();
	}
	
	public Map<String, Object> getMultiMapKey(QueryType queryType, Map<String, Object> map) {
		return map;
	}

	@Override
	public void displayError400(HttpServletRequest request,
			HttpServletResponse response, QueryParam queryParam)
			throws IOException, ServletException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		request.setAttribute("queryFormat", FormatType.HTML.getName());
		response.setStatus(400);
		response.sendError(400);
	}
}
