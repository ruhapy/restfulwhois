package com.cnnic.whois.dao.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryJoinType;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.ColumnCache;
import com.cnnic.whois.util.PermissionCache;
import com.cnnic.whois.util.WhoisUtil;

public class HelpQueryDao extends AbstractDbQueryDao {
	public static final String GET_ALL_HELP = "select * from notices ";
	public HelpQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}

	@Override
	public Map<String, Object> query(QueryParam param,
			PageBean... page) throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;
		try {
			connection = ds.getConnection();
			String selectSql = WhoisUtil.SELECT_HELP + "'" + param.getQ() + "'";
			Map<String, Object> helpMap = query(connection, selectSql,
					ColumnCache.getColumnCache().getHelpKeyFields(), "$mul$notices");
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
	
	@Override
	public Map<String, Object> getAll(String role) throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;
		try {
			connection = ds.getConnection();
			Map<String, Object> helpMap = query(connection, GET_ALL_HELP,
					ColumnCache.getColumnCache().getHelpKeyFields(), "$mul$notices");
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

	
	@Override
	protected String getJoinFieldName(String keyName) {
		String fliedName = "";
		if (keyName.equals(WhoisUtil.MULTIPRXNOTICES)) {
			fliedName = keyName.substring(WhoisUtil.MULTIPRX.length()) + "Id";
		} else if (keyName.equals(WhoisUtil.JOINNANOTICES)) {
			fliedName = keyName.substring(WhoisUtil.JOINFILEDPRX.length())
					+ "Id";
		} else {
			fliedName = WhoisUtil.HANDLE;
		}
		return fliedName;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.HELP;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.HELP.equals(queryType);
	}

	@Override
	protected boolean supportJoinType(QueryType queryType,
			QueryJoinType queryJoinType) {
		return false;
	}

	@Override
	public Object querySpecificJoinTable(String key, String handle,
			Connection connection) throws SQLException {
		throw new UnsupportedOperationException();
	}
	@Override
	public List<String> getKeyFields(String role) {
		return PermissionCache.getPermissionCache().getHelpKeyFileds(role);
	}
}