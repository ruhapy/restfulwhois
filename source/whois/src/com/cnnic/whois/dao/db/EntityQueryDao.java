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
import com.cnnic.whois.util.ColumnCache;
import com.cnnic.whois.util.WhoisUtil;

public class EntityQueryDao extends AbstractSearchQueryDao {
	private static final String MAP_KEY = "$mul$entity";
	public static final String GET_ALL_DNRENTITY = "select * from DNREntity limit 1";
	public static final String GET_ALL_RIRENTITY = "select * from RIREntity limit 0 ";
	protected EntityIndexService entityIndexService = EntityIndexService
			.getIndexService();

	public EntityQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}

	@Override
	public Map<String, Object> query(QueryParam param,
			PageBean... page) throws QueryException {
		SearchResult<EntityIndex> result = entityIndexService
				.preciseQueryEntitiesByHandleOrName(param.getQ());
		String selectSql = WhoisUtil.SELECT_LIST_RIRENTITY;
		Connection connection = null;
		Map<String, Object> map = null;
		try {
			connection = ds.getConnection();
			map = fuzzyQuery(connection, result, selectSql, MAP_KEY);
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
					ColumnCache.getColumnCache().getDNREntityKeyFileds(), MAP_KEY,
					format);
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
					ColumnCache.getColumnCache().getRIREntityKeyFileds(), MAP_KEY,
					 format);
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
			ResultSet results, Map<String, Object> map)
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
			map = WhoisUtil.toVCard(map);
		}
		return map;
	}

	private Map<String, Object> getAllDNREntity(String role)
			throws QueryException {
		String sql = GET_ALL_DNRENTITY;
		List<String> keyFields = permissionCache.getDNREntityKeyFileds(role);
		return getAllEntity(role, sql, keyFields);
	}

	private Map<String, Object> getAllRIREntity(String role)
			throws QueryException {
		String sql = GET_ALL_RIRENTITY;
		List<String> keyFields = permissionCache.getRIREntityKeyFileds(role);
		return getAllEntity(role, sql, keyFields);
	}

	private Map<String, Object> getAllEntity(String role, String sql,
			List<String> keyFields) throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;
		try {
			connection = ds.getConnection();
			Map<String, Object> entityMap = query(connection, sql, keyFields,
					MAP_KEY, role);
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
	public Map<String, Object> getAll(String role) throws QueryException {
		Map<String, Object> allDnrEntity = getAllDNREntity(role);
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		getListFromMap(allDnrEntity, mapList);
		Map<String, Object> allRirEntity = this.getAllRIREntity(role);
		getListFromMap(allRirEntity, mapList);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(MAP_KEY, mapList.toArray());
		return result;
	}

	private void getListFromMap(Map<String, Object> allEntity,
			List<Map<String, Object>> mapList) {
		if(null == allEntity){
			return ;
		}
		if(null != allEntity.get("Handle")){// only one result
			mapList.add(allEntity);
		}else{
			Object[] entities = (Object[]) allEntity.get(MAP_KEY);
			for(Object entity: entities){
				mapList.add((Map<String, Object>)entity);
			}
		}
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