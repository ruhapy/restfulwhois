package com.cnnic.whois.dao.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.bean.index.EntityIndex;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.service.index.SearchResult;
import com.cnnic.whois.util.WhoisUtil;

public class FuzzyEntityQueryDao extends AbstractSearchQueryDao {

	public FuzzyEntityQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}

	@Override
	public Map<String, Object> query(QueryParam param, String role, String format,
			PageBean... page) throws QueryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Object> fuzzyQueryEntity(String fuzzyQueryParamName,
			String queryPara, String role, String format, PageBean page)
			throws QueryException, SQLException {
		SearchResult<EntityIndex> result = entityIndexService
				.fuzzyQueryEntitiesByHandleAndName(fuzzyQueryParamName,
						queryPara, page);
		String selectSql = WhoisUtil.SELECT_LIST_RIRENTITY;
		Connection connection = null;
		Map<String, Object> map = null;
		try {
			connection = ds.getConnection();
			Map<String, Object> entityMap = fuzzyQuery(connection, result,
					selectSql, "$mul$entity", role, format);
			if (entityMap != null) {
				map = rdapConformance(map);
				map.putAll(entityMap);
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
	protected Map<String, Object> postHandleFieldsFuzzy(String keyName,
			String format, Map<String, Object> map) {
		map = WhoisUtil.toVCard(map, format);
		return map;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.FUZZYENTITY;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.FUZZYENTITY.equals(queryType);
	}
}