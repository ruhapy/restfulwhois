package com.cnnic.whois.dao.query.search;

import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.bean.index.NameServerIndex;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.service.index.SearchResult;
import com.cnnic.whois.util.validate.ValidateUtils;

public class NsQueryDao extends AbstractSearchQueryDao<NameServerIndex> {

	public NsQueryDao(String url) {
		super(url);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.SEARCHNS;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return getQueryType().equals(queryType);
	}

	@Override
	public SearchResult<NameServerIndex> search(QueryParam param)
			throws QueryException {
		String q = param.getQ();
		q = q.replace("\\:", ":");
		if(ValidateUtils.isIpv4(q)){
			q = EntityQueryDao.geneNsQByPreciseIpv4(q);			
		}else if (ValidateUtils.isIPv6(q)){
			q = EntityQueryDao.geneNsQByPreciseIpv6(q);
		} else {
			q = escapeSolrChar(q);
		}
		SearchResult<NameServerIndex> result = query(q, param.getPage());
		return result;
	}
}
