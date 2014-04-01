package com.cnnic.whois.dao.query.db;

import java.util.Map;

import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;
/**
 * query dao
 * @author nic
 *
 */
public interface QueryDao {
	/**
	 * get query join type
	 * 
	 * @return query type
	 */
	QueryType getQueryType();

	/**
	 * check is suppot query type
	 * 
	 * @param queryType
	 * @return true if support,false if not
	 */
	boolean supportType(QueryType queryType);

	/**
	 * query 
	 * @param param:query param
	 * @return query result map
	 * @throws QueryException
	 * @throws RedirectExecption
	 */
	Map<String, Object> query(QueryParam param)
			throws QueryException, RedirectExecption;

	/**
	 * get all data from db ,used for init cache
	 * @return query result map
	 * @throws QueryException
	 */
	Map<String, Object> getAll() throws QueryException;
}
