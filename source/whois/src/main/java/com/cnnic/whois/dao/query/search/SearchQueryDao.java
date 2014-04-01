package com.cnnic.whois.dao.query.search;

import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.bean.index.Index;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.service.index.SearchResult;

/**
 * search query dao 
 * @author nic
 *
 */
public interface SearchQueryDao{
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
	 * search object
	 * @param param:query param
	 * @return search result
	 * @throws QueryException
	 */
	SearchResult<? extends Index> search(QueryParam param)
			throws QueryException;
}