package com.cnnic.whois.dao.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.bean.index.EntityIndex;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.service.EntityIndexService;
import com.cnnic.whois.service.index.SearchResult;
import com.cnnic.whois.util.WhoisUtil;

public class EntityQueryDao extends AbstractSearchQueryDao {
	public static final String GET_ALL_DNRENTITY = "select * from DNREntity ";
	public static final String GET_ALL_RIRENTITY = "select * from RIREntity ";
	protected EntityIndexService entityIndexService = EntityIndexService
			.getIndexService();

	public EntityQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}

	@Override
	public Map<String, Object> query(QueryParam param, String role,
			String format, PageBean... page) throws QueryException {
		SearchResult<EntityIndex> result = entityIndexService
				.preciseQueryEntitiesByHandleOrName(param.getQ());
		String selectSql = WhoisUtil.SELECT_LIST_RIRENTITY;
		Connection connection = null;
		Map<String, Object> map = null;
		try {
			connection = ds.getConnection();
			map = fuzzyQuery(connection, result, selectSql, "$mul$entity",
					role, format);
			map = rdapConformance(map);
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

	@SuppressWarnings("unused")
	private Map<String, Object> queryDNREntity(String queryInfo, String role,
			String format) throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;

		try {
			connection = ds.getConnection();
			String selectSql = WhoisUtil.SELECT_LIST_DNRENTITY + "'"
					+ queryInfo + "'";
			Map<String, Object> entityMap = query(connection, selectSql,
					permissionCache.getDNREntityKeyFileds(role), "$mul$entity",
					role, format);
			if (entityMap != null) {
				map = rdapConformance(map);
				map.putAll(entityMap);
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

	@SuppressWarnings("unused")
	private Map<String, Object> queryRIREntity(String queryInfo, String role,
			String format) throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;

		try {
			connection = ds.getConnection();
			String selectSql = WhoisUtil.SELECT_LIST_RIRENTITY + "'"
					+ queryInfo + "'";
			Map<String, Object> entityMap = query(connection, selectSql,
					permissionCache.getRIREntityKeyFileds(role), "$mul$entity",
					role, format);
			if (entityMap != null) {
				map = rdapConformance(map);
				map.putAll(entityMap);
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

	protected Map<String, Object> postHandleFields(String keyName,
			String format, ResultSet results, Map<String, Object> map)
			throws SQLException {
		// asevent
		if (keyName.equals(WhoisUtil.JOINENTITESFILED)) {
			String entityHandle = results.getString(WhoisUtil.HANDLE);
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
		// vcard format
		if (keyName.equals(WhoisUtil.JOINENTITESFILED)
				|| keyName.equals(WhoisUtil.MULTIPRXENTITY)) {
			map = WhoisUtil.toVCard(map, format);
		}
		return map;
	}

	private Map<String, Object> getAllDNREntity(String role, String format)
			throws QueryException {
		String sql = GET_ALL_DNRENTITY;
		List<String> keyFields = permissionCache.getDNREntityKeyFileds(role);
		return getAllEntity(role, format, sql, keyFields);
	}

	private Map<String, Object> getAllRIREntity(String role, String format)
			throws QueryException {
		String sql = GET_ALL_RIRENTITY;
		List<String> keyFields = permissionCache.getRIREntityKeyFileds(role);
		return getAllEntity(role, format, sql, keyFields);
	}

	private Map<String, Object> getAllEntity(String role, String format,
			String sql, List<String> keyFields) throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;
		try {
			connection = ds.getConnection();
			Map<String, Object> entityMap = query(connection, sql, keyFields,
					"$mul$entity", role, format);
			if (entityMap != null) {
				map = rdapConformance(map);
				map.putAll(entityMap);
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
	public Map<String, Object> getAll(String role, String format)
			throws QueryException {
		Map<String, Object> allDnrEntity = getAllDNREntity(role, format);
		Map<String, Object> allRirEntity = this.getAllRIREntity(role, format);
		Map<String, Object> result = new HashMap<String, Object>();
		result.putAll(allDnrEntity);
		result.putAll(allRirEntity);
		return result;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.ENTITY;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.ENTITY.equals(queryType);
	}
}