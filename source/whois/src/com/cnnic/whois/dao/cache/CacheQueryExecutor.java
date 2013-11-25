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
		// cacheQueryDaos.add(new AsQueryDao());
		// cacheQueryDaos.add(new DelegationKeysQueryDao());
		cacheQueryDaos.add(new DnrDomainQueryDao());
		// cacheQueryDaos.add(new DnrEntityQueryDao());
		// cacheQueryDaos.add(new DsDataQueryDao());
		cacheQueryDaos.add(new EntityQueryDao());
		// cacheQueryDaos.add(new ErrorMsgQueryDao());
		// cacheQueryDaos.add(new EventsQueryDao());
		// cacheQueryDaos.add(new SearchDomainQueryDao());
		// cacheQueryDaos.add(new SearchEntityQueryDao());
		// cacheQueryDaos.add(new HelpQueryDao());
		// cacheQueryDaos.add(new IpQueryDao());// TODO:
		// cacheQueryDaos.add(new IpRedirectionQueryDao());
		// cacheQueryDaos.add(new KeyDataQueryDao());
		// cacheQueryDaos.add(new LinksQueryDao());
		// cacheQueryDaos.add(new NoticesQueryDao());
		 cacheQueryDaos.add(new NsQueryDao());
		// cacheQueryDaos.add(new PhonesQueryDao());
		// cacheQueryDaos.add(new PostalAddressQueryDao());
		// cacheQueryDaos.add(new PublicIdsQueryDao());
		// cacheQueryDaos.add(new RefirectionQueryDao());
		// cacheQueryDaos.add(new RegistrarQueryDao());
		// cacheQueryDaos.add(new RemarksQueryDao());
		cacheQueryDaos.add(new RirDomainQueryDao());
		// cacheQueryDaos.add(new RirEntityQueryDao());
		 cacheQueryDaos.add(new SecureDnsQueryDao());
		// cacheQueryDaos.add(new VariantsQueryDao());
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