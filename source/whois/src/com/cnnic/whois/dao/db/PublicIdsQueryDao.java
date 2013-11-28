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

public class PublicIdsQueryDao extends AbstractDbQueryDao {

	public PublicIdsQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.PUBLICIDS;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return getQueryType().equals(queryType);
	}

	@Override
	public Map<String, Object> query(QueryParam param, PageBean... page)
			throws QueryException {
		throw new UnsupportedOperationException();
	}

	@Override
	protected boolean supportJoinType(QueryType queryType,
			QueryJoinType queryJoinType) {
		return QueryJoinType.PUBLICIDS.equals(queryJoinType);
	}

	@Override
	public Object querySpecificJoinTable(String key, String handle,
			Connection connection) throws SQLException {
		return querySpecificJoinTable(key, handle,
				WhoisUtil.SELECT_JOIN_LIST_PUBLICIDS, connection, ColumnCache
						.getColumnCache().getPublicIdsKeyFileds());
	}
	@Override
	public List<String> getKeyFields(String role) {
		return PermissionCache.getPermissionCache().getPublicIdsKeyFileds(role);
	}
}