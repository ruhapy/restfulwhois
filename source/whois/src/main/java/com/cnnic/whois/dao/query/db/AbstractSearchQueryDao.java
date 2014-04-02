package com.cnnic.whois.dao.query.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.cnnic.whois.bean.QueryJoinType;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.bean.index.Index;
import com.cnnic.whois.dao.query.search.SearchQueryExecutor;
import com.cnnic.whois.service.QueryService;
import com.cnnic.whois.service.index.SearchResult;
import com.cnnic.whois.util.WhoisUtil;
/**
 * search dao,search data from solr.solr must be init after data changed in db
 * @author nic
 *
 */
public abstract class AbstractSearchQueryDao extends AbstractDbQueryDao{
	@Autowired
	protected SearchQueryExecutor searchQueryExecutor;
	public Object querySpecificJoinTable(String key, String handle)
			throws SQLException{
		throw new UnsupportedOperationException();
	}

	@Override
	protected boolean supportJoinType(QueryType queryType,
			QueryJoinType queryJoinType) {
		return false;
	}
	/**
	 * add truncated param to query result map
	 * @param map:query result map
	 * @param result:query result map after add truncated param
	 */
	protected void addTruncatedParamToMap(Map<String, Object> map,
			SearchResult<? extends Index> result) {
		if(result.getTotalResults()>QueryService.MAX_SIZE_FUZZY_QUERY){
			map.put(WhoisUtil.SEARCH_RESULTS_TRUNCATED_EKEY, true);
		}
	}
	
	/**
	 * fuzzy query domain
	 * @param domains:query string,eg cn*.cn,cn*
	 * @param keyName:query result map key name
	 * @return:query result map 
	 * @throws SQLException
	 */
	protected Map<String, Object> fuzzyQuery(SearchResult<? extends Index> domains,
			String keyName)
			throws SQLException {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for(Index index:domains.getResultList()){
				index.initPropValueMap();
				List<String> keyFlieds = permissionCache.getKeyFiledsByClass(index);
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				putQueryType(map, index);
				for (int i = 0; i < keyFlieds.size(); i++) {
					Object resultsInfo = null;
					String field = keyFlieds.get(i);
					if (field.startsWith(WhoisUtil.ARRAYFILEDPRX)) {
						String key = field.substring(WhoisUtil.ARRAYFILEDPRX.length());
						resultsInfo = index.getPropValue(key);
						String[] values = null;
						if (resultsInfo != null) {
							values = resultsInfo.toString().split(WhoisUtil.VALUEARRAYPRX);
						}
						map.put(key, values);
					} else if (field.startsWith(WhoisUtil.EXTENDPRX)) {
						resultsInfo = index.getPropValue(field);
						map.put(field.substring(WhoisUtil.EXTENDPRX.length()), resultsInfo);
					} else if (field.startsWith(WhoisUtil.JOINFILEDPRX)) {
						String key = field.substring(WhoisUtil.JOINFILEDPRX.length());
						Object value = queryJoinTable(field,
								index.getHandle());
						if (value != null)
							map.put(key, value);
					} else {
						resultsInfo = index.getPropValue(field);
						resultsInfo = resultsInfo==null?"":resultsInfo;
//						CharSequence id = "id";
//						if(!keyName.equals(WhoisUtil.JOINPUBLICIDS) && field.substring(field.length() - 2).equals(id) && !format.equals("application/html")){
//							continue;
//						}else{
						map.put(field, resultsInfo);//a different format have different name;
//						}
					}
				}
				map = postHandleFieldsFuzzy(keyName, map);
				list.add(map);
			}
			if (list.size() == 0){
				return null;
			}
			Map<String, Object> mapInfo = new LinkedHashMap<String, Object>();
			if (StringUtils.isBlank(keyName) && list.size() == 1) {//not multi search: entity query
				mapInfo = list.get(0);
			}else{//multi search
				mapInfo.put(keyName, list.toArray());
			}
			return mapInfo;
	}

	/**
	 * post handle fields when fuzzy query
	 * @param keyName
	 * @param map
	 * @return
	 */
	protected Map<String, Object> postHandleFieldsFuzzy(String keyName,
			 Map<String, Object> map) {
		return map;
	}
	
	/**
	 * put query type to query result,for filter column,will be removed after filter
	 * @param map:query result map
	 * @param index: search index
	 */
	private void putQueryType(Map<String, Object> map,Index index){
		if(map == null){
			return;
		}
		map.put(WhoisUtil.QUERY_TYPE, index.getDocType());
	}
}