package com.cnnic.whois.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.cnnic.whois.execption.ManagementException;
import com.cnnic.whois.util.WhoisUtil;

public class RedirectionDAO {
	private static RedirectionDAO redirectDAO = new RedirectionDAO();
	private DataSource ds;

	/**
	 * Connect to the datasource in the constructor
	 * 
	 * @throws IllegalStateException
	 */
	private RedirectionDAO() throws IllegalStateException {
		try {
			InitialContext ctx = new InitialContext();
			ds = (DataSource) ctx.lookup(WhoisUtil.JNDI_NAME);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}
	}

	/**
	 * Get RedirectionDAO objects
	 * 
	 * @return RedirectionDAO objects
	 */
	public static RedirectionDAO getRedirectDAO() {
		return redirectDAO;
	}

	/**
	 * Add domainRedirection information
	 * 
	 * @param domainName
	 * @param redirectUrl
	 * @throws ManagementException
	 */
	public void addDomainRedirection(String domainName, String redirectUrl)
			throws ManagementException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet resultsIsNull = null;
		try {
			connection = ds.getConnection();

			String sql = WhoisUtil.ISNULL_DOMAIN_REDIRECT;
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, domainName);
			stmt.setString(2, redirectUrl);
			resultsIsNull = stmt.executeQuery();

			if (resultsIsNull.next())
				throw new ManagementException("Already exists in the data ");

