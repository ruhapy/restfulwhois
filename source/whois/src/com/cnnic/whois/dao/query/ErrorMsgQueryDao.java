package com.cnnic.whois.dao.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryJoinType;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.WhoisUtil;

public class ErrorMsgQueryDao extends AbstractDbQueryDao {
	public ErrorMsgQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}

	@Override
	public Map<String, Object> query(QueryParam param, String role,
			String format, PageBean... page) throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;
		try {
			connection = ds.getConnection();
			String selectSql = WhoisUtil.SELECT_LIST_ERRORMESSAGE + "'"
					+ param.getQ() + "'";
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
		if (keyName.equals("$mul$errormessage")) {
			fliedName = "Error_Code";
		} else {
			fliedName = WhoisUtil.HANDLE;
		}
		return fliedName;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.ERRORMSG;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.ERRORMSG.equals(queryType);
	}

	@Override
	protected boolean supportJoinType(QueryType queryType,
			QueryJoinType queryJoinType) {
		return false;
	}

	@Override
	public Object querySpecificJoinTable(String key, String handle,
			String role, Connection connection, String format)
			throws SQLException {
		throw new UnsupportedOperationException();
	}
}