package com.cnnic.whois.dao.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.bean.index.Index;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.service.index.SearchResult;

@Repository
public class SearchDomainQueryDao extends AbstractSearchQueryDao {

	public SearchDomainQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}

	@Override
	public Map<String, Object> query(QueryParam param) throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;
		PageBean page = param.getPage();
		SearchResult<? extends Index> result = searchQueryExecutor.query(
				QueryType.DOMAIN, param);
		if (result.getResultList().size() == 0) {
			return map;
		}
		try {
			connection = ds.getConnection();
			Map<String, Object> domainMap = super.fuzzyQuery(connection,
					result, "$mul$domains");
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

	@Override
	public QueryType getQueryType() {
		return QueryType.SEARCHDOMAIN;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.SEARCHDOMAIN.equals(queryType);
	}
}