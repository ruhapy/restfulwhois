package com.cnnic.whois.dao.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryJoinType;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;

public abstract class AbstractDomainQueryDao extends AbstractDbQueryDao {
	public static final String GET_ALL_DNRDOMAIN = "select * from DNRDomain where ldh_name='z.cn'";
	public static final String GET_ALL_RIRDOMAIN = "select * from RIRDomain";
	public static final String QUERY_KEY = "$mul$domains";

	public AbstractDomainQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}

	@Override
	public Map<String, Object> query(QueryParam param, String role,
			PageBean... page) throws QueryException, RedirectExecption {
		throw new UnsupportedOperationException();
	}

	public Map<String, Object> query(String listSql, List<String> keyFields,
			String q, String role) throws QueryException {
		String sql = listSql + "'" + q + "'";
		return this.queryBySql(sql, keyFields, role);
	}

	protected Map<String, Object> queryBySql(String sql,
			List<String> keyFields, String role) throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;

		try {
			connection = ds.getConnection();
			Map<String, Object> domainMap = query(connection, sql, keyFields,
					QUERY_KEY, role);
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
	public QueryType getQueryType() {
		return QueryType.DOMAIN;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.DOMAIN.equals(queryType);
	}

	@Override
	protected boolean supportJoinType(QueryType queryType,
			QueryJoinType queryJoinType) {
		return false;
	}

	@Override
	public Object querySpecificJoinTable(String key, String handle,
			String role, Connection connection) throws SQLException {
		throw new UnsupportedOperationException();
	}
}