package com.cnnic.whois.dao.query.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.dao.query.db.AbstractDbQueryDao;
import com.cnnic.whois.dao.query.db.DbQueryExecutor;
import com.cnnic.whois.dao.query.db.QueryDao;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;
import com.cnnic.whois.util.DataFormat;

/**
 * cache query dao,query data from redis,must execute /cache/init first to load data from db
 * @author nic
 *
 */
public abstract class AbstractCacheQueryDao implements QueryDao {
	@Autowired
	@Qualifier("dbQueryExecutor")
	protected DbQueryExecutor dbQueryExecutor;
	@Autowired
	private StringRedisTemplate redisTemplate;

	private ValueOperations<String, String> valueOps;

	@PostConstruct
	public void initRedisTpl() {
		valueOps = this.redisTemplate.opsForValue();
	}
	/**
	 * query
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> query(QueryParam param) throws QueryException,
			RedirectExecption {
		String cacheKey = getCacheKey(param);
		return getMapAndConvertToJsonObject(cacheKey);
	}
	/**
	 * get cache key
	 * @param param: query param
	 * @return cache key
	 */
	protected String getCacheKey(QueryParam param) {
		List<String> keySplits = getCacheKeySplits(param);
		return StringUtils.join(keySplits, ":");
	}
	/**
	 * get cache key splits
	 * @param param:query param
	 * @return splits
	 */
	protected List<String> getCacheKeySplits(QueryParam param) {
		throw new UnsupportedOperationException();
	}
	/**
	 * get handle key splits
	 * @param param:query param
	 * @return handle key splits
	 */
	protected List<String> getHandleCacheKeySplits(QueryParam param) {
		List<String> keySplits = new ArrayList<String>();
		keySplits.add(this.getQueryType().toString());
		keySplits.add("handle");
		keySplits.add(param.getQ());
		return keySplits;
	}
	/**
	 * GET MAP JSON OBJ
	 * @param key
	 * @return json obj
	 */
	private JSONObject getMapAndConvertToJsonObject(String key) {
		String cacheObj = valueOps.get(key);
		if (StringUtils.isBlank(cacheObj)) {
			return null;
		}
		return DataFormat.fromObject(cacheObj);
	}
	/**
	 * need init cache
	 * @return true if need,false if not
	 */
	protected boolean needInitCache() {
		return false;
	}
	/**
	 * init cache
	 */
	protected void initCache() {
		throw new UnsupportedOperationException();
	}
	/**
	 * init cache with key
	 * @param queryResultKey
	 * @param key
	 */
	protected void initCacheWithOneKey(String queryResultKey, String key) {
		initCacheWithOneKey(queryResultKey, key, getQueryType());
	}
	/**
	 * init cache with key
	 * @param queryResultKey
	 * @param key
	 * @param queryType
	 */
	protected void initCacheWithOneKey(String queryResultKey, String key,
			QueryType queryType) {
		try {
			Map<String, Object> valuesMap = dbQueryExecutor.getAll(queryType);
			if (null == valuesMap) {
				return;
			}
			if (null == valuesMap.get(queryResultKey)) {
				setCache(valuesMap, key);
				return;
			}
			Object[] values = (Object[]) valuesMap.get(queryResultKey);
			for (Object entity : values) {
				Map<String, Object> entityMap = (Map<String, Object>) entity;
				Map<String, Object> map = null;
				map = AbstractDbQueryDao.rdapConformance(map);
				map.putAll(entityMap);
				setCache(map, key);
			}
			System.err.println("init cache,add " + getQueryType() + " size:"
					+ values.length);
		} catch (QueryException e) {
			e.printStackTrace();
		}
	}
	/**
	 * convert cache key value
	 * @param value:key value
	 * @return value
	 */
	protected String convertCacheKeyValue(String value) {
		return value;
	}
	/**
	 * set obj to map
	 * @param entityMap
	 * @param key
	 */
	private void setCache(Map<String, Object> entityMap, String key) {
		String keyValue = convertCacheKeyValue(entityMap.get(key).toString());
		String cacheKey = getCacheKey(new QueryParam(keyValue));
		System.err.println("init cache,add " + getQueryType() + ",key:"
				+ cacheKey);
		setCache(cacheKey, entityMap);
	}

	@Override
	public Map<String, Object> getAll() throws QueryException {
		throw new UnsupportedOperationException();
	}
	/**
	 * set obj to cache 
	 * @param key
	 * @param entityMap
	 */
	protected void setCache(String key, Map<String, Object> entityMap) {
		String jsonStr = DataFormat.getJsonObject(entityMap).toString();
		System.err.println(jsonStr);
		valueOps.set(key, jsonStr);
	}
}