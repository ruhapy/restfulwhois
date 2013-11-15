package com.cnnic.whois.dao.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryJoinType;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.bean.index.EntityIndex;
import com.cnnic.whois.dao.DbQueryExecutor;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.service.EntityIndexService;
import com.cnnic.whois.service.index.SearchResult;
import com.cnnic.whois.util.WhoisUtil;

public abstract class EntityQueryDAO extends AbstractDbQueryDAO {
	private AbstractSearchQueryDAO searchQueryDAO;
	protected EntityIndexService entityIndexService = EntityIndexService
			.getIndexService();

	public EntityQueryDAO(List<AbstractDbQueryDAO> dbQueryDaos) {
		super(dbQueryDaos);
	}

	@Override
	public Map<String, Object> query(String q, String role, String format,
			PageBean... page) throws QueryException {
		SearchResult<EntityIndex> result = entityIndexService
				.preciseQueryEntitiesByHandleOrName(q);
		Map<String, Object> map = null;
		map = searchQueryDAO.fuzzyQuery(result, "$mul$entity", role, format);
		map = rdapConformance(map);
		return map;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.ENTITY.equals(queryType);
	}

	protected String getJoinSql(String handle) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object queryJoins(String handle, String role, String format) {
		String sql = getJoinSql(handle);
		PreparedStatement stmt = null;
		ResultSet results = null;
		Connection connection = null;
		try {
			connection = ds.getConnection();
			List<String> keyFlieds = permissionCache
					.getDNRDomainKeyFileds(role);
			stmt = connection.prepareStatement(sql);
			results = stmt.executeQuery();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			while (results.next()) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				for (int i = 0; i < keyFlieds.size(); i++) {
					String field = keyFlieds.get(i);
					handleField(role, format, results, map, field);
				}
				String entityHandle = results.getString(WhoisUtil.HANDLE);
				handleAsEventActor(map, entityHandle);
				map = WhoisUtil.toVCard(map, format);
				list.add(map);
			}
			if (list.size() == 0) {
				return null;
			}
			if (list.size() > 1) {
				return list.toArray();
			} else {
				return list.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (results != null) {
				try {
					results.close();
				} catch (SQLException se) {
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException se) {
				}
			}
		}
	}

	private void handleAsEventActor(Map<String, Object> map, String entityHandle) {
		if (map.containsKey("events")) {
			Map<String, Object> map_Events = new LinkedHashMap<String, Object>();
			map_Events = (Map<String, Object>) map.get("events");
			if (map_Events.containsKey("eventActor")) {
				String eventactor = (String) map_Events.get("eventActor");
				if (entityHandle.equals(eventactor)) {
					map_Events.remove("eventActor");
					List<Map<String, Object>> listEvents = new ArrayList<Map<String, Object>>();
					listEvents.add(map_Events);
					map.put("asEventActor", listEvents.toArray());
					map.remove("events");
				}
			}
		}
	}

	@Override
	public String getJoinFieldIdColumnName() {
		return WhoisUtil.HANDLE;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.ENTITY;
	}

	@Override
	public boolean supportJoinType(QueryType queryType,
			QueryJoinType queryJoinType) {
		return false;
	}
}
