package com.cnnic.whois.dao.cache;

import java.util.Map;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;

public class SearchNsQueryDao extends AbstractCacheQueryDao {

	@Override
	public Map<String, Object> query(QueryParam param, String role,
			PageBean... pageParam) throws QueryException, RedirectExecption {
		return dbQueryExecutor
				.query(QueryType.SEARCHNS, param, role, pageParam);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.SEARCHNS;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.SEARCHNS.equals(queryType);
	}
}