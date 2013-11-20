package com.cnnic.whois.dao.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.QueryJoinType;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;

public abstract class AbstractDomainQueryDao extends AbstractDbQueryDao {
	public AbstractDomainQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}

	private static final String QUERY_KEY = "$mul$domains";

	public Map<String, Object> query(String listSql, List<String> keyFields,
			String q, String role, String format) throws QueryException {
		String sql = listSql + "'" + q + "'";
		return this.queryBySql(sql, keyFields, role, format);
	}

	protected Map<String, Object> queryBySql(String sql, List<String> keyFields,
			String role, String format) throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;

		try {
			connection = ds.getConnection();
			Map<String, Object> domainMap = query(connection, sql, keyFields,
					QUERY_KEY, role, format);
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