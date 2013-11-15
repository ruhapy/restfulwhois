package com.cnnic.whois.dao.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryJoinType;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.WhoisUtil;

public class NsQueryDAO extends AbstractDbQueryDAO {
	public NsQueryDAO(List<AbstractDbQueryDAO> dbQueryDaos) {
		super(dbQueryDaos);
	}

	private static final String QUERY_KEY = "$mul$nameServer";

	@Override
	public Map<String, Object> query(String q, String role, String format,
			PageBean... page) throws QueryException {
		PreparedStatement stmt = null;
		ResultSet results = null;
		Connection connection = null;
		try {
			connection = ds.getConnection();
			String sql = WhoisUtil.SELECT_LIST_NAMESREVER + "'" + q + "'";
			stmt = connection.prepareStatement(sql);
			results = stmt.executeQuery();
			List<String> keyFlieds = permissionCache
					.getNameServerKeyFileds(role);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			while (results.next()) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				for (int i = 0; i < keyFlieds.size(); i++) {
					String field = keyFlieds.get(i);
					handleField(role, format, results, map, field);
				}
				handleIp(format, map);
				list.add(map);
			}
			if (list.size() == 0) {
				return null;
			}
			Map<String, Object> domainMap = new LinkedHashMap<String, Object>();
			if (list.size() > 1) {
				domainMap.put(QUERY_KEY, list.toArray());
			} else {
				domainMap = list.get(0);
			}
			Map<String, Object> resultMap = null;
			if (domainMap != null) {
				resultMap = rdapConformance(resultMap);
				resultMap.putAll(domainMap);
			}
			return resultMap;
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

	private void handleIp(String format, Map<String, Object> map) {
		Map<String, Object> map_IP = new LinkedHashMap<String, Object>();
		Object IPAddressArray = map.get(WhoisUtil.getDisplayKeyName(
				"IPV4_Addresses", format));
		map_IP.put(WhoisUtil.IPV4PREFIX, IPAddressArray);
		IPAddressArray = map.get(WhoisUtil.getDisplayKeyName("IPV6_Addresses",
				format));
		map_IP.put(WhoisUtil.IPV6PREFIX, IPAddressArray);
		map.put(WhoisUtil.IPPREFIX, map_IP);
		map.remove(WhoisUtil.getDisplayKeyName("IPV4_Addresses", format));
		map.remove(WhoisUtil.getDisplayKeyName("IPV6_Addresses", format));
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.NAMESERVER.equals(queryType);
	}

	@Override
	public String getJoinFieldIdColumnName() {
		return WhoisUtil.HANDLE;
	}

	@Override
	public Object queryJoins(String handle, String role, String format) {
		String sql = WhoisUtil.SELECT_JOIN_LIST_JOINNAMESERVER + "'" + handle
				+ "'";
		PreparedStatement stmt = null;
		ResultSet results = null;
		Connection connection = null;
		try {
			connection = ds.getConnection();
			List<String> keyFlieds = permissionCache
					.getDNRDomainKeyFileds(role);
			stmt = connection.prepareStatement(sql);
			results = stmt.executeQuery();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			while (results.next()) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				for (int i = 0; i < keyFlieds.size(); i++) {
					String field = keyFlieds.get(i);
					handleField(role, format, results, map, field);
				}
				handleIp(format, map);
				list.add(map);
			}
			if (list.size() == 0) {
				return null;
			}
			if (list.size() > 1) {
				return list.toArray();
			} else {
				return list.get(0);
			}
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

	@Override
	public QueryType getQueryType() {
		return QueryType.NAMESERVER;
	}

	@Override
	public boolean supportJoinType(QueryType queryType,
			QueryJoinType queryJoinType) {
		return QueryJoinType.NAMESERVER.equals(queryJoinType);
	}
}
