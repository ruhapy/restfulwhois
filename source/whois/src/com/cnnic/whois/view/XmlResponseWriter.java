package com.cnnic.whois.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		out.write(getXMLFromMap(map, 0));
	}

	@Override
	public boolean support(FormatType formatType) {
		return null != formatType && formatType.isXmlFormat();
	}
	
	protected String getXMLFromMap(Map<String, Object> map, int iMode) {
		StringBuffer sb = new StringBuffer();

		if (map == null || map.size() == 0) {
			return "";
		}
		if (0 == iMode) {
			sb.append("<root>\n");
		}

		Iterator<String> iterr = map.keySet().iterator();

		while (iterr.hasNext()) {
			String key = (String) iterr.next();
			if (key.equals("vcardArray")){
				sb.append(getXMLFromVcard(map.get(key)));
				continue;
			}

			if (map.get(key) instanceof Map) {
				sb.append("<" + delTrim(key) + ">\n");
				sb.append(getXMLFromMap((Map<String, Object>) map.get(key),
						iMode + 1));
				sb.append("</" + delTrim(key) + ">\n");
			} else if (map.get(key) instanceof Object[]) {
				sb.append("<" + delTrim(key) + ">\n");
				for (Object obj : (Object[]) map.get(key)) {
					int count = ((Object[]) map.get(key)).length;
					
					if (obj instanceof Map) {
						sb.append(getXMLFromMap((Map<String, Object>) obj,
								iMode + 2));
					} else if (obj instanceof List) {
						sb.append(toVCardXml((List<String>) obj));
					} else {
						if(obj != null && !obj.toString().trim().equals("")){
							sb.append(obj);
						}
						if(((Object[]) map.get(key))[count - 1] != obj) {
							sb.append("</" + delTrim(key) + ">\n");
							sb.append("<" + delTrim(key) + ">\n");
						}
					}
				}
				sb.append("</" + delTrim(key) + ">\n");
			} else if (map.get(key) instanceof List) {
				String[] values = ((List<String>) map.get(key)).get(0).split(",");
				for (String value : values) {
					sb.append("<" + delTrim(key) + ">\n");
					sb.append(value);
					sb.append("</" + delTrim(key) + ">\n");
				}

			} else {
				sb.append("<" + delTrim(key) + ">\n");
				sb.append(map.get(key));
				sb.append("</" + delTrim(key) + ">\n");
			}
		}
		if (0 == iMode)
			sb.append("</root>\n");
		return sb.toString();
	}
	
	protected String getXMLFromVcard(Object VcardData) {
		StringBuffer sb = new StringBuffer();
		Object [] VcardValueArray = (Object [])VcardData;
		List<Object> VcardValueList = (List<Object>)VcardValueArray[1];

		sb.append("<" + delTrim("vcardArray") + ">\n");
		sb.append("<" + "vcard" + ">\n");

		for (int i = 0; i < VcardValueList.size(); i++){
			sb.append(toVCardXml((List<String>)VcardValueList.get(i)));
		}
		sb.append("</" + "vcard" + ">\n");
		sb.append("</" + delTrim("vcardArray") + ">\n");
		return sb.toString();
	}
	
	protected StringBuffer toVCardXml(List<String> vcard) {
		StringBuffer sb = new StringBuffer();		
		for (int i = 0; i < vcard.size(); i++) {
			String keyName = "";
			if (vcard.get(i).equals("version")) {
				keyName = "Version";
			} else if (vcard.get(i).equals("fn")) {
				keyName = "Fn";
			} else if (vcard.get(i).equals("bday")) {
				keyName = "Bday";
			} else if (vcard.get(i).equals("anniversary")) {
				keyName = "Anniversary";
			} else if (vcard.get(i).equals("gender")) {
				keyName = "Gender";
			} else if (vcard.get(i).equals("kind")) {
				keyName = "Kind";
			} else if (vcard.get(i).equals("lang")) {
				keyName = "Lang";
				String keyNameFront = "";
				String Attribute = vcard.get(1);
				String [] AttributeList;
				Attribute = Attribute.replace("{", "");
				Attribute = Attribute.replace("}", "");
				AttributeList = Attribute.split(":");
				keyNameFront = keyName + " " + AttributeList[0].replace("\"", "") + "=" + AttributeList[1];
				i = i + 3;
				sb.append("<" + keyNameFront + ">\n");
				sb.append("<" + vcard.get(2)+ ">\n");
				sb.append(vcard.get(i));
				sb.append("</" + vcard.get(2)+ ">\n");
				sb.append("</" + keyName + ">\n");
				continue;		
			} else if (vcard.get(i).equals("org")) {
				keyName = "Org";
				String keyNameFront = "";
				String Attribute = vcard.get(1);
				String [] AttributeList;
				Attribute = Attribute.replace("{", "");
				Attribute = Attribute.replace("}", "");
				AttributeList = Attribute.split(":");
				keyNameFront = keyName + " " + AttributeList[0].replace("\"", "") + "=" + AttributeList[1];
				i = i + 3;
				sb.append("<" + keyNameFront + ">\n");
				sb.append("<" + vcard.get(2)+ ">\n");
				sb.append(vcard.get(i));
				sb.append("</" + vcard.get(2)+ ">\n");
				sb.append("</" + keyName + ">\n");
				continue;
			} else if (vcard.get(i).equals("title")) {
				keyName = "Title";
			} else if (vcard.get(i).equals("role")) {
				keyName = "Role";
			} else if (vcard.get(i).equals("adr")) {
				keyName = "Adr";
				String keyNameFront = "";
				String Attribute = vcard.get(1);
				String [] AttributeList;
				Attribute = Attribute.replace("{", "");
				Attribute = Attribute.replace("}", "");
				AttributeList = Attribute.split(":");
				keyNameFront = keyName + " " + AttributeList[0].replace("\"", "") + "=" + AttributeList[1];
				i = i + 3;
				
				Object Value = vcard.get(3);
				List<String> ValueList = (List<String>)Value; 
				for (int k = 0; k < ValueList.size(); k++){
					sb.append("<" + keyNameFront + ">\n");					
					sb.append("<" + vcard.get(2)+ ">\n");
					sb.append(ValueList.get(k));
					sb.append("</" + vcard.get(2)+ ">\n");
					sb.append("</" + keyName + ">\n");
				}
				continue;				
			} else if (vcard.get(i).equals("geo")) {
				keyName = "Geo";
				String keyNameFront = "";
				String Attribute = vcard.get(1);
				String [] AttributeList;
				Attribute = Attribute.replace("{", "");
				Attribute = Attribute.replace("}", "");
				AttributeList = Attribute.split(":");
				keyNameFront = keyName + " " + AttributeList[0].replace("\"", "") + "=" + AttributeList[1];
				i = i + 3;
				sb.append("<" + keyNameFront + ">\n");
				sb.append("<" + vcard.get(2)+ ">\n");
				sb.append(vcard.get(i));
				sb.append("</" + vcard.get(2)+ ">\n");
				sb.append("</" + keyName + ">\n");
				continue;
			}  else if (vcard.get(i).equals("key")) {
				keyName = "Key";
				String keyNameFront = "";
				String Attribute = vcard.get(1);
				String [] AttributeList;
				Attribute = Attribute.replace("{", "");
				Attribute = Attribute.replace("}", "");
				AttributeList = Attribute.split(":");
				keyNameFront = keyName + " " + AttributeList[0].replace("\"", "") + "=" + AttributeList[1];
				i = i + 3;
				sb.append("<" + keyNameFront + ">\n");
				sb.append("<" + vcard.get(2)+ ">\n");
				sb.append(vcard.get(i));
				sb.append("</" + vcard.get(2)+ ">\n");
				sb.append("</" + keyName + ">\n");
				continue;
			} else if (vcard.get(i).equals("tz")) {
				keyName = "Tz";
			} else if (vcard.get(i).equals("url")) {
				keyName = "Url";
			} else if (vcard.get(i).equals("email")) {
				keyName = "Email";
			} else if (vcard.get(i).equals("tel")) {
				if (vcard.get(i + 1).indexOf("work") != -1) {
					keyName = "Office";
				} else if (vcard.get(i + 1).indexOf("fax") != -1) {
					keyName = "Fax";
				} else if (vcard.get(i + 1).indexOf("cell") != -1) {
					keyName = "Moblie";
				} 
				else {
					keyName = "phonesId";
				}
			}
			i = i + 3;
			sb.append("<" + keyName + ">\n");
			sb.append("<" + vcard.get(2)+ ">\n");
			sb.append(vcard.get(i));
			sb.append("</" + vcard.get(2)+ ">\n");
			sb.append("</" + keyName + ">\n");
		}
		return sb;
	}
}