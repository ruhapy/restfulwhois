package com.cnnic.whois.dao.query;

import java.util.Map;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryJoinType;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;

public interface QueryDao {
	/**
	 * get query join type
	 * 
	 * @return
	 */
	QueryType getQueryType();

	/**
	 * suppot query type
	 * 
	 * @param queryType
	 * @return
	 */
	boolean supportType(QueryType queryType);

	/**
	 * 
	 * @param queryJoinType
	 * @return
	 */
	boolean supportJoinType(QueryType queryType, QueryJoinType queryJoinType);

	/**
	 * query
	 * 
	 * @param q
	 * @param role
	 * @param format
	 * @return
	 * @throws QueryException 
	 */
	// TODO:remove format and role
	Map<String, Object> query(String q, String role, String format,PageBean ...page) throws QueryException;

	/**
	 * query as join
	 * 
	 * @param handle
	 * @param role
	 * @param format
	 * @return
	 */
	Map<String, Object> queryJoins(String handle, String role, String format);

	/**
	 * join id column name
	 * 
	 * @return
	 */
	String getJoinFieldIdColumnName();
}
