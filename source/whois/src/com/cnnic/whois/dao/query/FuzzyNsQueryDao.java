package com.cnnic.whois.dao.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.bean.index.NameServerIndex;
import com.cnnic.whois.bean.index.SearchCondition;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.service.index.SearchResult;
import com.cnnic.whois.util.WhoisUtil;

public class FuzzyNsQueryDao extends AbstractSearchQueryDao {

	public FuzzyNsQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}

	// public static AbstractDbQueryDao getQueryDAO() {
	// return queryDAO;
	// }

	@Override
	public Map<String, Object> query(QueryParam param, String role, String format,
			PageBean... page) throws QueryException {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Object> fuzzyQueryNameServer(String queryInfo,
			String role, String format, PageBean page) throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;
		SearchCondition searchCondition = new SearchCondition(queryInfo);
		int startPage = page.getCurrentPage() - 1;
		startPage = startPage >= 0 ? startPage : 0;
		int start = startPage * page.getMaxRecords();
		searchCondition.setStart(start);
		searchCondition.setRow(page.getMaxRecords());
		SearchResult<NameServerIndex> result = nameServerIndexService
				.queryNameServers(searchCondition);
		page.setRecordsCount(Long.valueOf(result.getTotalResults()).intValue());
		try {
			connection = ds.getConnection();
			String selectSql = WhoisUtil.SELECT_LIST_NAMESREVER + "'"
					+ queryInfo + "'";
			Map<String, Object> nsMap = fuzzyQuery(connection, result,
					selectSql, "$mul$nameServer", role, format);
			if (nsMap != null) {
				map = rdapConformance(map);
				map.putAll(nsMap);
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
	
	protected Map<String, Object> postHandleFieldsFuzzy(String keyName,
			String format, Map<String, Object> map) {
		Map<String, Object> map_IP = new LinkedHashMap<String, Object>();
		Object IPAddressArray = map.get(WhoisUtil.getDisplayKeyName("IPV4_Addresses", format));
		map_IP.put(WhoisUtil.IPV4PREFIX, IPAddressArray);
		IPAddressArray = map.get(WhoisUtil.getDisplayKeyName("IPV6_Addresses", format));
		map_IP.put(WhoisUtil.IPV6PREFIX, IPAddressArray);
		map.put(WhoisUtil.IPPREFIX, map_IP);
		map.remove(WhoisUtil.getDisplayKeyName("IPV4_Addresses", format));
		map.remove(WhoisUtil.getDisplayKeyName("IPV6_Addresses", format));
		return map;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.FUZZYNS;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.FUZZYNS.equals(queryType);
	}
}