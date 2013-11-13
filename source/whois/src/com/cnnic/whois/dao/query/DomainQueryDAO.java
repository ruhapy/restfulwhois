package com.cnnic.whois.dao.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.cnnic.whois.bean.QueryJoinType;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.util.PermissionCache;
import com.cnnic.whois.util.WhoisUtil;

public class DomainQueryDAO extends DbQueryDAO {
	protected DataSource ds;
	protected PermissionCache permissionCache = PermissionCache
			.getPermissionCache();

	public Map<String, Object> query(String q, String role, String format) {
		PreparedStatement stmt = null;
		ResultSet results = null;
		Connection connection = null;
		try {
			connection = ds.getConnection();
			String selectSql = WhoisUtil.SELECT_LIST_DNRDOMAIN + "'" + q + "'";
			List<String> keyFlieds = permissionCache
					.getDNRDomainKeyFileds(role);
			String keyName = "$mul$domains";
			stmt = connection.prepareStatement(selectSql);
			results = stmt.executeQuery();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			while (results.next()) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				for (int i = 0; i < keyFlieds.size(); i++) {
					String field = keyFlieds.get(i);
					handleField(keyName, role, format, results, map, field);
				}
				list.add(map);
			}
			if (list.size() == 0)
				return null;
			Map<String, Object> mapInfo = new LinkedHashMap<String, Object>();
			if (list.size() > 1) {
				mapInfo.put(keyName, list.toArray());
			} else {
				mapInfo = list.get(0);
			}
			return mapInfo;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (results != null) {
				try {
					results.close();
				} catch (SQLException se) {
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException se) {
				}
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

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.DOMAIN.equals(queryType);
	}

	@Override
	public boolean supportJoinType(QueryJoinType queryJoinType) {
		return false;
	}

	@Override
	public String getJoinFieldIdColumnName() {
		throw new UnsupportedOperationException();//TODO: superClass
	}

	@Override
	public Map<String, Object> queryJoins(String handle, String role,
			String format) {
		throw new UnsupportedOperationException();
	}
}
