package com.cnnic.whois.dao.queryV2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.WhoisUtil;

public abstract class DelegationKeysQueryDao extends AbstractDbQueryDao {
	public DelegationKeysQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}
	/**
	 * Connect to the database query delegationKey information
	 * 
	 * @param queryInfo
	 * @param role
	 * @return map collection
	 * @throws QueryException
	 */
	public Map<String, Object> queryDelegationKeys(String queryInfo, String role, String format)
			throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;

		try {
			connection = ds.getConnection();
			String selectSql = WhoisUtil.SELECT_LIST_DELEGATIONKEYS + "'"
					+ queryInfo + "'";
			map = query(connection, selectSql,
					permissionCache.getDelegationKeyFileds(role),
					"$mul$delegationKeys", role, format);
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
