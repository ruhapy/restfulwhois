package com.cnnic.whois.dao.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.QueryJoinType;
import com.cnnic.whois.bean.QueryType;
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

	@Override
	protected boolean supportJoinType(QueryType queryType,
			QueryJoinType queryJoinType) {
		return false;
	}

	@Override
	public Object querySpecificJoinTable(String key, String handle,
			String role, Connection connection, String format)
			throws SQLException {
		throw new UnsupportedOperationException();
	}

}
