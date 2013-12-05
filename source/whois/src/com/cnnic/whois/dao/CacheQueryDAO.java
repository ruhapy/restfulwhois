package com.cnnic.whois.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;

import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.WhoisProperties;
import com.cnnic.whois.util.WhoisUtil;

public class CacheQueryDAO {
	private Jedis cache = null;
	private static CacheQueryDAO queryDAO = new CacheQueryDAO();

	public static CacheQueryDAO getQueryDAO() {
		return queryDAO;
	}

	private void initCache() {
		cache = new Jedis(WhoisProperties.getCacheIp(),
				Integer.valueOf(WhoisProperties.getCachePort()));
	}

	public CacheQueryDAO() {
		super();
		initCache();
	}

	public static void main(String[] args) {
		Jedis redis = new Jedis("218.241.106.159", 6379);
		Map childMap = new HashMap();
		childMap.put("child", "childV");
		Map map = new HashMap();
		map.put("child", childMap);
		map.put("username", "asdf");
		redis.set("hash", JSONObject.fromObject(map).toString());
		JSONObject obj = JSONObject.fromObject(redis.get("hash"));
		System.err.println();
	}

	public JSONObject getMapAndConvertToJsonObject(String key) {
		String cacheObj = cache.get(key);
		if (StringUtils.isBlank(cacheObj)) {
			return null;
		}
		return JSONObject.fromObject(cacheObj);
	}

	public Map<String, Object> queryDoamin(String domainName, String role,
			String format) throws QueryException {
		Map<String, Object> map = null;
		Map<String, Object> domainMap = getMapAndConvertToJsonObject("domain:z.cn");
		if (domainMap != null) {
			map = rdapConformance(map);
			map.putAll(domainMap);
		}
		return map;
	}

	public void setDomainMap(String key, Map<String, Object> domainInfo) {
		cache.set(key, JSONObject.fromObject(domainInfo).toString());
	}

	private Map<String, Object> rdapConformance(Map<String, Object> map) {
		if (map == null) {
			map = new LinkedHashMap<String, Object>();
		}
		Object[] conform = new Object[1];
		conform[0] = WhoisUtil.RDAPCONFORMANCE;
		map.put(WhoisUtil.RDAPCONFORMANCEKEY, conform);
		return map;
	}
}