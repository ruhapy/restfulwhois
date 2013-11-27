package com.cnnic.whois.dao.cache;

import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;

public class RemarksQueryDao extends AbstractCacheQueryDao {
	@Override
	protected List<String> getCacheKeySplits(QueryParam param) {
		return super.getHandleCacheKeySplits(param);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.REMARKS;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.REMARKS.equals(queryType);
	}

	@Override
	protected boolean needInitCache() {
		return true;
	}

	@Override
	protected void initCache() {
		super.initCacheWithOneKey("$mul$remarks", "remarksId");
	}
}