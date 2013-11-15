package com.cnnic.whois.dao.queryV2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.WhoisUtil;

public abstract class AbstractDomainQueryDao extends AbstractDbQueryDao {
	public AbstractDomainQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}

	private static final String QUERY_KEY = "$mul$domains";

	public Map<String, Object> query(String listSql, List<String> keyFields,
			String q, String role, String format) throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;

		try {
			connection = ds.getConnection();
			String selectSql = listSql + "'" + q + "'";
			Map<String, Object> domainMap = query(connection, selectSql,
					keyFields, "$mul$domains", role, format);
			if (domainMap != null) {
				map = rdapConformance(map);
				map.putAll(domainMap);
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

	/**
	 * Connect to the database query RIRDoamin information
	 * 
	 * @param queryInfo
	 * @param role
	 * @return map collection
	 * @throws QueryException
	 */
	public Map<String, Object> queryRIRDoamin(String queryInfo, String role,
			String format) throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;

		try {
			connection = ds.getConnection();
			String selectSql = WhoisUtil.SELECT_LIST_RIRDOMAIN + "'"
					+ queryInfo + "'";
			Map<String, Object> domainMap = query(connection, selectSql,
					permissionCache.getRIRDomainKeyFileds(role),
					"$mul$domains", role, format);
			if (domainMap != null) {
				map = rdapConformance(map);
				map.putAll(domainMap);
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

	// @Override
	// public boolean supportType(QueryType queryType) {
	// return QueryType.DOMAIN.equals(queryType);
	// }
	//
	// @Override
	// public String getJoinFieldIdColumnName() {
	// return WhoisUtil.HANDLE;
	// }
	//
	// @Override
	// public Map<String, Object> queryJoins(String handle, String role,
	// String format) {
	// throw new UnsupportedOperationException();
	// }
	//
	// @Override
	// public QueryType getQueryType() {
	// return QueryType.DOMAIN;
	// }
	//
	// @Override
	// public boolean supportJoinType(QueryType queryType,
	// QueryJoinType queryJoinType) {
	// return false;
	// }
}
