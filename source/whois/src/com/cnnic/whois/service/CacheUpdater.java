package com.cnnic.whois.service;

import java.util.Map;

import com.cnnic.whois.dao.CacheQueryDAO;
import com.cnnic.whois.dao.QueryDAO;
import com.cnnic.whois.execption.QueryException;

public class CacheUpdater {
	private static CacheUpdater cacheUpdater = new CacheUpdater();
	private CacheQueryDAO cache = CacheQueryDAO.getQueryDAO();
	private QueryDAO queryDao = QueryDAO.getQueryDAO();

	public static CacheUpdater getCacheUpdater() {
		return cacheUpdater;
	}

	public void init() {
		try {
			Map<String, Object> result = queryDao.queryDNRDoamin("z.cn",
					"root", "application/json");
			cache.setDomainMap("domain:z.cn", result);
		} catch (QueryException e) {
			e.printStackTrace();
		}
	}
}
