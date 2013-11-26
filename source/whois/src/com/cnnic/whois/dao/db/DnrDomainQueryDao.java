package com.cnnic.whois.dao.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.ColumnCache;
import com.cnnic.whois.util.PermissionCache;
import com.cnnic.whois.util.WhoisUtil;

public class DnrDomainQueryDao extends AbstractDomainQueryDao {

	public DnrDomainQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}

	@Override
	public Map<String, Object> query(QueryParam param, 
			PageBean... page) throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;

		try {
			connection = ds.getConnection();
			List<String> keyFields = ColumnCache.getColumnCache().getDNRDomainKeyFileds();
			Map<String, Object> domainMap = query(
					WhoisUtil.SELECT_LIST_DNRDOMAIN, keyFields, param.getQ());
			if (domainMap != null) {
				map = rdapConformance(map);
				map.putAll(domainMap);
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
	public Map<String, Object> getAll(String role) throws QueryException {
		List<String> dnrKeyFields = ColumnCache.getColumnCache().getDNRDomainKeyFileds();
		Map<String, Object> dnrDomains = queryBySql(GET_ALL_DNRDOMAIN,
				dnrKeyFields);
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		getListFromMap(dnrDomains, mapList);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(QUERY_KEY, mapList.toArray());
		return result;
	}

	private void getListFromMap(Map<String, Object> allDnrEntity,
			List<Map<String, Object>> mapList) {
		if(null != allDnrEntity.get("Handle")){// only one result
			mapList.add(allDnrEntity);
		}else{
			Object[] entities = (Object[]) allDnrEntity.get(QUERY_KEY);
			for(Object entity: entities){
				mapList.add((Map<String, Object>)entity);
			}
		}
	}
	
	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.DNRDOMAIN.equals(queryType);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.DNRDOMAIN;
	}
	
	@Override
	public List<String> getKeyFields(String role) {
		return PermissionCache.getPermissionCache().getDNRDomainKeyFileds(role);
	}
}