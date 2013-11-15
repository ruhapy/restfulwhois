package com.cnnic.whois.dao.queryV2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.WhoisUtil;

public abstract class VariantsQueryDao extends AbstractDbQueryDao {
	public VariantsQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}
	/**
	 * Connect to the database query registrar information
	 * 
	 * @param queryInfo
	 * @param role
	 * @return map collection
	 * @throws QueryException
	 */
	public Map<String, Object> queryRegistrar(String queryInfo, String role, 
			boolean isJoinTable, String format)
			throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;

		try {
			connection = ds.getConnection();
			
			String selectSql = WhoisUtil.SELECT_LIST_JOIN_REGISTRAR + "'"
			+ queryInfo + "'";
			
			if(!isJoinTable)
			selectSql = WhoisUtil.SELECT_LIST_REGISTRAR + "'"
					+ queryInfo + "'";
			
			map = query(connection, selectSql,
					permissionCache.getRegistrarKeyFileds(role),
					"$mul$registrar", role, format);
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
