package com.cnnic.whois.dao.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cnnic.whois.bean.QueryJoinType;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.ColumnCache;
import com.cnnic.whois.util.PermissionCache;
import com.cnnic.whois.util.WhoisUtil;
@Repository
public class KeyDataQueryDao extends AbstractDbQueryDao {
	public static final String GET_ALL_KEYDATA = "select * from keyData ";

	@Override
	public Map<String, Object> query(QueryParam param)
			throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;

		try {
			connection = ds.getConnection();
			String selectSql = WhoisUtil.SELECT_LIST_KEYDATA + "'"
					+ param.getQ() + "'";
			map = query(connection, selectSql, ColumnCache.getColumnCache()
					.getKeyDataKeyFileds(), "$mul$keyData");
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
	public Map<String, Object> getAll() throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;
		try {
			connection = ds.getConnection();
			map = query(connection, GET_ALL_KEYDATA, ColumnCache
					.getColumnCache().getKeyDataKeyFileds(), "$mul$keyData");
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
		if (keyName.equals(WhoisUtil.JOINKEYDATA)
				|| keyName.equals("$mul$keyData")) {
			fliedName = "KeyDataID";
		} else {
			fliedName = WhoisUtil.HANDLE;
		}
		return fliedName;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.KEYDATA;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.KEYDATA.equals(queryType);
	}

	@Override
	protected boolean supportJoinType(QueryType queryType,
			QueryJoinType queryJoinType) {
		return QueryJoinType.KEYDATA.equals(queryJoinType);
	}

	@Override
	public Object querySpecificJoinTable(String key, String handle,
			Connection connection) throws SQLException {
		return querySpecificJoinTable(key, handle,
				WhoisUtil.SELECT_JOIN_LIST_KEYDATA, connection, ColumnCache
						.getColumnCache().getKeyDataKeyFileds());
	}

	@Override
	public List<String> getKeyFields(String role) {
		return PermissionCache.getPermissionCache()
				.getKeyDataMapKeyFileds(role);
	}
}
