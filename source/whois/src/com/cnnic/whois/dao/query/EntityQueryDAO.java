package com.cnnic.whois.dao.query;

import java.util.Map;

import javax.sql.DataSource;

import com.cnnic.whois.bean.QueryJoinType;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.service.EntityIndexService;
import com.cnnic.whois.util.PermissionCache;
import com.cnnic.whois.util.WhoisUtil;

public class EntityQueryDAO extends DbQueryDAO{
	private static QueryType QUERY_TYPE = QueryType.DOMAIN;
	protected DataSource ds;
	protected PermissionCache permissionCache = PermissionCache
			.getPermissionCache();
	protected EntityIndexService entityIndexService = EntityIndexService
			.getIndexService();

	@Override
	public Map<String, Object> query(String q, String role, String format) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.ENTITY.equals(queryType);
	}
	@Override
	public boolean supportJoinType(QueryJoinType queryJoinType) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Map<String, Object> queryJoins(String handle, String role,
			String format) {
		String sql = WhoisUtil.SELECT_JOIN_LIST_JOINDNRENTITY + "'" + handle + "'";
		return null;
	}
	@Override
	public String getJoinFieldIdColumnName() {
		// TODO Auto-generated method stub
		return null;
	}
}
