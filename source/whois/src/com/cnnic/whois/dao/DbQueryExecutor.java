package com.cnnic.whois.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.dao.queryV2.AbstractDbQueryDao;
import com.cnnic.whois.dao.queryV2.AsQueryDao;
import com.cnnic.whois.dao.queryV2.DelegationKeysQueryDao;
import com.cnnic.whois.dao.queryV2.DnrDomainQueryDao;
import com.cnnic.whois.dao.queryV2.DnrEntityQueryDao;
import com.cnnic.whois.dao.queryV2.DsDataQueryDao;
import com.cnnic.whois.dao.queryV2.EntityQueryDao;
import com.cnnic.whois.dao.queryV2.ErrorMsgQueryDao;
import com.cnnic.whois.dao.queryV2.EventsQueryDao;
import com.cnnic.whois.dao.queryV2.FuzzyDomainQueryDao;
import com.cnnic.whois.dao.queryV2.FuzzyEntityQueryDao;
import com.cnnic.whois.dao.queryV2.HelpQueryDao;
import com.cnnic.whois.dao.queryV2.IpQueryDao;
import com.cnnic.whois.dao.queryV2.IpRedirectionQueryDao;
import com.cnnic.whois.dao.queryV2.KeyDataQueryDao;
import com.cnnic.whois.dao.queryV2.LinksQueryDao;
import com.cnnic.whois.dao.queryV2.NoticesQueryDao;
import com.cnnic.whois.dao.queryV2.NsQueryDao;
import com.cnnic.whois.dao.queryV2.PhonesQueryDao;
import com.cnnic.whois.dao.queryV2.PostalAddressQueryDao;
import com.cnnic.whois.dao.queryV2.PublicIdsQueryDao;
import com.cnnic.whois.dao.queryV2.RefirectionQueryDao;
import com.cnnic.whois.dao.queryV2.RegistrarQueryDao;
import com.cnnic.whois.dao.queryV2.RemarksQueryDao;
import com.cnnic.whois.dao.queryV2.RirDomainQueryDao;
import com.cnnic.whois.dao.queryV2.RirEntityQueryDao;
import com.cnnic.whois.dao.queryV2.SecureDnsQueryDao;
import com.cnnic.whois.dao.queryV2.VariantsQueryDao;
import com.cnnic.whois.execption.QueryException;

public class DbQueryExecutor {
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
		dbQueryDaos.add(new FuzzyDomainQueryDao(dbQueryDaos));
		dbQueryDaos.add(new FuzzyEntityQueryDao(dbQueryDaos));
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

	public Map<String, Object> query(QueryType queryType, QueryParam param,
			String role, String format) throws QueryException {
		for (AbstractDbQueryDao queryDao : dbQueryDaos) {
			if (queryDao.supportType(queryType)) {
				return queryDao.query(param, role, format);
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