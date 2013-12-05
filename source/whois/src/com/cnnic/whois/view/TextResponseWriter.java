package com.cnnic.whois.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cnnic.whois.util.DataFormat;
import com.cnnic.whois.util.WhoisUtil;

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
				map.put(multiKey.substring(WhoisUtil.MULTIPRX.length()), jsonObj);
			}
			
			response.setHeader("Content-Type", format);
			out.write(getPresentationFromMap(map, 0));
	}

	@Override
	public boolean support(FormatType formatType) {
		return FormatType.TEXTPLAIN.equals(formatType);
	}
	
	protected String getPresentationFromMap(Map<String, Object> map, int iMode) {
		StringBuffer sb = new StringBuffer();

		if (map == null || map.size() == 0) {
			return "";
		}

		Iterator<String> iterr = map.keySet().iterator();

		while (iterr.hasNext()) {
			String key = (String) iterr.next();

			if (map.get(key) instanceof Map) {
				for(int i = 0; i < iMode; i++){
					sb.append(WhoisUtil.BLANKSPACE);
				}
				sb.append(delTrim(key) + ":\n");
				sb.append(getPresentationFromMap(
						(Map<String, Object>) map.get(key), iMode + 1));
				
			} else if (map.get(key) instanceof Object[]) {
				for(int i = 0; i < iMode; i++){
					sb.append(WhoisUtil.BLANKSPACE);
				}
				sb.append(delTrim(key) + ":\n");
				for (Object obj : (Object[]) map.get(key)) {
					if (obj instanceof Map) {
						sb.append(getPresentationFromMap(
								(Map<String, Object>) obj, iMode + 1));
					}else{
						if(obj != null && !obj.toString().trim().equals("")){
							for(int i = 0; i < iMode; i++){
								sb.append(WhoisUtil.BLANKSPACE);
							}
							if (key.equals("vcardArray")){
								if (!(obj instanceof ArrayList)){
									sb.append(WhoisUtil.BLANKSPACE);
									sb.append(obj + "\n");
								}else{									
									List<List<Object>> listVcard = new ArrayList<List<Object>>();
									listVcard = (ArrayList<List<Object>>)obj;
									for (int m = 0; m < listVcard.size(); m++){
										List<Object> listElement;
										listElement = listVcard.get(m);
										if (listElement.get(0).equals("adr")){
											List<List<Object>> listAdr = new ArrayList<List<Object>>();
											listAdr = (ArrayList<List<Object>>)listElement.get(3);
											sb.append(WhoisUtil.BLANKSPACE + WhoisUtil.BLANKSPACE);
											sb.append("adr:"+listAdr.get(0));
											for (int n = 1; n < listAdr.size(); n++){
												sb.append("," + listAdr.get(n));
											}
											sb.append("\n");
											continue;
										}
										sb.append(WhoisUtil.BLANKSPACE + WhoisUtil.BLANKSPACE);
										sb.append(listElement.get(0) + ":" + listElement.get(3) +"\n");
									}
								}								
							} else {
								sb.append(obj + "\n");
							}
							
						}
					}
				}
			} else {
				for(int i = 0; i < iMode; i++){
					sb.append(WhoisUtil.BLANKSPACE);
				}
				sb.append(delTrim(key) + ":");
				sb.append(map.get(key) + "\n");
			}
		}
		return sb.toString();
	}
}