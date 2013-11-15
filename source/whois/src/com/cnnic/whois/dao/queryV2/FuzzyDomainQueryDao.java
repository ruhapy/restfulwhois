package com.cnnic.whois.dao.queryV2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.bean.index.DomainIndex;
import com.cnnic.whois.bean.index.SearchCondition;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.service.index.SearchResult;
import com.cnnic.whois.util.WhoisUtil;

public class FuzzyDomainQueryDao extends AbstractSearchQueryDao{
	
	public FuzzyDomainQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}
	
	@Override
	public Map<String, Object> query(QueryParam param, String role, String format,
			PageBean... page) throws QueryException {
		// TODO Auto-generated method stub
		return null;
	}
	public Map<String, Object> fuzzyQueryDoamin(String domain, String domainPuny, String role, 
			String format, PageBean page)
			throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;
		SearchCondition searchCondition = new SearchCondition("ldhName:"+domainPuny + " OR unicodeName:"+domain);
		int startPage = page.getCurrentPage() - 1;
		startPage = startPage >= 0 ? startPage : 0;
		int start = startPage * page.getMaxRecords();
		searchCondition.setStart(start);
		searchCondition.setRow(page.getMaxRecords());
		SearchResult<DomainIndex> result = domainIndexService.queryDomains(searchCondition);
		page.setRecordsCount(Long.valueOf(result.getTotalResults()).intValue());
		if(result.getResultList().size()==0){
			return map;
		}
		try {
			connection = ds.getConnection();
			String sql = WhoisUtil.SELECT_LIST_DNRDOMAIN;
			DomainIndex domainIndex = result.getResultList().get(0);
			if("rirDomain".equals(domainIndex.getDocType())){
				sql = WhoisUtil.SELECT_LIST_RIRDOMAIN;
			}
			Map<String, Object> domainMap = super.fuzzyQuery(connection, result,sql,"$mul$domains",
					role, format);
			if(domainMap != null){
				map =  rdapConformance(map);
				map.putAll(domainMap);
				addTruncatedParamToMap(map, result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new QueryException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException se) {
				}
			}
		}
		return map;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.FUZZYDOMAIN;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.FUZZYDOMAIN.equals(queryType);
	}
}