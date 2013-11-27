package com.cnnic.whois.dao.cache;

import java.util.List;

import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;

public class PhonesQueryDao extends AbstractCacheQueryDao {
	@Override
	protected List<String> getCacheKeySplits(QueryParam param) {
		return super.getHandleCacheKeySplits(param);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.PHONES;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.PHONES.equals(queryType);
	}

//	@Override
//	protected boolean needInitCache() {
//		return true;
//	}
//
//	@Override
//	protected void initCache() {
//		super.initCacheWithOneKey("$mul$phones", "phonesId");
//	}
}