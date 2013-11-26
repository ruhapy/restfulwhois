package com.cnnic.whois.dao.cache;

import java.util.Map;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;

public class HelpQueryDao extends AbstractCacheQueryDao {
	@Override
	public Map<String, Object> query(QueryParam param, 
			PageBean... pageParam) throws QueryException, RedirectExecption {
		return dbQueryExecutor.query(QueryType.HELP, param, pageParam);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.HELP;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.HELP.equals(queryType);
	}

	@Override
	protected boolean needInitCache() {
		return false;
	}
	@Deprecated
	@Override
	protected void initCache() {
		super.initCacheWithOneKey("$mul$notices", "noticesId");
	}
}