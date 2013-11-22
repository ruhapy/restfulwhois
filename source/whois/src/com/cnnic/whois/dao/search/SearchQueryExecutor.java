package com.cnnic.whois.dao.search;

import java.util.ArrayList;
import java.util.List;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.bean.index.Index;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;
import com.cnnic.whois.service.index.SearchResult;
import com.cnnic.whois.util.WhoisProperties;

public class SearchQueryExecutor {
	private static SearchQueryExecutor executor = new SearchQueryExecutor();

	public static SearchQueryExecutor getExecutor() {
		return executor;
	}

	private List<SearchQueryDao> searchQueryDaos = new ArrayList<SearchQueryDao>();

	private void init() {
		searchQueryDaos.add(new DomainQueryDao(WhoisProperties
				.getDomainSolrUrl()));
		searchQueryDaos.add(new EntityQueryDao(WhoisProperties
				.getEntitySolrUrl()));
		searchQueryDaos.add(new NsQueryDao(WhoisProperties
				.getNameServerSolrUrl()));
	}

	public SearchQueryExecutor() {
		super();
		init();
	}

	public SearchResult<? extends Index> query(QueryType queryType,
			QueryParam param, PageBean... pageParam) throws QueryException,
			RedirectExecption {
		for (SearchQueryDao queryDao : searchQueryDaos) {
			if (queryDao.supportType(queryType)) {
				return queryDao.query(param, pageParam);
			}
		}
		return null;
	}
}