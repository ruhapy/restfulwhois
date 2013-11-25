package com.cnnic.whois.dao.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;

public class EntityQueryDao extends AbstractCacheQueryDao {
	@Override
	protected List<String> getCacheKeySplits(QueryParam param) {
		List<String> keySplits = new ArrayList<String>();
		keySplits.add(QueryType.ENTITY.toString());
		keySplits.add("handle");
		keySplits.add(param.getQ());
		return keySplits;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.ENTITY;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.ENTITY.equals(queryType);
	}

	@Override
	protected boolean needInitCache() {
		return true;
	}

	@Override
	protected void initCache() {
		try {
			Map<String, Object> valuesMap = dbQueryExecutor.getAll(
					QueryType.ENTITY, "root");
			if (null == valuesMap) {
				return;
			}
			if (null == valuesMap.get("$mul$entity")) {
				setCache(valuesMap);
				return;
			}
			Object[] values = (Object[]) valuesMap.get("$mul$entity");
			for (Object entity : values) {
				Map<String, Object> entityMap = (Map<String, Object>) entity;
				setCache(entityMap);
			}
			System.err.println("init cache,add entities size:"+values.length);
		} catch (QueryException e) {
			e.printStackTrace();
		}
	}

	private void setCache(Map<String, Object> entityMap) {
		String key = super.getCacheKey(new QueryParam(entityMap.get("Handle")
				.toString()));
		System.err.println("init cache,add entity,key:"+key);
		super.setCache(key, entityMap);
	}
}