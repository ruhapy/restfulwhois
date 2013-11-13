package com.cnnic.whois.dao.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.cnnic.whois.bean.QueryJoinType;
import com.cnnic.whois.service.DomainIndexService;
import com.cnnic.whois.service.EntityIndexService;
import com.cnnic.whois.service.NameServerIndexService;
import com.cnnic.whois.util.PermissionCache;
import com.cnnic.whois.util.WhoisUtil;

public abstract class DbQueryDAO implements QueryDao {
	protected DataSource ds;
	List<QueryDao> queryDaos;
	protected PermissionCache permissionCache = PermissionCache
			.getPermissionCache();
	protected DomainIndexService domainIndexService = DomainIndexService
			.getIndexService();
	protected NameServerIndexService nameServerIndexService = NameServerIndexService
			.getIndexService();
	protected EntityIndexService entityIndexService = EntityIndexService
			.getIndexService();

	protected void handleField(String keyName, String role, String format,
			ResultSet results, Map<String, Object> map, String field)
			throws SQLException {
		Object resultsInfo;
		if (field.startsWith(WhoisUtil.ARRAYFILEDPRX)) {
			String key = field.substring(WhoisUtil.ARRAYFILEDPRX.length());
			resultsInfo = results.getString(key);
			String[] values = null;
			if (resultsInfo != null) {
				values = resultsInfo.toString().split(WhoisUtil.VALUEARRAYPRX);
			}
			map.put(WhoisUtil.getDisplayKeyName(key, format), values);
		} else if (field.startsWith(WhoisUtil.EXTENDPRX)) {
			resultsInfo = results.getString(field);
			map.put(field.substring(WhoisUtil.EXTENDPRX.length()), resultsInfo);
		} else if (field.startsWith(WhoisUtil.JOINFILEDPRX)) {
			String joinFieldIdColumnName = getJoinFieldIdColumnName();
			String joinFieldValue = results.getString(joinFieldIdColumnName);
			QueryJoinType queryJoinType = getQueryJoinType();
			queryJoinEntity(queryJoinType, joinFieldValue, keyName, role,
					format, map, field);
		} else {
			queryNormalField(format, results, map, field);
		}
	}

	public QueryJoinType getQueryJoinType() {
		return null;
	}

	protected void queryNormalField(String format, ResultSet results,
			Map<String, Object> map, String field) throws SQLException {
		Object resultsInfo;
		resultsInfo = results.getObject(field) == null ? "" : results
				.getObject(field);

		CharSequence id = "id";
		boolean fieldEndwithId = WhoisUtil.getDisplayKeyName(field, format)
				.substring(field.length() - 2).equals(id);
		if (fieldEndwithId && !format.equals("application/html")) {
			// continue;
		} else {
			map.put(WhoisUtil.getDisplayKeyName(field, format), resultsInfo);
		}
	}

	public void queryJoinEntity(QueryJoinType queryJoinType, String handle,
			String keyName, String role, String format,
			Map<String, Object> map, String field) throws SQLException {
		String key = field.substring(WhoisUtil.JOINFILEDPRX.length());
		for (QueryDao queryDao : queryDaos) {
			if (queryDao.supportJoinType(queryJoinType)) {
				Object value = queryDao.queryJoins(handle, role, format);
				if (value != null){
					map.put(key, value);
				}
				break;
			}
		}
	}

	protected Map<String, Object> rdapConformance(Map<String, Object> map) {
		if (map == null) {
			map = new LinkedHashMap<String, Object>();
		}
		Object[] conform = new Object[1];
		conform[0] = WhoisUtil.RDAPCONFORMANCE;
		map.put(WhoisUtil.RDAPCONFORMANCEKEY, conform);
		return map;
	}
}
