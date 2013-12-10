package com.cnnic.whois.dao.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cnnic.whois.bean.QueryJoinType;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.ColumnCache;
import com.cnnic.whois.util.PermissionCache;
import com.cnnic.whois.util.WhoisUtil;

@Repository
public class NsQueryDao extends AbstractDbQueryDao {
	public static final String GET_ALL_NAMESREVER = "select * from nameServer ";

	public NsQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}

	public Map<String, Object> query(QueryParam param) throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;

		try {
			connection = ds.getConnection();

			String selectSql = WhoisUtil.SELECT_LIST_NAMESREVER + "'"
					+ param.getQ() + "'";
			Map<String, Object> nsMap = query(connection, selectSql,
					ColumnCache.getColumnCache().getNameServerKeyFileds(),
					"$mul$nameServer");
			if (nsMap != null) {
				map = rdapConformance(map);
				map.putAll(nsMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new QueryException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException se) {
				}
			}
		}
		return map;
	}

	@Override
	protected Map<String, Object> formatValue(Map<String, Object> map) {
		Map<String, Object> map_IP = new LinkedHashMap<String, Object>();
		Object IPAddressArray = map.get("IPV4_Addresses");
		map_IP.put(WhoisUtil.IPV4PREFIX, IPAddressArray);
		IPAddressArray = map.get("IPV6_Addresses");
		map_IP.put(WhoisUtil.IPV6PREFIX, IPAddressArray);
		map.put(WhoisUtil.IPPREFIX, map_IP);
		map.remove("IPV4_Addresses");
		map.remove("IPV6_Addresses");
		return map;
	}

	@Override
	public Map<String, Object> getAll() throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;
		try {
			connection = ds.getConnection();
			Map<String, Object> nsMap = query(connection, GET_ALL_NAMESREVER,
					ColumnCache.getColumnCache().getNameServerKeyFileds(),
					"$mul$nameServer");
			if (nsMap != null) {
				map = rdapConformance(map);
				map.putAll(nsMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new QueryException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException se) {
				}
			}
		}
		return map;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.NAMESERVER;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.NAMESERVER.equals(queryType);
	}

	@Override
	protected boolean supportJoinType(QueryType queryType,
			QueryJoinType queryJoinType) {
		return QueryJoinType.NAMESERVER.equals(queryJoinType);
	}

	@Override
	public Object querySpecificJoinTable(String key, String handle,
			Connection connection) throws SQLException {
		return querySpecificJoinTable(key, handle,
				WhoisUtil.SELECT_JOIN_LIST_JOINNAMESERVER, connection,
				ColumnCache.getColumnCache().getNameServerKeyFileds());
	}

	@Override
	public List<String> getKeyFields(String role) {
		List<String> cacheFields = PermissionCache.getPermissionCache()
				.getNameServerKeyFileds(role);
		List<String> result = new ArrayList<String>(cacheFields);
		result.add(WhoisUtil.IPPREFIX);
		result.add(WhoisUtil.IPV4PREFIX);
		result.add(WhoisUtil.IPV6PREFIX);
		return result;
	}
}