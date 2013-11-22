package com.cnnic.whois.dao.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.dao.db.DbQueryExecutor;
import com.cnnic.whois.dao.db.QueryDao;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;
import com.cnnic.whois.util.DataFormat;
import com.cnnic.whois.util.WhoisProperties;

public abstract class AbstractCacheQueryDao implements QueryDao {
	protected static DbQueryExecutor dbQueryExecutor = DbQueryExecutor
			.getExecutor();
	protected static Jedis cache = new Jedis(WhoisProperties.getCacheIp(),
			Integer.valueOf(WhoisProperties.getCachePort()));

	public AbstractCacheQueryDao() {
		super();
		init();
	}

	private void init() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> query(QueryParam param, String role,
			PageBean... page) throws QueryException, RedirectExecption {
		String cacheKey = getCacheKey(param);
		return getMapAndConvertToJsonObject(cacheKey);
	}

	protected String getCacheKey(QueryParam param) {
		List<String> keySplits = getCacheKeySplits(param);
		return StringUtils.join(keySplits, ":");
	}

	protected List<String> getCacheKeySplits(QueryParam param) {
		throw new UnsupportedOperationException();
	}
	
	protected List<String> getHandleCacheKeySplits(QueryParam param) {
		List<String> keySplits = new ArrayList<String>();
		keySplits.add(this.getQueryType().toString());
		keySplits.add("handle");
		keySplits.add(param.getQ());
		return keySplits;
	}

	private JSONObject getMapAndConvertToJsonObject(String key) {
		String cacheObj = cache.get(key);
		if (StringUtils.isBlank(cacheObj)) {
			return null;
		}
		return DataFormat.fromObject(cacheObj);
	}

	protected boolean needInitCache() {
		return false;
	}

	protected void initCache() {
		throw new UnsupportedOperationException();
	}

	protected void initCacheWithOneKey(String queryResultKey,String key) {
		try {
			Map<String, Object> valuesMap = dbQueryExecutor.getAll(
					this.getQueryType(), "root");
			if (null == valuesMap) {
				return;
			}
			if (null == valuesMap.get(queryResultKey)) {
				setCache(valuesMap,key);
				return;
			}
			Object[] values = (Object[]) valuesMap.get(queryResultKey);
			for (Object entity : values) {
				Map<String, Object> entityMap = (Map<String, Object>) entity;
				setCache(entityMap,key);
			}
		} catch (QueryException e) {
			e.printStackTrace();
		}
	}

	private void setCache(Map<String, Object> entityMap,String key) {
		String cacheKey = getCacheKey(new QueryParam(entityMap
				.get(key).toString()));
		setCache(cacheKey, entityMap);
	}
	@Override
	public Map<String, Object> getAll(String role) throws QueryException {
		throw new UnsupportedOperationException();
	}

	protected void setCache(String key, Map<String, Object> entityMap) {
		String jsonStr = DataFormat.getJsonObject(entityMap).toString();
		cache.set(key, jsonStr);
	}
}