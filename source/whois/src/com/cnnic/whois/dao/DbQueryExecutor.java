package com.cnnic.whois.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.dao.queryV2.AbstractDbQueryDao;
import com.cnnic.whois.dao.queryV2.DnrDomainQueryDao;
import com.cnnic.whois.dao.queryV2.NsQueryDao;
import com.cnnic.whois.dao.queryV2.RirDomainQueryDao;
import com.cnnic.whois.execption.QueryException;

public class DbQueryExecutor {
	private static DbQueryExecutor executor = new DbQueryExecutor();

	public static DbQueryExecutor getExecutor() {
		return executor;
	}

	List<AbstractDbQueryDao> dbQueryDaos = new ArrayList<AbstractDbQueryDao>();

	private void init() {
		dbQueryDaos.add(new DnrDomainQueryDao(dbQueryDaos));
		dbQueryDaos.add(new RirDomainQueryDao(dbQueryDaos));
		dbQueryDaos.add(new NsQueryDao(dbQueryDaos));
	}

	public DbQueryExecutor() {
		super();
		init();
	}

	public Map<String, Object> query(QueryType queryType, String q,
			String role, String format) throws QueryException {
		for (AbstractDbQueryDao queryDao : dbQueryDaos) {
			if (queryDao.supportType(queryType)) {
				return queryDao.query(q, role, format);
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
