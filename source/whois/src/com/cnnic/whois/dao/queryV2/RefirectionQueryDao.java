package com.cnnic.whois.dao.queryV2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;
import com.cnnic.whois.util.WhoisUtil;

public abstract class RefirectionQueryDao extends AbstractDbQueryDao {
	public RefirectionQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}
	/**
	 * Query redirect information
	 * 
	 * @param tableName
	 * @param queryInfo
	 * @throws QueryException
	 * @throws RedirectExecption
	 *             When the throw this exception query data, and the data
	 *             content stored to the anomaly.
	 */
	public void queryRedirection(String tableName, String queryInfo)
			throws QueryException, RedirectExecption {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet results = null;
		String selectSql = "";

		if (tableName.equals(WhoisUtil.AUTNUM)) {
			int queryPara = Integer.parseInt(queryInfo);
			selectSql = WhoisUtil.SELECT_URL_AUTNUM_EDIRECTION1 + queryPara
					+ WhoisUtil.SELECT_URL_AUTNUM_EDIRECTION2 + queryPara;
		} else {
			queryInfo = queryInfo.substring(queryInfo.lastIndexOf(".") + 1);
			selectSql = WhoisUtil.SELECT_URL_DOAMIN_REDIRECTION + "'"
					+ queryInfo + "'";
		}

		try {
			connection = ds.getConnection();
			stmt = connection.prepareStatement(selectSql);
			results = stmt.executeQuery();
			if (results.next()) {
				throw new RedirectExecption(results.getString("redirectURL"));
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
	}
}
