package com.cnnic.whois.dao;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;
import com.cnnic.whois.permission.PermissionController;
import com.cnnic.whois.view.FormatType;
import com.cnnic.whois.view.ViewResolver;

@Service
public class QueryEngine {
	private static QueryEngine engine = new QueryEngine();
	@Autowired
	@Qualifier("cacheQueryExecutor")
	private QueryExecutor queryExecutor;
	// DbQueryExecutor.getExecutor();
	private ViewResolver viewResolver = ViewResolver.getResolver();
	private PermissionController permissionController = PermissionController
			.getPermissionController();

	public static QueryEngine getEngine() {
		return engine;
	}

	private void init() {
	}

	public QueryEngine() {
		super();
		init();
	}

	public Map<String, Object> query(QueryType queryType, QueryParam param)
			throws QueryException, RedirectExecption {
		Map<String, Object> result = queryExecutor.query(queryType, param);
//		result = permissionController.removeUnAuthedEntries(result, role);
//		result = viewResolver.format(result, FormatType.getFormatType(format));
		return result;
	}
}
