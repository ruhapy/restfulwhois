package com.cnnic.whois.dao.query;

import java.util.Map;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;

public interface QueryExecutor {
	/**
	 * query main method
	 * @param queryType
	 * @param param
	 * @param pageParam
	 * @return
	 * @throws QueryException
	 * @throws RedirectExecption
	 */
	Map<String, Object> query(QueryType queryType, QueryParam param,
			PageBean... pageParam) throws QueryException, RedirectExecption;
}