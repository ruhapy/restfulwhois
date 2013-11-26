package com.cnnic.whois.permission;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.sf.json.JSONArray;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.dao.db.AbstractDbQueryDao;
import com.cnnic.whois.dao.db.DbQueryExecutor;
import com.cnnic.whois.util.WhoisUtil;

public class PermissionController {
	private static PermissionController permissionController = new PermissionController();
	private DbQueryExecutor dbQueryExecutor = DbQueryExecutor.getExecutor();

	public static PermissionController getPermissionController() {
		return permissionController;
	}

	public Map<String, Object> removeUnAuthedEntriesMap(
			Map<String, Object> map, QueryType queryType, String role) {
		if (null == map) {
			return map;
		}
		List<String> dnrKeyFields = dbQueryExecutor.getKeyFields(queryType,
				role);
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		List<String> keyFieldsWithoutPrefix = this.removeFieldPrefix(dnrKeyFields);
		for (Iterator<Entry<String, Object>> it = map.entrySet().iterator(); it
				.hasNext();) {
			Entry<String, Object> entry = it.next();
			if (keyFieldsWithoutPrefix.contains(entry.getKey())) {
				resultMap.put(entry.getKey(), entry.getValue());
			}
		}
		for (String field : dnrKeyFields) {
			if (!field.startsWith(WhoisUtil.JOINFILEDPRX)) {
				continue;
			}
			String joinEntityName = field.substring(WhoisUtil.JOINFILEDPRX
					.length());
			Object valueObj = map.get(joinEntityName);
			removeUnAuthedEntriesObject(valueObj, role);
		}
		return resultMap;
	}

	private List<String> removeFieldPrefix(List<String> fields) {
		List<String> result = new ArrayList<String>();
		for (String field : fields) {
			if (field.startsWith(WhoisUtil.JOINFILEDPRX)) {
				field = field.substring(WhoisUtil.JOINFILEDPRX.length());
			} else if (field.startsWith(WhoisUtil.MULTIPRX)) {
				field = field.substring(WhoisUtil.MULTIPRX.length());
			} else if (field.startsWith(WhoisUtil.ARRAYFILEDPRX)) {
				field = field.substring(WhoisUtil.ARRAYFILEDPRX.length());
			} else if (field.startsWith(WhoisUtil.EXTENDPRX)) {
				field = field.substring(WhoisUtil.EXTENDPRX.length());
			}
			result.add(field);
		}
		return result;
	}

	private QueryType getQueryType(Map<String, Object> map) {
		Object queryTypeObj = map.get(AbstractDbQueryDao.QUERY_TYPE);
		if (null != queryTypeObj) {
			return QueryType.getQueryType((String) queryTypeObj);
		}
		return null;
	}

	private void removeUnAuthedEntriesObject(Object object, String role) {
		if(null == object){
			return;
		}
		if (object instanceof Object[]) {
			removeUnAuthedEntriesArray((Object[]) object, role);
		} else if (object instanceof JSONArray) {
			removeUnAuthedEntriesJsonArray((JSONArray) object, role);
		} else if (object instanceof Map) {
			Map<String, Object> map = (Map<String, Object>) object;
			QueryType queryType = getQueryType(map);
			removeUnAuthedEntriesMap(map, queryType, role);
		}
	}

	private void removeUnAuthedEntriesJsonArray(JSONArray object, String role) {
		for (int i = 0; i < object.size(); i++) {
			removeUnAuthedEntriesObject(object.get(i), role);
		}
	}

	private void removeUnAuthedEntriesArray(Object[] array, String role) {
		for (Object object : array) {
			removeUnAuthedEntriesObject(object, role);
		}
	}
}