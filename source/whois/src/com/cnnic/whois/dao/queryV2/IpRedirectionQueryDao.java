package com.cnnic.whois.dao.queryV2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.execption.RedirectExecption;
import com.cnnic.whois.util.WhoisUtil;

public abstract class IpRedirectionQueryDao extends AbstractDbQueryDao {
	public IpRedirectionQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}

	/**
	 * Query IPRedirection information
	 * 
	 * @param startHighAddr
	 * @param endHighAddr
	 * @param startLowAddr
	 * @param endLowAddr
	 * @throws QueryException
	 * @throws RedirectExecption
	 *             When the throw this exception query data, and the data
	 *             content stored to the anomaly.
	 */
	public void queryIPRedirection(long startHighAddr, long endHighAddr,
			long startLowAddr, long endLowAddr) throws QueryException,
			RedirectExecption {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet results = null;

		String selectSql = "";
		if (startHighAddr == 0) {
			selectSql = (WhoisUtil.SELECT_URL_IPV4_REDIRECTION1 + startLowAddr
					+ WhoisUtil.SELECT_URL_IPV4_REDIRECTION2 + startLowAddr + WhoisUtil.SELECT_URL_IPV4_REDIRECTION3);
		} else {

			selectSql = WhoisUtil.SELECT_URL_IPV6_REDIRECTION1 + startHighAddr
					+ WhoisUtil.SELECT_URL_IPV6_REDIRECTION2 + startHighAddr
					+ WhoisUtil.SELECT_URL_IPV6_REDIRECTION3 + startLowAddr
					+ WhoisUtil.SELECT_URL_IPV6_REDIRECTION4 + startHighAddr
					+ WhoisUtil.SELECT_URL_IPV6_REDIRECTION5 + startHighAddr
					+ WhoisUtil.SELECT_URL_IPV6_REDIRECTION6 + startLowAddr
					+ WhoisUtil.SELECT_URL_IPV6_REDIRECTION7;
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
