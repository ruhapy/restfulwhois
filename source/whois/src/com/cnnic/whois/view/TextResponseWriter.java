package com.cnnic.whois.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.WhoisUtil;

@Component("textResponseWriter")
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
			HttpServletResponse response, Map<String, Object> map) 
		throws IOException, ServletException {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			PrintWriter out = response.getWriter();
			String errorCode = "200"; 
			
			request.setAttribute("queryFormat", FormatType.TEXTPLAIN.getName());
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
							
			response.setHeader("Content-Type", FormatType.TEXTPLAIN.getName());
			out.write(getPresentationFromMap(map, 0));
	}
	
	public void displayOverTimeMessage(HttpServletRequest request, HttpServletResponse response, 
			String role) throws IOException, ServletException{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		try {
			map = WhoisUtil.processError(WhoisUtil.RATELIMITECODE);
		} catch (QueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		request.setAttribute("queryFormat", FormatType.TEXTPLAIN.getName());
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setStatus(429);
		response.setHeader("Content-Type", FormatType.TEXTPLAIN.getName());
		out.write(getPresentationFromMap(map, 0));
	}
	
	@Override
	public boolean support(FormatType formatType) {
		return null != formatType && formatType.isTextFormat();
	}
	
	@SuppressWarnings("unchecked")
	protected String getPresentationFromMap(Map<String, Object> map, int iMode) {
		StringBuffer sb = new StringBuffer();

		if (map == null || map.size() == 0) {
			return "";
		}

		Iterator<String> iterr = map.keySet().iterator();

		while (iterr.hasNext()) {
			String key = (String) iterr.next();
			if (key.equals("rdapConformance")){
				continue;
			}
			if (key.equals("vcardArray")){
				sb.append(getPresentationFromVcard(map.get(key), iMode));
				continue;
			}
			
			if (map.get(key) instanceof Map) {
				for(int i = 0; i < iMode; i++){
					sb.append(WhoisUtil.BLANKSPACE);
				}
				sb.append(delTrim(key) + " : \n");
				sb.append(getPresentationFromMap(
						(Map<String, Object>) map.get(key), iMode + 1));	
			}else if (map.get(key) instanceof Object[]) {
				for (Object obj : (Object[]) map.get(key)) {
					if (obj instanceof Map) {
						for(int i = 0; i < iMode; i++){
							sb.append(WhoisUtil.BLANKSPACE);
						}
						sb.append(delTrim(key) + " : \n");
						sb.append(getPresentationFromMap(
								(Map<String, Object>) obj, iMode + 1));
					} else if (obj instanceof List) {
						if (obj instanceof JSONArray){
							sb.append(parseJSONArray(map.get(key), key, iMode + 1));
						}
					} else {
						if(obj != null && !obj.toString().trim().equals("")){
							for(int i = 0; i < iMode; i++){
								sb.append(WhoisUtil.BLANKSPACE);
							}
							sb.append(delTrim(key) + " : " + obj + "\n");
						}
					}
				}
			} else if (map.get(key) instanceof List) {
				if (map.get(key) instanceof JSONArray){
					sb.append(parseJSONArray(map.get(key), key, iMode));
				}			
			}else {	
				if(map.get(key) != null && !map.get(key).toString().trim().equals("")){
					for(int i = 0; i < iMode; i++){
						sb.append(WhoisUtil.BLANKSPACE);
					}
					sb.append(delTrim(key) + " : " + map.get(key) + "\n");
				}	
			}
		}
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	protected String getPresentationFromVcard(Object VcardData, int iMode) {
		StringBuffer sb = new StringBuffer();		
		Object[] VcardValueArray;
		
		if (VcardData instanceof JSONArray){
			VcardValueArray = ((JSONArray) VcardData).toArray();
	    } else {
		    VcardValueArray = (Object [])VcardData;
	    }
		
		List<Object> VcardValueList = (List<Object>)VcardValueArray[1];
	    for (int i = 0; i < VcardValueList.size(); i++){
	    	List<Object> vcard = (List<Object>)VcardValueList.get(i);
		    for (int j = 0; j < vcard.size(); j++) {
				String keyName = (String) vcard.get(j);				
				if (vcard.get(j).equals("adr")) {
					Object value = vcard.get(3);
					List<String> valueList = (List<String>)value; 	
					for (int k = 1; k < valueList.size(); k++){
						if(valueList.get(k) != null && !valueList.get(k).toString().trim().equals("")){
							for(int l = 0; l < iMode; l++){
								sb.append(WhoisUtil.BLANKSPACE);
							}
							sb.append(keyName + " : " + valueList.get(k) + "\n");
					    }
					}
					continue;				
				} else if (vcard.get(j).equals("tel")) {
					if (((String) vcard.get(j + 1)).indexOf("work") != -1) {
						keyName = "office";
					} else if (((String) vcard.get(j + 1)).indexOf("fax") != -1) {
						keyName = "fax";
					} else if (((String) vcard.get(j + 1)).indexOf("cell") != -1) {
						keyName = "moblie";
					} else {
						keyName = "phonesId";
					}
				}
				if(vcard.get(j + 3) != null && !vcard.get(j + 3).toString().trim().equals("")){
					for(int k = 0; k < iMode; k++){
						sb.append(WhoisUtil.BLANKSPACE);
					}
					sb.append(keyName + " : " + vcard.get(j + 3) + "\n");
			    }	
			}
	    }	    
	    return sb.toString();		
	}
	
	protected StringBuffer parseJSONObject(Object object, int iMode){
		StringBuffer sb = new StringBuffer();		
		JSONObject jsonObject = (JSONObject) object;				
		Map<String, Object> map = new LinkedHashMap<String, Object>(); 
		
		Iterator<?> iterator = jsonObject.keys();   
        while(iterator.hasNext()){ 
            String key = (String) iterator.next().toString();                         
            Object obj = jsonObject.get(key); 
            map.put(key, obj);
        }
        sb.append(getPresentationFromMap(map, iMode));
		return sb;
	}
	
	protected StringBuffer parseJSONArray(Object object, String key, int iMode){
		StringBuffer sb = new StringBuffer();		
		JSONArray jsonArray = (JSONArray) object;
	
		if (jsonArray.get(0) instanceof JSONObject){
			for(int i = 0; i < iMode; i++){
				sb.append(WhoisUtil.BLANKSPACE);
			}
			sb.append(delTrim(key) + " : \n");
			for (int i = 0; i < jsonArray.size(); i++) {
				sb.append(parseJSONObject(jsonArray.get(i), iMode + 1)); 
			}
		} else {
			for (int i = 0; i < jsonArray.size(); i++) {
				if(jsonArray.get(i) != null && !jsonArray.get(i).toString().trim().equals("")){
					for(int j = 0; j < iMode; j++){
						sb.append(WhoisUtil.BLANKSPACE);
					}
					sb.append(delTrim(key) + " : " + jsonArray.get(i) + "\n");			
				}	
			}
		}
        return sb;
	}
	
	public Map<String, Object> getMultiMapKey(QueryType queryType, Map<String, Object> map) {
		return map;
	}
}
