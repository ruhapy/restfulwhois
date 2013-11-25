package com.cnnic.whois.view;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.sf.json.JSONArray;

public abstract class AbstractResponseWriter implements ResponseWriter {

	abstract protected String formatKey(String keyName);

	@Override
	public Map<String, Object> format(Map<String, Object> map) {
		Map<String, Object> result = formatMap(map);
		return result;
	}

	protected boolean isUnusedEntry(Entry<String, Object> entry) {
		String key = entry.getKey();
		boolean endwithId = key.substring(key.length() - 2).equals("id");
		if (endwithId) {
			return true;
		}
		return false;
	}

	private Map<String, Object> formatMap(Map<String, Object> map) {
		if (null == map) {
			return null;
		}
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (null == entry.getValue()) {
				continue;
			}
			if (isUnusedEntry(entry)) {
				continue;
			}
			Object value = formatObject(entry.getValue());
			String formatedKey = formatKey(entry.getKey());
			resultMap.put(formatedKey, value);
		}
		return resultMap;
	}

	@SuppressWarnings("unchecked")
	private Object formatObject(Object object) {
		if (object instanceof String) {
			return object;
		}
		if (object instanceof Object[]) {
			return formatArray((Object[]) object);
		} else if (object instanceof JSONArray) {
			return formatJSONArray((JSONArray) object);
		} else if (object instanceof Map) {
			return format((Map<String, Object>) object);
		}
		return object;
	}

	private Object formatJSONArray(JSONArray object) {
		JSONArray result = new JSONArray();
		for(int i=0;i<object.size();i++){
			Object formatedObj = formatObject(object.get(i));
			result.add(formatedObj);
		}
		return result;
	}

	private Object formatArray(Object[] array) {
		Object[] result = new Object[array.length];
		int i = 0;
		for (Object object : array) {
			result[i] = formatObject(object);
			i++;
		}
		return result;
	}

	protected String formatKeyToCamelCase(String keyName) {
		String[] names = keyName.split("_");
		keyName = names[0].toLowerCase();
		for (int i = 1; i < names.length; i++) {
			keyName += names[i];
		}
		return keyName;
	}
}