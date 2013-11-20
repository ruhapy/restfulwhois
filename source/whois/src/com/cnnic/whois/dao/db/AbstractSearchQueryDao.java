package com.cnnic.whois.dao.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.QueryJoinType;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.bean.index.Index;
import com.cnnic.whois.service.QueryService;
import com.cnnic.whois.service.index.SearchResult;
import com.cnnic.whois.util.WhoisUtil;

public abstract class AbstractSearchQueryDao extends AbstractDbQueryDao{
	public Object querySpecificJoinTable(String key, String handle,
			String role, Connection connection, String format)
			throws SQLException{
		throw new UnsupportedOperationException();
	}
	
	public AbstractSearchQueryDao(List<AbstractDbQueryDao> dbQueryDaos) {
		super(dbQueryDaos);
	}
//	public static AbstractDbQueryDao getQueryDAO() {
//		return queryDAO;
//	}
	@Override
	protected boolean supportJoinType(QueryType queryType,
			QueryJoinType queryJoinType) {
		return false;
	}
	protected void addTruncatedParamToMap(Map<String, Object> map,
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
				map = postHandleFieldsFuzzy(keyName, format, map);
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

	protected Map<String, Object> postHandleFieldsFuzzy(String keyName,
			String format, Map<String, Object> map) {
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
		//vcard format
		if(keyName.equals(WhoisUtil.MULTIPRXENTITY)){
			map = WhoisUtil.toVCard(map, format);
		}
		return map;
	}
}