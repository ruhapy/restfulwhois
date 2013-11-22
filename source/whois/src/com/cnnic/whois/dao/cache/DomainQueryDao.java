package com.cnnic.whois.dao.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.dao.db.AbstractDomainQueryDao;
import com.cnnic.whois.execption.QueryException;

public class DomainQueryDao extends AbstractCacheQueryDao {
	@Override
	protected List<String> getCacheKeySplits(QueryParam param) {
		List<String> keySplits = new ArrayList<String>();
		keySplits.add(QueryType.DOMAIN.toString());
		keySplits.add("handle");
		keySplits.add(param.getQ());
		return keySplits;
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
	protected boolean needInitCache() {
		return true;
	}

	@Override
	protected void initCache() {
		try {
			Map<String, Object> valuesMap = dbQueryExecutor.getAll(
					QueryType.DOMAIN, "root");
			if (null == valuesMap) {
				return;
			}
			if (null == valuesMap.get(AbstractDomainQueryDao.QUERY_KEY)) {
				setCache(valuesMap);
				return;
			}
			Object[] values = (Object[]) valuesMap
					.get(AbstractDomainQueryDao.QUERY_KEY);
			for (Object entity : values) {
				Map<String, Object> entityMap = (Map<String, Object>) entity;
				setCache(entityMap);
			}
		} catch (QueryException e) {
			e.printStackTrace();
		}
	}

	private void setCache(Map<String, Object> entityMap) {
		String key = super.getCacheKey(new QueryParam(entityMap.get("handle")
				.toString()));
		super.setCache(key, entityMap);
	}
}