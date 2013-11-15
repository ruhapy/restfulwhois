package com.cnnic.whois.dao.queryV2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.WhoisUtil;

public abstract class ErrorMsgQueryDao extends AbstractDbQueryDao {
	public ErrorMsgQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}

	/**
	 * Generate an error map collection
	 * 
	 * @param errorCode
	 * @param title
	 * @param description
	 * @return map
	 */
	public Map<String, Object> getErrorMessage(String errorCode, String role,
			String format) throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;
		try {
			connection = ds.getConnection();

			String selectSql = WhoisUtil.SELECT_LIST_ERRORMESSAGE + "'"
					+ errorCode + "'";
			Map<String, Object> errorMessageMap = query(connection, selectSql,
					permissionCache.getErrorMessageKeyFileds(role),
					"$mul$errormessage", role, format);
			if (errorMessageMap != null) {
				map = rdapConformance(map);
				map.putAll(errorMessageMap);
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
	@Override
	protected String getJoinFieldName(String keyName) {
		String fliedName = "";
		if (keyName.equals("$mul$errormessage")){
			fliedName = "Error_Code";
		}else {
			fliedName = WhoisUtil.HANDLE;
		}
		return fliedName;
	}
}
