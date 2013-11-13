package com.cnnic.whois.dao.query;

import java.util.Map;

import com.cnnic.whois.bean.QueryJoinType;
import com.cnnic.whois.bean.QueryType;

public interface QueryDao {
	/**
	 * get query join type
	 * 
	 * @return
	 */
	QueryJoinType getQueryJoinType();

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
	boolean supportJoinType(QueryJoinType queryJoinType);

	/**
	 * query
	 * 
	 * @param q
	 * @param role
	 * @param format
	 * @return
	 */
	// TODO:remove format and role
	Map<String, Object> query(String q, String role, String format);

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
