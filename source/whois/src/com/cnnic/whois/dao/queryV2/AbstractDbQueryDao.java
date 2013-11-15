package com.cnnic.whois.dao.queryV2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.index.DomainIndex;
import com.cnnic.whois.bean.index.EntityIndex;
import com.cnnic.whois.bean.index.Index;
import com.cnnic.whois.bean.index.NameServerIndex;
import com.cnnic.whois.bean.index.SearchCondition;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.service.DomainIndexService;
import com.cnnic.whois.service.EntityIndexService;
import com.cnnic.whois.service.NameServerIndexService;
import com.cnnic.whois.service.QueryService;
import com.cnnic.whois.service.index.SearchResult;
import com.cnnic.whois.util.PermissionCache;
import com.cnnic.whois.util.WhoisUtil;

public abstract class AbstractDbQueryDao implements QueryDao{
//	private static AbstractDbQueryDao queryDAO = new AbstractDbQueryDao();
	protected DataSource ds;
	protected PermissionCache permissionCache = PermissionCache
			.getPermissionCache();
	protected DomainIndexService domainIndexService = DomainIndexService.getIndexService();
	protected NameServerIndexService nameServerIndexService = NameServerIndexService.getIndexService();
	protected EntityIndexService entityIndexService = EntityIndexService.getIndexService();
	protected List<AbstractDbQueryDao> dbQueryDaos;
	
	/**
	 * Connect to the datasource in the constructor
	 * 
	 * @throws IllegalStateException
	 */
	public AbstractDbQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super();
		this.dbQueryDaos = dbQueryDaos;
		try {
			InitialContext ctx = new InitialContext();
			ds = (DataSource) ctx.lookup(WhoisUtil.JNDI_NAME);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}
	}

	/**
	 * Get QueryDAO objects
	 * 
	 * @return QueryDAO objects
	 */
