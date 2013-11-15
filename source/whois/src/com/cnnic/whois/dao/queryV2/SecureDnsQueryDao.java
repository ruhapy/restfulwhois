package com.cnnic.whois.dao.queryV2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.WhoisUtil;

public abstract class SecureDnsQueryDao extends AbstractDbQueryDao {
	public SecureDnsQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}
	/**
	 * Connect to the database query SecureDNS information
	 * 
	 * @param queryInfo
	 * @param role
	 * @return map collection
	 * @throws QueryException
	 */
	public Map<String, Object> querySecureDNS(String queryInfo, String role, String format)
			throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;

		try {
			connection = ds.getConnection();
			String selectSql = WhoisUtil.SELECT_LIST_SECUREDNS + "'" + queryInfo
					+ "'";
			map = query(connection, selectSql,
					permissionCache.getSecureDNSMapKeyFileds(role),
					"$mul$secureDNS", role, format);
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
	protected String getJoinFieldName(String keyName) {
		String fliedName = "";
		if (keyName.equals(WhoisUtil.JOINSECUREDNS) || keyName.equals("$mul$secureDNS")){
			fliedName = "SecureDNSID";
		}else {
			fliedName = WhoisUtil.HANDLE;
		}
		return fliedName;
	}
}