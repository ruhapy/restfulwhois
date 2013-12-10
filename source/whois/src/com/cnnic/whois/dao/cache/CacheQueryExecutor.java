package com.cnnic.whois.dao.cache;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.dao.QueryExecutor;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;

@Service("cacheQueryExecutor")
public class CacheQueryExecutor implements QueryExecutor {
	private static CacheQueryExecutor executor = new CacheQueryExecutor();

	public static CacheQueryExecutor getExecutor() {
		return executor;
	}

	@Autowired
	private List<AbstractCacheQueryDao> cacheQueryDaos;

	@Override
	public Map<String, Object> query(QueryType queryType, QueryParam param,
			PageBean... pageParam) throws QueryException, RedirectExecption {
		for (AbstractCacheQueryDao queryDao : cacheQueryDaos) {
			if (queryDao.supportType(queryType)) {
				return queryDao.query(param);
			}
		}
		return null;
	}

	public Map<String, Object> initCache() {
		for (AbstractCacheQueryDao queryDao : cacheQueryDaos) {
			if (queryDao.needInitCache()) {
				queryDao.initCache();
			}
		}
		return null;
	}

	public List<AbstractCacheQueryDao> getDbQueryDaos() {
		return cacheQueryDaos;
	}

	public void setDbQueryDaos(List<AbstractCacheQueryDao> cacheQueryDaos) {
		this.cacheQueryDaos = cacheQueryDaos;
	}
}