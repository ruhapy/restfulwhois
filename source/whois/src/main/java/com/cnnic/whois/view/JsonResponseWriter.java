package com.cnnic.whois.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.DataFormat;
import com.cnnic.whois.util.WhoisUtil;
/**
 * json response writer
 * @author nic
 *
 */
@Component("jsonResponseWriter")
public class JsonResponseWriter extends AbstractResponseWriter {
	private static JsonResponseWriter writer = new JsonResponseWriter();
	
	public static ResponseWriter getWriter() {
		return writer;
	}

	@Override
	public String formatKey(String keyName) {
		return formatKeyToCamelCaseIfNotJoinKey(keyName);
	}

	@Override
	public void writeResponse(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> map)
		throws IOException, ServletException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		PrintWriter out = response.getWriter();
		String errorCode = "200"; 
		
		request.setAttribute("queryFormat", FormatType.JSON.getName());
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		errorCode = writeResponseCode(response, map, errorCode);
		response.setHeader("Content-Type", FormatType.RDAPANDJSON.getName());
		out.print(DataFormat.getJsonObject(map));
	}
	
	/**
	 * display overtime message
	 */
	public void displayOverTimeMessage(HttpServletRequest request, HttpServletResponse response,  
			String role,QueryParam queryParam) throws IOException, ServletException{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		try {
			map = WhoisUtil.processError(WhoisUtil.RATELIMITECODE,queryParam);
		} catch (QueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		request.setAttribute("queryFormat", FormatType.JSON.getName());
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setStatus(429);
		response.setHeader("Content-Type", FormatType.RDAPANDJSON.getName());
		out.print(DataFormat.getJsonObject(map));
	}
	
	@Override
	public boolean support(FormatType formatType) {
		return null != formatType && formatType.isJsonFormat();
	}

	@Override
	public void displayError400(HttpServletRequest request,
			HttpServletResponse response, QueryParam queryParam)
			throws IOException, ServletException {request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			try {
				map = WhoisUtil.processError(WhoisUtil.COMMENDRRORCODE,queryParam);
			} catch (QueryException e) {
				e.printStackTrace();
			}
			PrintWriter out = response.getWriter();
			request.setAttribute("queryFormat", FormatType.JSON.getName());
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setStatus(Integer.valueOf(WhoisUtil.COMMENDRRORCODE));
			response.setHeader("Content-Type", FormatType.RDAPANDJSON.getName());
			out.print(DataFormat.getJsonObject(map));}
}
