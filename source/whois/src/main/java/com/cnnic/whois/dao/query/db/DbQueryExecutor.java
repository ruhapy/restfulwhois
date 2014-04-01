package com.cnnic.whois.dao.query.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.dao.query.QueryExecutor;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;
/**
 * db query executor ,query from db
 * @author nic
 *
 */
@Service("dbQueryExecutor")
public class DbQueryExecutor implements QueryExecutor {
	private static DbQueryExecutor executor = new DbQueryExecutor();

	/**
	 * get executor
	 * @return executor
	 */
	public static DbQueryExecutor getExecutor() {
		return executor;
	}

	@Resource(name = "dbQueryDaos")
	private List<AbstractDbQueryDao> dbQueryDaos;

	@Override
	public Map<String, Object> query(QueryType queryType, QueryParam param,
			PageBean... pageParam) throws QueryException, RedirectExecption {
		for (AbstractDbQueryDao queryDao : dbQueryDaos) {
			if (queryDao.supportType(queryType)) {
				return queryDao.query(param);
			}
		}
		return null;
	}

	/**
	 * get all entities from db,used for init cache
	 * @param queryType:query type
	 * @return query result map
	 * @throws QueryException
	 */
	public Map<String, Object> getAll(QueryType queryType)
			throws QueryException {
		for (QueryDao queryDao : dbQueryDaos) {
			if (queryDao.supportType(queryType)) {
				return queryDao.getAll();
			}
		}
		return null;
	}

	/**
	 * get key fields by role,used for permission controll
	 * @param queryType:query type
	 * @param role:user role
	 * @return key fields list
	 */
	public List<String> getKeyFields(QueryType queryType, String role) {
		for (AbstractDbQueryDao queryDao : dbQueryDaos) {
			if (queryDao.supportType(queryType)) {
				return queryDao.getKeyFields(role);
			}
		}
		return new ArrayList<String>();
	}
	/**
	 * used for search,get search map key of query result map, implemented by each search type
	 * @param queryType:query type
	 * @return map key string,null if none key found
	 */
	public String getMapKey(QueryType queryType) {
		for (AbstractDbQueryDao queryDao : dbQueryDaos) {
			if (queryDao.supportType(queryType)) {
				return queryDao.getMapKey();
			}
		}
		return null;
	}

	/**
	 * format value after query
	 * @param queryType:query type
	 * @param map:query result map
	 * @return formated query result map
	 */
	public Map<String, Object> formatValue(QueryType queryType,
			Map<String, Object> map) {
		for (AbstractDbQueryDao queryDao : dbQueryDaos) {
			if (queryDao.supportType(queryType)) {
				return queryDao.formatValue(map);
			}
		}
		return map;
	}
}