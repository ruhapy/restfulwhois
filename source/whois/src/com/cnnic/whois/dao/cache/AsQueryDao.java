package com.cnnic.whois.dao.cache;

import java.util.Map;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;

public class AsQueryDao extends AbstractCacheQueryDao {

	@Override
	public Map<String, Object> query(QueryParam param, 
			PageBean... pageParam) throws QueryException, RedirectExecption {
		return dbQueryExecutor.query(QueryType.AUTNUM, param, pageParam);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.AUTNUM;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.AUTNUM.equals(queryType);
	}
}