			sql = WhoisUtil.INSERT_DOMAIN_REDIRECTION + "'" + domainName + "'"
					+ "," + "'" + redirectUrl + "')";
			connection = ds.getConnection();
			stmt = connection.prepareStatement(sql);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagementException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException se) {
				}
			}
		}
	}

	/**
	 * Add ipRedirection information
	 * 
	 * @param startHighAddr
	 * @param endHighAddr
	 * @param startLowAddr
	 * @param endLowAddr
	 * @param redirectUrl
	 * @throws ManagementException
	 */
	public void addIPRedirection(long startHighAddr, long endHighAddr,
			long startLowAddr, long endLowAddr, String redirectUrl)
			throws ManagementException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet resultsIsNull = null;
		try {
			connection = ds.getConnection();
			String sql = WhoisUtil.ISNULL_IP_REDIRECT;
			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, startHighAddr);
			stmt.setLong(2, endHighAddr);
			stmt.setLong(3, startLowAddr);
			stmt.setLong(4, endLowAddr);
			stmt.setString(5, redirectUrl);

			resultsIsNull = stmt.executeQuery();
			if (resultsIsNull.next())
				throw new ManagementException("Already exists in the data ");

			sql = WhoisUtil.INSERT_IP_REDIRECTION + startHighAddr + ","
					+ endHighAddr + "," + startLowAddr + "," + endLowAddr
					+ ",'" + redirectUrl + "')";
			connection = ds.getConnection();
			stmt = connection.prepareStatement(sql);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagementException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException se) {
				}
			}
		}
	}

	/**
	 * Add autunmRedirection information
	 * 
	 * @param startNumber
	 * @param endNumber
	 * @param redirectUrl
	 * @throws ManagementException
	 */
	public void addAutnumRedirection(String startNumber, String endNumber,
			String redirectUrl) throws ManagementException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet resultsIsNull = null;
		try {
			connection = ds.getConnection();
			String sql = WhoisUtil.ISNULL_AUTNUM_REDIRECT;
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, Integer.parseInt(startNumber));
			stmt.setInt(2, Integer.parseInt(endNumber));
			stmt.setString(3, redirectUrl);
			resultsIsNull = stmt.executeQuery();

			if (resultsIsNull.next()) {
				throw new ManagementException("Already exists in the data ");
			}

			sql = WhoisUtil.INSERT_AUTNUM_REDIRECTION
					+ Integer.parseInt(startNumber) + ","
					+ Integer.parseInt(endNumber) + "," + "'" + redirectUrl
					+ "')";

			connection = ds.getConnection();
			stmt = connection.prepareStatement(sql);
			stmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagementException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException se) {
				}
			}
		}
	}

	/**
	 * List redirection information
	 * 
	 * @param tableName
	 * @return map collection
	 * @throws ManagementException
	 */
	public Map<Integer, List<String>> listRedirect(String tableName)
			throws ManagementException {
		Connection connection = null;
		PreparedStatement stmt = null;
		Map<Integer, List<String>> redirectInfoList = new HashMap<Integer, List<String>>();

		try {
			connection = ds.getConnection();
			String sql = WhoisUtil.SELECT_REDIRECT + tableName + "_redirect";

			stmt = connection.prepareStatement(sql);
			ResultSet results = stmt.executeQuery();
			while (results.next()) {
				List<String> list = new ArrayList<String>();
				if (tableName.equals(WhoisUtil.AUTNUM)) {
					list.add(results.getString("startNumber"));
					list.add(results.getString("endNumber"));

				} else if (tableName.equals(WhoisUtil.IP)) {
					//The long type data into ip
					long startHightAddress = results
							.getLong("StartHighAddress");
					long startLowAddress = results.getLong("StartLowAddress");
					long endHighAddress = results.getLong("EndHighAddress");
					long endLowAddress = results.getLong("EndLowAddress");

					String startNumber = "";
					String endNumber = "";

					if (startHightAddress != 0 && endHighAddress != 0) {
						startNumber = WhoisUtil.ipV6ToString(startHightAddress,
								startLowAddress);
						endNumber = WhoisUtil.ipV6ToString(endHighAddress,
								endLowAddress);
					} else {
						startNumber = WhoisUtil.longtoipV4(startLowAddress);
						endNumber = WhoisUtil.longtoipV4(endLowAddress);
					}

					list.add(startNumber);
					list.add(endNumber);

				} else {
					list.add(results.getString("redirectType"));
				}
				list.add(results.getString("redirectURL"));
				redirectInfoList.put(results.getInt("id"), list);
			}
			return redirectInfoList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagementException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException se) {
				}
			}
		}
	}

	/**
	 * Update domainRedirection information
	 * 
	 * @param domainName
	 * @param id
	 * @param redirectUrl
	 * @throws ManagementException
	 */
	public void updateDomainRedirection(String domainName, int id,
			String redirectUrl) throws ManagementException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet resultsIsNull = null;
		try {
			connection = ds.getConnection();

			String sql = WhoisUtil.ISNULL_DOMAIN_REDIRECT;
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, domainName);
			stmt.setString(2, redirectUrl);
			resultsIsNull = stmt.executeQuery();

			if (resultsIsNull.next())
				throw new ManagementException("Already exists in the data ");

			sql = WhoisUtil.UPDATE_DOMAIN_REDIRECTION  + "'" + domainName + "'"
					+ WhoisUtil.UPDATE_REDIRECTION1 + "'" + redirectUrl + "'"
					+ WhoisUtil.UPDATE_REDIRECTION2 + id;
			connection = ds.getConnection();
			stmt = connection.prepareStatement(sql);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagementException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException se) {
				}
			}
		}
	}

	/**
	 * Update autnumRedirection information
	 * 
	 * @param startNumber
	 * @param endNumber
	 * @param id
	 * @param redirectUrl
	 * @throws ManagementException
	 */
	public void updateAutnumRedirection(String startNumber, String endNumber,
			int id, String redirectUrl) throws ManagementException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet resultsIsNull = null;
		try {
			connection = ds.getConnection();
			String sql = WhoisUtil.ISNULL_AUTNUM_REDIRECT;
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, Integer.parseInt(startNumber));
			stmt.setInt(2, Integer.parseInt(endNumber));
			stmt.setString(3, redirectUrl);

			resultsIsNull = stmt.executeQuery();
			if (resultsIsNull.next())
				throw new ManagementException("Already exists in the data ");

			sql = WhoisUtil.UPDATE_AUTNUM_REDIRECTION1
					+ Integer.parseInt(startNumber)
					+ WhoisUtil.UPDATE_AUTNUM_REDIRECTION2
					+ Integer.parseInt(endNumber)
					+ WhoisUtil.UPDATE_REDIRECTION1 + "'" + redirectUrl + "'"
					+ WhoisUtil.UPDATE_REDIRECTION2 + id;

			connection = ds.getConnection();
			stmt = connection.prepareStatement(sql);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagementException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException se) {
				}
			}
		}
	}

	/**
	 * Update ipRedirection information
	 * 
	 * @param startHighAddr
	 * @param endHighAddr
	 * @param startLowAddr
	 * @param endLowAddr
	 * @param id
	 * @param redirectUrl
	 * @throws ManagementException
	 */
	public void updateIPRedirection(long startHighAddr, long endHighAddr,
			long startLowAddr, long endLowAddr, int id, String redirectUrl)
			throws ManagementException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet resultsIsNull = null;
		try {
			connection = ds.getConnection();
			String sql = WhoisUtil.ISNULL_IP_REDIRECT;
			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, startHighAddr);
			stmt.setLong(2, endHighAddr);
			stmt.setLong(3, startLowAddr);
			stmt.setLong(4, endLowAddr);
			stmt.setString(5, redirectUrl);

			resultsIsNull = stmt.executeQuery();
			if (resultsIsNull.next())
				throw new ManagementException("Already exists in the data ");

			sql = WhoisUtil.UPDATE_IP_REDIRECTION1 + startHighAddr
					+ WhoisUtil.UPDATE_IP_REDIRECTION2 + endHighAddr
					+ WhoisUtil.UPDATE_IP_REDIRECTION3 + startLowAddr
					+ WhoisUtil.UPDATE_IP_REDIRECTION4 + endLowAddr
					+ WhoisUtil.UPDATE_REDIRECTION1 + "'" + redirectUrl + "'"
					+ WhoisUtil.UPDATE_REDIRECTION2 + id;
			connection = ds.getConnection();
			stmt = connection.prepareStatement(sql);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagementException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException se) {
				}
			}
		}
	}

	/**
	 * Delete redirection information
	 * 
	 * @param id
	 * @param tableName
	 * @throws ManagementException
	 */
	public void deleteRedirect(int id, String tableName)
			throws ManagementException {
		Connection connection = null;
		PreparedStatement stmt = null;

		try {
			connection = ds.getConnection();
			String sql = WhoisUtil.DELETE_REDIRECT1 + tableName + "_redirect"
					+ WhoisUtil.DELETE_REDIRECT2;

			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ManagementException(e);
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
