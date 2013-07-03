package com.cnnic.whois.service;

import java.util.List;
import java.util.Map;

import com.cnnic.whois.dao.RedirectionDAO;
import com.cnnic.whois.execption.ManagementException;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.WhoisUtil;

public class RedirectionService {
	private static RedirectionService redirectService = new RedirectionService();

	private RedirectionService() {
	}

	/**
	 * Get RedirectionService objects
	 * 
	 * @return RedirectionService Object
	 */
	public static RedirectionService getRedirectService() {
		return redirectService;
	}

	/**
	 * Add redirection information
	 * @param startNumber
	 * @param endNumber
	 * @param redirectUrl
	 * @param tableName
	 * @throws ManagementException
	 */
	public void addRedirect(String startNumber, String endNumber,
			String redirectUrl, String tableName) throws ManagementException {
		RedirectionDAO redirectDAO = RedirectionDAO.getRedirectDAO();

		if (tableName.endsWith(WhoisUtil.IP)) {
			String[] startAddress = isIPMaskNetWork(startNumber);
			String[] endAddress = isIPMaskNetWork(endNumber);
			
			long[] ipLongs = WhoisUtil.parsingIp(startAddress[0],
					endAddress[0], Integer.parseInt(startAddress[1]),
					Integer.parseInt(endAddress[1]));

			redirectDAO.addIPRedirection(ipLongs[0], ipLongs[1], ipLongs[2],
					ipLongs[3], redirectUrl);
			return;
		} else if (tableName.endsWith(WhoisUtil.AUTNUM)) {
			redirectDAO.addAutnumRedirection(startNumber, endNumber,
					redirectUrl);
			return;
		}
		redirectDAO.addDomainRedirection(startNumber, redirectUrl);
	}

	private String[] isIPMaskNetWork(String ipInfo) {
		String[] infoArray = new String[2];
		if (ipInfo.indexOf(WhoisUtil.PRX) >= 0) {
			infoArray = ipInfo.split(WhoisUtil.PRX);
		} else {
			infoArray[0] = ipInfo;
			infoArray[1] = "0";
		}
		return infoArray;
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
		RedirectionDAO redirectDAO = RedirectionDAO.getRedirectDAO();
		return redirectDAO.listRedirect(tableName);
	}

	/**
	 * Update redirection information
	 * @param startNumber
	 * @param endNumber
	 * @param redirectUrl
	 * @param id
	 * @param tableName
	 * @throws ManagementException
	 */
	public void updateRedirect(String startNumber, String endNumber,
			String redirectUrl, int id, String tableName)
			throws ManagementException {
		RedirectionDAO redirectDAO = RedirectionDAO.getRedirectDAO();
		if (tableName.endsWith(WhoisUtil.IP)) {
			String[] startAddress = isIPMaskNetWork(startNumber);
			String[] endAddress = isIPMaskNetWork(endNumber);

			long[] ipLongs = WhoisUtil.parsingIp(startAddress[0],
					endAddress[0], Integer.parseInt(startAddress[1]),
					Integer.parseInt(endAddress[1]));;
			
			redirectDAO.updateIPRedirection(ipLongs[0], ipLongs[1], ipLongs[2],
					ipLongs[3], id,redirectUrl);
			return;
		} else if (tableName.endsWith(WhoisUtil.AUTNUM)) {
			redirectDAO.updateAutnumRedirection(startNumber, endNumber,id,
					redirectUrl);
			return;
		}
		redirectDAO.updateDomainRedirection(startNumber,id, redirectUrl);
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
		RedirectionDAO redirectDAO = RedirectionDAO.getRedirectDAO();
		redirectDAO.deleteRedirect(id, tableName);
	}

}