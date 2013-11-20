package com.cnnic.whois.dao;

import java.util.Map;
import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;

public class QueryEngine {
	private static QueryEngine engine = new QueryEngine();
	private static QueryExecutor queryExecutor = DbQueryExecutor.getExecutor();

	public static QueryEngine getEngine() {
		return engine;
	}

	private void init() {
	}

	public QueryEngine() {
		super();
		init();
	}

	public Map<String, Object> query(QueryType queryType, QueryParam param,
			String role, String format,PageBean... pageParam) throws QueryException,
			RedirectExecption {
		return queryExecutor.query(queryType, param, role, format,pageParam);
	}
}
