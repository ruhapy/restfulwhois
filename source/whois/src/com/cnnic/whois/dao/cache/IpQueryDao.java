package com.cnnic.whois.dao.cache;

import java.util.Map;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;

public class IpQueryDao extends AbstractCacheQueryDao {

	@Override
	public Map<String, Object> query(QueryParam param, String role,
			PageBean... pageParam) throws QueryException, RedirectExecption {
		return dbQueryExecutor.query(QueryType.IP, param, role, pageParam);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.IP;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.IP.equals(queryType);
	}
}