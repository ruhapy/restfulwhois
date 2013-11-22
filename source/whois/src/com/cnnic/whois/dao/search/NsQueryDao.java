package com.cnnic.whois.dao.search;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.bean.index.NameServerIndex;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.service.index.SearchResult;

public class NsQueryDao extends AbstractSearchQueryDao<NameServerIndex> {

	public NsQueryDao(String url) {
		super(url);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.NAMESERVER;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.NAMESERVER.equals(queryType);
	}

	@Override
	public SearchResult<NameServerIndex> query(QueryParam param,
			PageBean... pageParam) throws QueryException {
		PageBean page = pageParam[0];
		SearchResult<NameServerIndex> result = query(param.getQ(), page);
		return result;
	}
}