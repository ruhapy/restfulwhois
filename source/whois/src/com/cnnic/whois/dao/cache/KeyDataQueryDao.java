package com.cnnic.whois.dao.cache;

import java.util.List;
import java.util.Map;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;

public class KeyDataQueryDao extends AbstractCacheQueryDao {
	@Override
	protected List<String> getCacheKeySplits(QueryParam param) {
		return super.getHandleCacheKeySplits(param);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.KEYDATA;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.KEYDATA.equals(queryType);
	}

	@Override
	protected boolean needInitCache() {
		return true;
	}

	@Override
	protected void initCache() {
		try {
			Map<String, Object> valuesMap = dbQueryExecutor.getAll(
					QueryType.KEYDATA, "root");
			if (null == valuesMap) {
				return;
			}
			if (null == valuesMap.get("$mul$keyData")) {
				setCache(valuesMap);
				return;
			}
			Object[] values = (Object[]) valuesMap.get("$mul$keyData");
			for (Object entity : values) {
				Map<String, Object> entityMap = (Map<String, Object>) entity;
				setCache(entityMap);
			}
		} catch (QueryException e) {
			e.printStackTrace();
		}
	}

	private void setCache(Map<String, Object> entityMap) {
		String key = super.getCacheKey(new QueryParam(entityMap
				.get("keyDataID").toString()));
		super.setCache(key, entityMap);
	}
}