//	public static AbstractDbQueryDao getQueryDAO() {
//		return queryDAO;
//	}

	
	
	public Map<String, Object> fuzzyQueryEntity(String fuzzyQueryParamName,String queryPara,
			String role, String format, PageBean page)
			throws QueryException, SQLException {
		SearchResult<EntityIndex> result = entityIndexService
				.fuzzyQueryEntitiesByHandleAndName(fuzzyQueryParamName,queryPara,page);
		String selectSql = WhoisUtil.SELECT_LIST_RIRENTITY;
		Connection connection = null;
		Map<String, Object> map = null;
		try {
			connection = ds.getConnection();
			Map<String, Object> entityMap = fuzzyQuery(connection, result,selectSql,"$mul$entity", role, format);
			if(entityMap != null){
				map = rdapConformance(map);
				map.putAll(entityMap);
				addTruncatedParamToMap(map, result);
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
	
	public Map<String, Object> fuzzyQueryNameServer(String queryInfo, String role, 
			String format, PageBean page)
			throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;
		SearchCondition searchCondition = new SearchCondition(queryInfo);
		int startPage = page.getCurrentPage() - 1;
		startPage = startPage >= 0 ? startPage : 0;
		int start = startPage * page.getMaxRecords();
		searchCondition.setStart(start);
		searchCondition.setRow(page.getMaxRecords());
		SearchResult<NameServerIndex> result = nameServerIndexService.queryNameServers(searchCondition);
		page.setRecordsCount(Long.valueOf(result.getTotalResults()).intValue());
		try {
			connection = ds.getConnection();
			String selectSql = WhoisUtil.SELECT_LIST_NAMESREVER + "'"
					+ queryInfo + "'";
			Map<String, Object> nsMap = fuzzyQuery(connection, result,selectSql,
					"$mul$nameServer", role, format);
			if(nsMap != null){
				map = rdapConformance(map);
				map.putAll(nsMap);
				addTruncatedParamToMap(map, result);
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

	public Map<String, Object> fuzzyQueryDoamin(String domain, String domainPuny, String role, 
			String format, PageBean page)
			throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;
		SearchCondition searchCondition = new SearchCondition("ldhName:"+domainPuny + " OR unicodeName:"+domain);
		int startPage = page.getCurrentPage() - 1;
		startPage = startPage >= 0 ? startPage : 0;
		int start = startPage * page.getMaxRecords();
		searchCondition.setStart(start);
		searchCondition.setRow(page.getMaxRecords());
		SearchResult<DomainIndex> result = domainIndexService.queryDomains(searchCondition);
		page.setRecordsCount(Long.valueOf(result.getTotalResults()).intValue());
		if(result.getResultList().size()==0){
			return map;
		}
		try {
			connection = ds.getConnection();
			String sql = WhoisUtil.SELECT_LIST_DNRDOMAIN;
			DomainIndex domainIndex = result.getResultList().get(0);
			if("rirDomain".equals(domainIndex.getDocType())){
				sql = WhoisUtil.SELECT_LIST_RIRDOMAIN;
			}
			Map<String, Object> domainMap = this.fuzzyQuery(connection, result,sql,"$mul$domains",
					role, format);
			if(domainMap != null){
				map =  rdapConformance(map);
				map.putAll(domainMap);
				addTruncatedParamToMap(map, result);
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

	private void addTruncatedParamToMap(Map<String, Object> map,
			SearchResult<? extends Index> result) {
		if(result.getTotalResults()>QueryService.MAX_SIZE_FUZZY_QUERY){
			map.put(WhoisUtil.SEARCH_RESULTS_TRUNCATED_EKEY, true);
		}
	}
	
	protected Map<String, Object> fuzzyQuery(Connection connection, SearchResult<? extends Index> domains,
			String selectSql,String keyName, String role, String format)
			throws SQLException {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for(Index index:domains.getResultList()){
				index.initPropValueMap();
				List<String> keyFlieds = permissionCache.getKeyFiledsByClass(index, role);
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				for (int i = 0; i < keyFlieds.size(); i++) {
					Object resultsInfo = null;
					if (keyFlieds.get(i).startsWith(WhoisUtil.ARRAYFILEDPRX)) {
						String key = keyFlieds.get(i).substring(WhoisUtil.ARRAYFILEDPRX.length());
						resultsInfo = index.getPropValue(key);
						String[] values = null;
						if (resultsInfo != null) {
							values = resultsInfo.toString().split(WhoisUtil.VALUEARRAYPRX);
						}
						map.put(WhoisUtil.getDisplayKeyName(key, format), values);
					} else if (keyFlieds.get(i).startsWith(WhoisUtil.EXTENDPRX)) {
						resultsInfo = index.getPropValue(keyFlieds.get(i));
						map.put(keyFlieds.get(i).substring(WhoisUtil.EXTENDPRX.length()), resultsInfo);
					} else if (keyFlieds.get(i).startsWith(WhoisUtil.JOINFILEDPRX)) {
						String key = keyFlieds.get(i).substring(WhoisUtil.JOINFILEDPRX.length());
						Object value = queryJoinTable(keyFlieds.get(i),
								index.getHandle(), selectSql, role,
								connection, format);
						if (value != null)
							map.put(key, value);
					} else {
						resultsInfo = index.getPropValue(keyFlieds.get(i));
						resultsInfo = resultsInfo==null?"":resultsInfo;
						CharSequence id = "id";
						if(!keyName.equals(WhoisUtil.JOINPUBLICIDS) && WhoisUtil.getDisplayKeyName(keyFlieds.get(i), format).substring(keyFlieds.get(i).length() - 2).equals(id) && !format.equals("application/html")){
							continue;
						}else{
							map.put(WhoisUtil.getDisplayKeyName(keyFlieds.get(i), format), resultsInfo);//a different format have different name;
						}
					}
				}
				handleIpWhenQueryNs(keyName, format, map);
				//vcard format
				if(keyName.equals(WhoisUtil.MULTIPRXENTITY)){
					map = WhoisUtil.toVCard(map, format);
				}
				list.add(map);
			}
			if (list.size() == 0){
				return null;
			}
			Map<String, Object> mapInfo = new LinkedHashMap<String, Object>();
			if (list.size() > 1) {
				mapInfo.put(keyName, list.toArray());
			} else {
				mapInfo = list.get(0);
			}
			return mapInfo;
	}

	

	
	/**
	 * Generate an error map collection
	 * 
	 * @param errorCode
	 * @param title
	 * @param description
	 * @return map
	 */
	public Map<String, Object> getErrorMessage(String errorCode, String role, String format)
			throws QueryException {
		Connection connection = null;
		Map<String, Object> map = null;
		try {
			connection = ds.getConnection();

			String selectSql = WhoisUtil.SELECT_LIST_ERRORMESSAGE + "'"
					+ errorCode + "'";
			Map<String, Object> errorMessageMap = query(connection, selectSql,
					permissionCache.getErrorMessageKeyFileds(role),
					"$mul$errormessage", role, format);
			if(errorMessageMap != null){
				map =  rdapConformance(map);
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
	
	


	/**
	 * According to the table field collections and SQL to obtain the
	 * corresponding data information
	 * 
	 * @param connection
	 * @param sql
	 * @param keyFlieds
	 * @param keyName
	 * @param role
	 * @return map collection
	 * @throws SQLException
	 */
	protected Map<String, Object> query(Connection connection, String sql,
			List<String> keyFlieds, String keyName, String role, String format)
			throws SQLException {
		PreparedStatement stmt = null; 
		ResultSet results = null;

		try {
			stmt = connection.prepareStatement(sql);
			results = stmt.executeQuery();

			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			
			while (results.next()) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				for (int i = 0; i < keyFlieds.size(); i++) {
					Object resultsInfo = null;
					
					String field = keyFlieds.get(i);
					if (field.startsWith(WhoisUtil.ARRAYFILEDPRX)) {
						String key = field.substring(WhoisUtil.ARRAYFILEDPRX.length());
						resultsInfo = results.getString(key);
						String[] values = null;
						if (resultsInfo != null) {
							values = resultsInfo.toString().split(WhoisUtil.VALUEARRAYPRX);
						}
						map.put(WhoisUtil.getDisplayKeyName(key, format), values);
					} else if (field.startsWith(WhoisUtil.EXTENDPRX)) {
						resultsInfo = results.getString(field);
						map.put(field.substring(WhoisUtil.EXTENDPRX.length()), resultsInfo);
					} else if (field.startsWith(WhoisUtil.JOINFILEDPRX)) {
						String fliedName = "";
						if (keyName.equals(WhoisUtil.MULTIPRXNOTICES) || keyName.equals(WhoisUtil.MULTIPRXREMARKS)) {
							fliedName = keyName.substring(WhoisUtil.MULTIPRX.length()) + "Id";
						} else if(keyName.equals(WhoisUtil.JOINNANOTICES) || keyName.equals(WhoisUtil.JOINREMARKS)){
							fliedName = keyName.substring(WhoisUtil.JOINFILEDPRX.length()) + "Id";
						}else if (keyName.equals("$mul$errormessage")){
							fliedName = "Error_Code";
						}else if (keyName.equals(WhoisUtil.JOINSECUREDNS) || keyName.equals("$mul$secureDNS")){
							fliedName = "SecureDNSID";
						}else if (keyName.equals(WhoisUtil.JOINDSDATA) || keyName.equals("$mul$dsData")){
							fliedName = "DsDataID";
						}else if (keyName.equals(WhoisUtil.JOINKEYDATA) || keyName.equals("$mul$keyData")){
							fliedName = "KeyDataID";
						}else {
							fliedName = WhoisUtil.HANDLE;
						}
						

						String key = field.substring(WhoisUtil.JOINFILEDPRX.length());
						Object value = queryJoinTable(field,
								results.getString(fliedName), sql, role,
								connection, format);
						if (value != null)
							map.put(key, value);
					} else {
						preHandleNormalField(keyName, format, results, map, field);
						resultsInfo = results.getObject(field) == null ? "": results.getObject(field);
						
						CharSequence id = "id";
						boolean fieldEndwithId = WhoisUtil.getDisplayKeyName(field, format).substring(field.length() - 2).equals(id);
						if(fieldEndwithId && !format.equals("application/html")){
							continue;
						}else{
							map.put(WhoisUtil.getDisplayKeyName(field, format), resultsInfo);//a different format have different name;
						}
					}
				}
				
				map = postHandleFields(keyName, format, results, map);
				list.add(map);
			}

			if (list.size() == 0)
				return null;
			
			Map<String, Object> mapInfo = new LinkedHashMap<String, Object>();
			
			// link , remark and notice change to array
			if(keyName.equals(WhoisUtil.JOINLINKFILED)|| 
					keyName.equals(WhoisUtil.JOINNANOTICES) ||
					keyName.equals(WhoisUtil.JOINREMARKS) ||
					keyName.equals(WhoisUtil.MULTIPRXLINK ) ||
//					keyName.equals(WhoisUtil.MULTIPRXNOTICES )||
					keyName.equals(WhoisUtil.MULTIPRXREMARKS) ||
					keyName.equals(WhoisUtil.JOINPUBLICIDS) ||
					keyName.equals(WhoisUtil.JOINDSDATA)||
					keyName.equals(WhoisUtil.JOINKEYDATA)){
				mapInfo.put(keyName, list.toArray());
			}else{
				if (list.size() > 1) {
					mapInfo.put(keyName, list.toArray());
				} else {
					mapInfo = list.get(0);
				}
			}
			return mapInfo;
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

	protected Map<String, Object> postHandleFields(String keyName, String format,
			ResultSet results, Map<String, Object> map) throws SQLException {
		return map;
	}

	protected void preHandleNormalField(String keyName, String format,
			ResultSet results, Map<String, Object> map, String field)
			throws SQLException {}

	private void handleIpWhenQueryNs(String keyName, String format,
			Map<String, Object> map) {
		if (keyName.equals("$mul$nameServer") || keyName.equals("$join$nameServer")){
			Map<String, Object> map_IP = new LinkedHashMap<String, Object>();
			Object IPAddressArray = map.get(WhoisUtil.getDisplayKeyName("IPV4_Addresses", format));
			map_IP.put(WhoisUtil.IPV4PREFIX, IPAddressArray);
			IPAddressArray = map.get(WhoisUtil.getDisplayKeyName("IPV6_Addresses", format));
			map_IP.put(WhoisUtil.IPV6PREFIX, IPAddressArray);
			map.put(WhoisUtil.IPPREFIX, map_IP);
			map.remove(WhoisUtil.getDisplayKeyName("IPV4_Addresses", format));
			map.remove(WhoisUtil.getDisplayKeyName("IPV6_Addresses", format));
		}
	}
	
	/**
	 * Determine the different types of schedule and query information according
	 * to the parameters
	 * 
	 * @param key
	 * @param handle
	 * @param sql
	 * @param role
	 * @param connection
	 * @return Returns the schedule information content
	 * @throws SQLException
	 */
	public Object queryJoinTable(String key, String handle, String sql,
			String role, Connection connection, String format) throws SQLException {
		if (key.equals(WhoisUtil.JOINENTITESFILED)) {
			String entitysql = WhoisUtil.SELECT_JOIN_LIST_JOINDNRENTITY;
			if (sql.indexOf("ip") >= 0 || sql.indexOf("autnum") >= 0
					|| sql.indexOf("RIRDomain") >= 0) {
				entitysql = WhoisUtil.SELECT_JOIN_LIST_JOINRIRENTITY;
				return querySpecificJoinTable(key, handle, entitysql, role,
						connection, permissionCache.getRIREntityKeyFileds(role), format);
			}else{
				return querySpecificJoinTable(key, handle, entitysql, role,
						connection, permissionCache.getDNREntityKeyFileds(role), format);
			}
			
		} else if (key.equals(WhoisUtil.JOINLINKFILED)) {
			return querySpecificJoinTable(key, handle,
					WhoisUtil.SELECT_JOIN_LIST_LINK, role, connection,
					permissionCache.getLinkKeyFileds(role), format);
		} else if (key.equals(WhoisUtil.JOINPHONFILED)) {
			return querySpecificJoinTable(key, handle,
					WhoisUtil.SELECT_JOIN_LIST_PHONE, role, connection,
					permissionCache.getPhonesKeyFileds(role), format);
		} else if (key.equals(WhoisUtil.JOINPOSTATLADDRESSFILED)) {
			return querySpecificJoinTable(key, handle,
					WhoisUtil.SELECT_JOIN_LIST_POSTALADDRESS, role, connection,
					permissionCache.getPostalAddressKeyFileds(role), format);
		} else if (key.equals(WhoisUtil.JOINVARIANTS)) {
			return querySpecificJoinTable(key, handle,
					WhoisUtil.SELECT_JOIN_LIST_VARIANTS, role, connection,
					permissionCache.getVariantsKeyFileds(role), format);
		} else if (key.equals(WhoisUtil.JOINDALEGATIONKEYS)) {
			return querySpecificJoinTable(key, handle,
					WhoisUtil.SELECT_JOIN_LIST_DELEGATIONKEYS, role,
					connection, permissionCache.getDelegationKeyFileds(role), format);
		} else if (key.equals(WhoisUtil.JOINNAMESERVER)) {
			return querySpecificJoinTable(key, handle,
					WhoisUtil.SELECT_JOIN_LIST_JOINNAMESERVER, role,
					connection, permissionCache.getNameServerKeyFileds(role), format);
		} else if (key.equals(WhoisUtil.JOINNAREGISTRAR)) {
			return querySpecificJoinTable(key, handle,
					WhoisUtil.SELECT_JOIN_LIST_REGISTRAR, role, connection,
					permissionCache.getRegistrarKeyFileds(role), format);
		} else if (key.equals(WhoisUtil.JOINNANOTICES)) {
			return querySpecificJoinTable(key, handle,
					WhoisUtil.SELECT_JOIN_LIST_NOTICES, role, connection,
					permissionCache.getNoticesKeyFileds(role), format);
		} else if (key.equals(WhoisUtil.JOINEVENTS)) {
			return querySpecificJoinTable(key, handle,
					WhoisUtil.SELECT_JOIN_LIST_EVENTS, role, connection,
					permissionCache.getEventsKeyFileds(role), format);
		} else if (key.equals(WhoisUtil.JOINREMARKS)) {
			return querySpecificJoinTable(key, handle,
					WhoisUtil.SELECT_JOIN_LIST_REMARKS, role, connection,
					permissionCache.getRemarksKeyFileds(role), format);
		}else if (key.equals(WhoisUtil.JOINPUBLICIDS)) {
			return querySpecificJoinTable(key, handle,
					WhoisUtil.SELECT_JOIN_LIST_PUBLICIDS, role, connection,
					permissionCache.getPublicIdsKeyFileds(role), format);
		}else if (key.equals(WhoisUtil.JOINSECUREDNS)) {
			return querySpecificJoinTable(key, handle,
					WhoisUtil.SELECT_JOIN_LIST_SECUREDNS, role, connection,
					permissionCache.getSecureDNSMapKeyFileds(role), format);
		}else if (key.equals(WhoisUtil.JOINDSDATA)) {
			return querySpecificJoinTable(key, handle,
					WhoisUtil.SELECT_JOIN_LIST_DSDATA, role, connection,
					permissionCache.getDsDataMapKeyFileds(role), format);
		}else if (key.equals(WhoisUtil.JOINKEYDATA)) {
			return querySpecificJoinTable(key, handle,
					WhoisUtil.SELECT_JOIN_LIST_KEYDATA, role, connection,
					permissionCache.getKeyDataMapKeyFileds(role), format);
		}

		return null;
	}

	/**
	 * query schedule information
	 * 
	 * @param key
	 * @param handle
	 * @param sql
	 * @param role
	 * @param connection
	 * @param keyFlieds
	 * @return Returns the schedule information content
	 * @throws SQLException
	 */
	public Object querySpecificJoinTable(String key, String handle, String sql,
			String role, Connection connection, List<String> keyFlieds,
			String format)
			throws SQLException {

		Map<String, Object> map = query(connection, sql + "'" + handle + "'",
				keyFlieds, key, role, format);
		if (map != null) {
			if (null == map.get(key)) {
				return map;
			} else if (!map.isEmpty()) {
				return map.get(key);
			}
		}
		return null;
	}

	protected Map<String, Object> rdapConformance(Map<String, Object> map){
		if(map == null){
			map = new LinkedHashMap<String, Object>();
		}
		Object[] conform = new Object[1];
		conform[0] = WhoisUtil.RDAPCONFORMANCE;
		map.put(WhoisUtil.RDAPCONFORMANCEKEY, conform);
		return map;
	}
}
