package com.cnnic.whois.service;

import java.util.Arrays;
import java.util.Map;

import com.cnnic.whois.dao.ExColumnDAO;
import com.cnnic.whois.execption.ManagementException;
import com.cnnic.whois.util.ColumnCache;
import com.cnnic.whois.util.WhoisUtil;

public class ExColumnService {
	private static ExColumnService columnService = new ExColumnService();

	private ExColumnService() {
	}

	/**
	 * Get ExColumnService objects
	 * 
	 * @return ExColumnService Object
	 */
	public static ExColumnService getColumnService() {
		return columnService;
	}

	/**
	 * Add the extension field
	 * 
	 * @param tableName
	 * @param columnMap
	 * @throws ManagementException
	 */
	public void addCoulumn(String tableName, Map<String, String> columnMap)
			throws ManagementException {
		ExColumnDAO columnDAO = ExColumnDAO.getColumnDAO();
		columnDAO.addCoulumn(tableName, columnMap);
		setColumnExtendList(tableName);
	}

	/**
	 * List extension field
	 * 
	 * @param tableName
	 * @return map collection
	 * @throws ManagementException
	 */
	public Map<String, String> listCoulumn(String tableName)
			throws ManagementException {
		ExColumnDAO columnDAO = ExColumnDAO.getColumnDAO();
		return columnDAO.listCoulumn(tableName);
	}

	/**
	 * Update the extension field
	 * 
	 * @param tableName
	 * @param oldColumnName
	 * @param newCloumnName
	 * @param columnLength
	 * @throws ManagementException
	 */
	public void updateCoulumn(String tableName, String oldColumnName,
			String newCloumnName, String columnLength)
			throws ManagementException {
		ExColumnDAO columnDAO = ExColumnDAO.getColumnDAO();
		columnDAO.updateCoulumn(tableName, oldColumnName, newCloumnName,
				columnLength);
		setColumnExtendList(tableName);

	}

	/**
	 * Delete the extension field
	 * 
	 * @param tableName
	 * @param columnName
	 * @throws ManagementException
	 */
	public void deleteCoulumn(String tableName, String columnName)
			throws ManagementException {
		ExColumnDAO columnDAO = ExColumnDAO.getColumnDAO();
		columnDAO.deleteCoulumn(tableName, columnName);
		setColumnExtendList(tableName);
	}

	/**
	 * When the extension field after the update is complete, call the method
	 * will be updated to add data to the ColumnCache cache
	 * 
	 * @param tableName
	 */
	private void setColumnExtendList(String tableName) {
		ColumnCache columnCache = ColumnCache.getColumnCache();
		int typeBinary = Arrays.binarySearch(WhoisUtil.extendColumnTableTypes,
				tableName);
		switch (typeBinary) {
		case 0:
			columnCache.setASKeyFileds();
			break;
		case 1:
			columnCache.setDelegationKeyFileds();
			break;
		case 2:
			columnCache.setDNRDomainKeyFileds();
			break;
		case 3:
			columnCache.setDNREntityKeyFileds();
			break;
		case 4:
			columnCache.setIPKeyFileds();
			break;
		case 5:
			columnCache.setLinkKeyFileds();
			break;
		case 6:
			columnCache.setNameServerKeyFileds();
			break;
		case 7:
			columnCache.setNoticesKeyFileds();
			break;
		case 8:
			columnCache.setPhonesKeyFileds();
			break;
		case 9:
			columnCache.setPostalAddressKeyFileds();
			break;
		case 10:
			columnCache.setRegistrarKeyFileds();
			break;
		case 11:
			columnCache.setRIRDomainKeyFileds();
			break;
		case 12:
			columnCache.setRIREntityKeyFileds();
			break;
		case 13:
			columnCache.setVariantsKeyFileds();
			break;

		}
	}
}
