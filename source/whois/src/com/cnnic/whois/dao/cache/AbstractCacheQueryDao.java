package com.cnnic.whois.dao.cache;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
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

	@Override
	public Map<String, Object> getAll(String role) throws QueryException {
		throw new UnsupportedOperationException();
	}

	protected void setCache(String key, Map<String, Object> entityMap) {
		String jsonStr = DataFormat.getJsonObject(entityMap).toString();
		cache.set(key, jsonStr);
	}
}