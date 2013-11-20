package com.cnnic.whois.service;

import com.cnnic.whois.dao.CacheQueryDAO;
import com.cnnic.whois.dao.DbQueryExecutor;
import com.cnnic.whois.dao.QueryDAO;
import com.cnnic.whois.dao.QueryExecutor;
import com.cnnic.whois.dao.cache.CacheQueryExecutor;

public class CacheUpdater {
	private static CacheUpdater cacheUpdater = new CacheUpdater();
	private CacheQueryDAO cache = CacheQueryDAO.getQueryDAO();
	private QueryDAO queryDao = QueryDAO.getQueryDAO();
	private static QueryExecutor dbQueryExecutor = DbQueryExecutor.getExecutor();
	
	public static CacheUpdater getCacheUpdater() {
		return cacheUpdater;
	}

	public void init() {
		CacheQueryExecutor cacheQueryExecutor = CacheQueryExecutor.getExecutor();
		cacheQueryExecutor.initCache();
//		try {
//			Map<String, Object> result = queryDao.queryDNRDoamin("z.cn",
//					"root", "application/json");
//			cache.setDomainMap("domain:z.cn", result);
//		} catch (QueryException e) {
//			e.printStackTrace();
//		}
	}
}
