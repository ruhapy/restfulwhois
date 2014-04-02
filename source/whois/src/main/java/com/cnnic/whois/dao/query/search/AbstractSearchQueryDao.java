package com.cnnic.whois.dao.query.search;

import java.lang.reflect.ParameterizedType;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.index.SearchCondition;
import com.cnnic.whois.service.index.SearchResult;
import com.cnnic.whois.util.WhoisUtil;

/**
 * search query dao,query from solr
 * @author nic
 *
 * @param <T>
 */
public abstract class AbstractSearchQueryDao<T> implements SearchQueryDao {
	private CommonsHttpSolrServer server;

	/**
	 * construction
	 * @param url:solr core url
	 */
	public AbstractSearchQueryDao(String url) {
		super();
		try {
			this.server = new CommonsHttpSolrServer(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * search by query string
	 * @param q:query string
	 * @param page:page param
	 * @return search result
	 */
	protected SearchResult<T> query(String q, PageBean page) {
		SearchCondition searchCondition = new SearchCondition(q);
		int startPage = page.getCurrentPage() - 1;
		startPage = startPage >= 0 ? startPage : 0;
		int start = startPage * page.getMaxRecords();
		searchCondition.setStart(start);
		searchCondition.setRow(page.getMaxRecords());
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery(searchCondition.getSearchword());
		solrQuery.setStart(Integer.valueOf(searchCondition.getStart()));
		solrQuery.setRows(Integer.valueOf(searchCondition.getRow()));
		QueryResponse queryResponse = null;
		try {
			queryResponse = this.server.query(solrQuery);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		SearchResult<T> searchResult = new SearchResult<T>();
		try {
			setSearchResult(searchResult, queryResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		page.setRecordsCount(Long.valueOf(searchResult.getTotalResults()).intValue());
		return searchResult;
	}
	
	/**
	 * set search result
	 * @param searchResult :search result
	 * @param queryResponse:search result from solr
	 */
	@SuppressWarnings("unchecked")
	private void setSearchResult(SearchResult<T> searchResult,
			QueryResponse queryResponse) {
		searchResult.setSearchTime(queryResponse.getElapsedTime() / 1000.0D);
		searchResult.setTotalResults(queryResponse.getResults().getNumFound());
		Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
		List indexes = queryResponse.getBeans(entityClass);
		searchResult.setResultList(indexes);
	}
	/**
	 * escape special solr char
	 * @param q:query string
	 * @return encoded query string
	 */
	public static String escapeSolrChar(String q){
		return WhoisUtil.escapeQueryChars(q);
	}
}