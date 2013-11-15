package com.cnnic.whois.dao.queryV2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.WhoisUtil;

public abstract class LinksQueryDao extends AbstractDbQueryDao {
	public LinksQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}
	/**
	 * Connect to the database query link information
	 * 
	 * @param queryPara
	 * @param role
	 * @return map collection
	 * @throws QueryException
	 */
	public Map<String, Object> queryLinks(String queryPara, String role, String format)
			throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;

		try {
			connection = ds.getConnection();
			String selectSql = WhoisUtil.SELECT_LIST_LINK + "'" + queryPara
					+ "'";
			map = query(connection, selectSql,
					permissionCache.getLinkKeyFileds(role), "$mul$link", role, format);
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
