package com.cnnic.whois.dao.query.db;

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
/**
 * phone  query dao
 * @author nic
 *
 */
@Repository
public class PhonesQueryDao extends AbstractDbQueryDao {

	@Override
	public Map<String, Object> query(QueryParam param) throws QueryException {
		Map<String, Object> map = null;
		try {
			String selectSql = WhoisUtil.SELECT_LIST_PHONE + "'" + param.getQ()
					+ "'";
			map = query(selectSql, columnCache
					.getPhonesKeyFileds(), "$mul$phones");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new QueryException(e);
		}
		return map;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.PHONES;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.PHONES.equals(queryType);
	}

	@Override
	protected boolean supportJoinType(QueryType queryType,
			QueryJoinType queryJoinType) {
		return QueryJoinType.PHONES.equals(queryJoinType);
	}

	@Override
	public Object querySpecificJoinTable(String key, String handle) throws SQLException {
		return querySpecificJoinTable(key, handle,
				WhoisUtil.SELECT_JOIN_LIST_PHONE, columnCache.getPhonesKeyFileds());
	}

	@Override
	public List<String> getKeyFields(String role) {
		return permissionCache.getPhonesKeyFileds(role);
	}
}
