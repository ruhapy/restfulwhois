package com.cnnic.whois.dao.queryV2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.WhoisUtil;

public abstract class AsQueryDao extends AbstractDbQueryDao {
	public AsQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}
	/**
	 * Connect to the database query AS information
	 * 
	 * @param queryInfo
	 * @param role
	 * @return map collection
	 * @throws QueryException
	 */
	public Map<String, Object> queryAS(int queryInfo, String role, String format)
			throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;

		try {
			connection = ds.getConnection();
			String selectSql = WhoisUtil.SELECT_LIST_AS1 + queryInfo
					+ WhoisUtil.SELECT_LIST_AS2 + queryInfo
					+ WhoisUtil.SELECT_LIST_AS3;
			Map<String, Object> asMap = query(connection, selectSql,
					permissionCache.getASKeyFileds(role), "$mul$autnum", role, format);
			if(asMap != null){
				map = rdapConformance(map);
				map.putAll(asMap);
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

}
