package com.cnnic.whois.dao.queryV2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.WhoisUtil;

public abstract class IpQueryDao extends AbstractDbQueryDao {
	public IpQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}
	/**
	 * Connect to the database query ip information
	 * 
	 * @param startHighAddr
	 * @param endHighAddr
	 * @param startLowAddr
	 * @param endLowAddr
	 * @param role
	 * @return map collection
	 * @throws QueryException
	 */
	public Map<String, Object> queryIP(long startHighAddr, long endHighAddr,
			long startLowAddr, long endLowAddr, String role, String format)
			throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;
		String selectSql = "";
		try {
			connection = ds.getConnection();
			if (startHighAddr == 0) { //If fuzzy matching with the SQL statement
				selectSql = (endLowAddr == 0) ? (WhoisUtil.SELECT_LIST_IPv4_1
						+ startLowAddr + WhoisUtil.SELECT_LIST_IPv4_2
						+ startLowAddr + WhoisUtil.SELECT_LIST_IPv4_3)
						: (WhoisUtil.SELECT_LIST_IPv4_4 + startLowAddr
								+ WhoisUtil.SELECT_LIST_IPv4_2 + endLowAddr + WhoisUtil.SELECT_LIST_IPv4_5);
			} else {
				selectSql = (endLowAddr == 0) ? (WhoisUtil.SELECT_LIST_IPv6_1
						+ startHighAddr + WhoisUtil.SELECT_LIST_IPv6_2
						+ startHighAddr + WhoisUtil.SELECT_LIST_IPv6_3
						+ startLowAddr + WhoisUtil.SELECT_LIST_IPv6_4
						+ startHighAddr + WhoisUtil.SELECT_LIST_IPv6_5
						+ startHighAddr + WhoisUtil.SELECT_LIST_IPv6_6
						+ startLowAddr + WhoisUtil.SELECT_LIST_IPv6_7)

						: (WhoisUtil.SELECT_LIST_IPv6_8 + startHighAddr
								+ WhoisUtil.SELECT_LIST_IPv6_2 + startHighAddr
								+ WhoisUtil.SELECT_LIST_IPv6_3 + startLowAddr
								+ WhoisUtil.SELECT_LIST_IPv6_4 + endHighAddr
								+ WhoisUtil.SELECT_LIST_IPv6_5 + endHighAddr
								+ WhoisUtil.SELECT_LIST_IPv6_6 + endLowAddr
								+ WhoisUtil.SELECT_LIST_IPv6_7 + WhoisUtil.SELECT_LIST_IPv6_9);
			}
			
			Map<String, Object> ipMap = query(connection, selectSql,
					permissionCache.getIPKeyFileds(role), "$mul$IP", role, format);
			if(ipMap != null){
				map = rdapConformance(map);
				map.putAll(ipMap);
			}
			if(map != null){
				map.remove(WhoisUtil.getDisplayKeyName("StartLowAddress", format));
				map.remove(WhoisUtil.getDisplayKeyName("EndLowAddress", format));
				map.remove(WhoisUtil.getDisplayKeyName("StartHighAddress", format));
				map.remove(WhoisUtil.getDisplayKeyName("EndHighAddress", format));
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
	protected void preHandleNormalField(String keyName, String format,
			ResultSet results, Map<String, Object> map, String field)
			throws SQLException {
		if(keyName.equals(WhoisUtil.MULTIPRXIP) && field.equals("StartLowAddress")){
			if((map.get("Start Address") == null && map.get("End Address") == null) || (map.get("startAddress") == null && map.get("endAddress") == null)){
				String ipVersion = results.getString("IP_Version");
				
				String startHighAddress = results.getString("StartHighAddress");
				String startLowAddress = results.getString("StartLowAddress");
				String endHighAddress = results.getString("EndHighAddress");
				String endLowAddress = results.getString("EndLowAddress");
				
				String startAddress = "";
				String endAddress = "";
				
				if (ipVersion != null) {
					if (ipVersion.toString().indexOf("v6") != -1) { //judgment is IPv6 or IPv4
						startAddress = WhoisUtil.ipV6ToString(
								Long.parseLong(startHighAddress),
								Long.parseLong(startLowAddress));
						endAddress = WhoisUtil.ipV6ToString(
								Long.parseLong(endHighAddress),
								Long.parseLong(endLowAddress));
					} else {
						startAddress = WhoisUtil.longtoipV4(Long
								.parseLong(startLowAddress));
						endAddress = WhoisUtil
								.longtoipV4(Long.parseLong(endLowAddress));
					}
					map.put(WhoisUtil.getDisplayKeyName("Start_Address", format), startAddress);//a different fromat have different name;
					map.put(WhoisUtil.getDisplayKeyName("End_Address", format), endAddress);
				}
			}
		}
	}
}
