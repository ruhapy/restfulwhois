package com.cnnic.whois.dao.cache;

import java.util.List;
import java.util.Map;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;

public class EventsQueryDao extends AbstractCacheQueryDao {
	@Override
	protected List<String> getCacheKeySplits(QueryParam param) {
		return super.getHandleCacheKeySplits(param);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.EVENTS;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.EVENTS.equals(queryType);
	}

	@Override
	protected boolean needInitCache() {
		return true;
	}

	@Override
	protected void initCache() {
		try {
			Map<String, Object> valuesMap = dbQueryExecutor.getAll(
					QueryType.EVENTS, "root");
			if (null == valuesMap) {
				return;
			}
			if (null == valuesMap.get("$mul$events")) {
				setCache(valuesMap);
				return;
			}
			Object[] values = (Object[]) valuesMap.get("$mul$events");
			for (Object entity : values) {
				Map<String, Object> entityMap = (Map<String, Object>) entity;
				setCache(entityMap);
			}
		} catch (QueryException e) {
			e.printStackTrace();
		}
	}

	private void setCache(Map<String, Object> entityMap) {
		String key = super.getCacheKey(new QueryParam(entityMap.get("eventsId")
				.toString()));
		super.setCache(key, entityMap);
	}
}