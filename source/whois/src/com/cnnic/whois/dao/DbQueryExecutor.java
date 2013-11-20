package com.cnnic.whois.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.dao.query.AbstractDbQueryDao;
import com.cnnic.whois.dao.query.AsQueryDao;
import com.cnnic.whois.dao.query.DelegationKeysQueryDao;
import com.cnnic.whois.dao.query.DnrDomainQueryDao;
import com.cnnic.whois.dao.query.DnrEntityQueryDao;
import com.cnnic.whois.dao.query.DsDataQueryDao;
import com.cnnic.whois.dao.query.EntityQueryDao;
import com.cnnic.whois.dao.query.ErrorMsgQueryDao;
import com.cnnic.whois.dao.query.EventsQueryDao;
import com.cnnic.whois.dao.query.SearchDomainQueryDao;
import com.cnnic.whois.dao.query.SearchEntityQueryDao;
import com.cnnic.whois.dao.query.HelpQueryDao;
import com.cnnic.whois.dao.query.IpQueryDao;
import com.cnnic.whois.dao.query.IpRedirectionQueryDao;
import com.cnnic.whois.dao.query.KeyDataQueryDao;
import com.cnnic.whois.dao.query.LinksQueryDao;
import com.cnnic.whois.dao.query.NoticesQueryDao;
import com.cnnic.whois.dao.query.NsQueryDao;
import com.cnnic.whois.dao.query.PhonesQueryDao;
import com.cnnic.whois.dao.query.PostalAddressQueryDao;
import com.cnnic.whois.dao.query.PublicIdsQueryDao;
import com.cnnic.whois.dao.query.RefirectionQueryDao;
import com.cnnic.whois.dao.query.RegistrarQueryDao;
import com.cnnic.whois.dao.query.RemarksQueryDao;
import com.cnnic.whois.dao.query.RirDomainQueryDao;
import com.cnnic.whois.dao.query.RirEntityQueryDao;
import com.cnnic.whois.dao.query.SecureDnsQueryDao;
import com.cnnic.whois.dao.query.VariantsQueryDao;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;

public class DbQueryExecutor implements QueryExecutor {
	private static DbQueryExecutor executor = new DbQueryExecutor();

	public static DbQueryExecutor getExecutor() {
		return executor;
	}

	private List<AbstractDbQueryDao> dbQueryDaos = new ArrayList<AbstractDbQueryDao>();

	private void init() {
		dbQueryDaos.add(new AsQueryDao(dbQueryDaos));
		dbQueryDaos.add(new DelegationKeysQueryDao(dbQueryDaos));
		dbQueryDaos.add(new DnrDomainQueryDao(dbQueryDaos));
		dbQueryDaos.add(new DnrEntityQueryDao(dbQueryDaos));
		dbQueryDaos.add(new DsDataQueryDao(dbQueryDaos));
		dbQueryDaos.add(new EntityQueryDao(dbQueryDaos));
		dbQueryDaos.add(new ErrorMsgQueryDao(dbQueryDaos));
		dbQueryDaos.add(new EventsQueryDao(dbQueryDaos));
		dbQueryDaos.add(new SearchDomainQueryDao(dbQueryDaos));
		dbQueryDaos.add(new SearchEntityQueryDao(dbQueryDaos));
		dbQueryDaos.add(new HelpQueryDao(dbQueryDaos));
		dbQueryDaos.add(new IpQueryDao(dbQueryDaos));// TODO:
		dbQueryDaos.add(new IpRedirectionQueryDao(dbQueryDaos));
		dbQueryDaos.add(new KeyDataQueryDao(dbQueryDaos));
		dbQueryDaos.add(new LinksQueryDao(dbQueryDaos));
		dbQueryDaos.add(new NoticesQueryDao(dbQueryDaos));
		dbQueryDaos.add(new NsQueryDao(dbQueryDaos));
		dbQueryDaos.add(new PhonesQueryDao(dbQueryDaos));
		dbQueryDaos.add(new PostalAddressQueryDao(dbQueryDaos));
		dbQueryDaos.add(new PublicIdsQueryDao(dbQueryDaos));
		dbQueryDaos.add(new RefirectionQueryDao(dbQueryDaos));
		dbQueryDaos.add(new RegistrarQueryDao(dbQueryDaos));
		dbQueryDaos.add(new RemarksQueryDao(dbQueryDaos));
		dbQueryDaos.add(new RirDomainQueryDao(dbQueryDaos));
		dbQueryDaos.add(new RirEntityQueryDao(dbQueryDaos));
		dbQueryDaos.add(new SecureDnsQueryDao(dbQueryDaos));
		dbQueryDaos.add(new VariantsQueryDao(dbQueryDaos));
	}

	public DbQueryExecutor() {
		super();
		init();
	}

	@Override
	public Map<String, Object> query(QueryType queryType, QueryParam param,
			String role, String format,PageBean... pageParam) throws QueryException,
			RedirectExecption {
		for (AbstractDbQueryDao queryDao : dbQueryDaos) {
			if (queryDao.supportType(queryType)) {
				return queryDao.query(param, role, format,pageParam);
			}
		}
		return null;
	}

	public List<AbstractDbQueryDao> getDbQueryDaos() {
		return dbQueryDaos;
	}

	public void setDbQueryDaos(List<AbstractDbQueryDao> dbQueryDaos) {
		this.dbQueryDaos = dbQueryDaos;
	}
}