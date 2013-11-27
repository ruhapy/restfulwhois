package com.cnnic.whois.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cnnic.whois.util.DataFormat;
import com.cnnic.whois.util.WhoisUtil;

public class XmlResponseWriter extends AbstractResponseWriter {
	private static XmlResponseWriter writer = new XmlResponseWriter();

	public static ResponseWriter getWriter() {
		return writer;
	}

	@Override
	public String formatKey(String keyName) {
		return formatKeyToCamelCaseIfNotJoinKey(keyName);
	}

	@Override
	public void writeResponse(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> map, String format, int queryType)
		throws IOException, ServletException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		PrintWriter out = response.getWriter();
		String errorCode = "200"; 
		
		request.setAttribute("queryFormat", format);
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		//set response status
		if(map.containsKey("errorCode") || map.containsKey("Error Code")){
			if(map.containsKey("errorCode")) 
				errorCode = map.get("errorCode").toString();
			if (map.containsKey("Error Code"))
				errorCode = map.get("Error Code").toString();
			if (errorCode.equals(WhoisUtil.ERRORCODE)){
				response.setStatus(404);
			}
			if (errorCode.equals(WhoisUtil.COMMENDRRORCODE)){
				response.setStatus(400);
			}
		}
		
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
			switch (queryType) {
			case 1:
				map.put("domainSearchResults", jsonObj);
				break;
			case 3:
				map.put("entitySearchResults", jsonObj);
				break;
			case 9:
				map.put("nameserverSearchResults", jsonObj);
				break;
			default:
				map.put(multiKey.substring(WhoisUtil.MULTIPRX.length()), jsonObj);
			}
		}
		
		response.setHeader("Content-Type", format);
		out.write(DataFormat.getXmlString(map));
	}

	@Override
	public boolean support(FormatType formatType) {
		return FormatType.XML.equals(formatType);
	}
}