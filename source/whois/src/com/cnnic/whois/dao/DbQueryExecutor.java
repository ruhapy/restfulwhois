package com.cnnic.whois.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.dao.query.DomainQueryDAO;
import com.cnnic.whois.dao.query.QueryDao;

public class DbQueryExecutor {

	List<QueryDao> queryDaos = new ArrayList<QueryDao>();

	private void init() {
		queryDaos.add(new DomainQueryDAO());
	}

	public DbQueryExecutor() {
		super();
	}

	private Map<String, Object> query(QueryType queryType, String role,
			String format) {
		for (QueryDao queryDao : queryDaos) {
			if (queryDao.supportType(queryType)) {
				queryDao.query("q",role, format);
				break;
			}
		}
		return null;
	}
}
