package com.cnnic.whois.dao.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryJoinType;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.bean.index.DomainIndex;
import com.cnnic.whois.bean.index.Index;
import com.cnnic.whois.dao.search.SearchQueryExecutor;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;
import com.cnnic.whois.service.index.SearchResult;
import com.cnnic.whois.util.WhoisUtil;

public class DomainQueryDao extends AbstractSearchQueryDao {
	SearchQueryExecutor searchQueryExecutor = SearchQueryExecutor.getExecutor();
	public static final String GET_ALL_DNRDOMAIN = "select * from DNRDomain";
	public static final String GET_ALL_RIRDOMAIN = "select * from RIRDomain";
	public static final String QUERY_KEY = "$mul$domains";

	public DomainQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}

	@Override
	public Map<String, Object> query(QueryParam param, String role,
			PageBean... page) throws QueryException, RedirectExecption {
		if (param.isFuzzyQ()) {
			return doFuzzyQuery(param, role, page);
		} else {
			return doQuery(param, role, page);
		}
	}

	private Map<String, Object> doFuzzyQuery(QueryParam param, String role,
			PageBean... pageParam) throws QueryException {
		SearchResult<? extends Index> result = searchQueryExecutor.query(
				QueryType.DOMAIN, param, pageParam);
		return queryWithIndexs(role, result);
	}

	private Map<String, Object> queryWithIndexs(String role,
			SearchResult<? extends Index> result) throws QueryException {
		Map<String, Object> map = null;
		if (result.getResultList().size() == 0) {
			return map;
		}
		Connection connection = null;
		try {
			connection = ds.getConnection();
			String sql = "to delete";// TODO:delete
			Map<String, Object> domainMap = super.fuzzyQuery(connection,
					result, sql, "$mul$domains", role);
			if (domainMap != null) {
				map = rdapConformance(map);
				map.putAll(domainMap);
				addTruncatedParamToMap(map, result);
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

	private Map<String, Object> doQuery(QueryParam param, String role,
			PageBean... page) throws QueryException {
		Map<String, Object> map = null;
		List<String> dnrKeyFields = permissionCache.getDNRDomainKeyFileds(role);
		Map<String, Object> dnrMap = query(param, role, dnrKeyFields, page);
		map.putAll(dnrMap);
		List<String> rirKeyFields = permissionCache.getDNRDomainKeyFileds(role);
		Map<String, Object> rirMap = query(param, role, rirKeyFields, page);
		map.putAll(rirMap);
		return map;
	}

	private Map<String, Object> query(QueryParam param, String role,
			List<String> keyFields, PageBean... page) throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;
		try {
			connection = ds.getConnection();
			Map<String, Object> domainMap = query(
					WhoisUtil.SELECT_LIST_DNRDOMAIN, keyFields, param.getQ(),
					role);
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

	protected Map<String, Object> query(String listSql, List<String> keyFields,
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
	public Map<String, Object> getAll(String role) throws QueryException {
		List<String> dnrKeyFields = permissionCache.getDNRDomainKeyFileds(role);
		Map<String, Object> dnrDomains = queryBySql(GET_ALL_DNRDOMAIN,
				dnrKeyFields, role);
		List<String> rirKeyFields = permissionCache.getRIRDomainKeyFileds(role);
		Map<String, Object> rirDomains = queryBySql(GET_ALL_RIRDOMAIN,
				rirKeyFields, role);
		Map<String, Object> result = new HashMap<String, Object>();
		// TODO:handle size 1
		result.putAll(dnrDomains);
		result.putAll(rirDomains);
		return result;
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