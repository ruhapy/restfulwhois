package com.cnnic.whois.dao.queryV2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.WhoisUtil;

public abstract class HelpQueryDao extends AbstractDbQueryDao {
	public HelpQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}

	public Map<String, Object> getHelp(String helpCode, String role,
			String format) throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;
		try {
			connection = ds.getConnection();

			String selectSql = WhoisUtil.SELECT_HELP + "'" + helpCode + "'";
			Map<String, Object> helpMap = query(connection, selectSql,
					permissionCache.getHelpKeyFileds(role), "$mul$notices",
					role, format);
			if (helpMap != null) {
				map = rdapConformance(map);
				map.putAll(helpMap);
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
