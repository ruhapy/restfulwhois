package com.cnnic.whois.dao.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.dao.QueryExecutor;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;

public class CacheQueryExecutor implements QueryExecutor {
	private static CacheQueryExecutor executor = new CacheQueryExecutor();

	public static CacheQueryExecutor getExecutor() {
		return executor;
	}

	private List<AbstractCacheQueryDao> cacheQueryDaos = new ArrayList<AbstractCacheQueryDao>();

	private void init() {
		// dbQueryDaos.add(new AsQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new DelegationKeysQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new DnrDomainQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new DnrEntityQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new DsDataQueryDao(dbQueryDaos));
		cacheQueryDaos.add(new EntityQueryDao());
		cacheQueryDaos.add(new DomainQueryDao());
		// dbQueryDaos.add(new ErrorMsgQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new EventsQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new SearchDomainQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new SearchEntityQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new HelpQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new IpQueryDao(dbQueryDaos));// TODO:
		// dbQueryDaos.add(new IpRedirectionQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new KeyDataQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new LinksQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new NoticesQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new NsQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new PhonesQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new PostalAddressQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new PublicIdsQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new RefirectionQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new RegistrarQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new RemarksQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new RirDomainQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new RirEntityQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new SecureDnsQueryDao(dbQueryDaos));
		// dbQueryDaos.add(new VariantsQueryDao(dbQueryDaos));
	}

	public CacheQueryExecutor() {
		super();
		init();
	}

	@Override
	public Map<String, Object> query(QueryType queryType, QueryParam param,
			String role, PageBean... pageParam) throws QueryException,
			RedirectExecption {
		for (AbstractCacheQueryDao queryDao : cacheQueryDaos) {
			if (queryDao.supportType(queryType)) {
				return queryDao.query(param, role, pageParam);
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