package com.cnnic.whois.dao.query;

import java.util.List;
import java.util.Map;

import com.cnnic.whois.bean.PageBean;
import com.cnnic.whois.bean.QueryJoinType;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.bean.index.Index;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.service.DomainIndexService;
import com.cnnic.whois.service.NameServerIndexService;
import com.cnnic.whois.util.WhoisUtil;

public class FuzzyQueryDAO extends DbQueryDAO {
	protected DomainIndexService domainIndexService = DomainIndexService
			.getIndexService();
	protected NameServerIndexService nameServerIndexService = NameServerIndexService
			.getIndexService();

	protected void handleFieldsFuzzy(String keyName, String role,
			String format, Index index, List<String> keyFlieds,
			Map<String, Object> map) {
		for (int i = 0; i < keyFlieds.size(); i++) {
			Object resultsInfo = null;
			String field = keyFlieds.get(i);
			if (field.startsWith(WhoisUtil.ARRAYFILEDPRX)) {
				String key = field.substring(WhoisUtil.ARRAYFILEDPRX.length());
				resultsInfo = index.getPropValue(key);
				String[] values = null;
				if (resultsInfo != null) {
					values = resultsInfo.toString().split(
							WhoisUtil.VALUEARRAYPRX);
				}
				map.put(WhoisUtil.getDisplayKeyName(key, format), values);
			} else if (field.startsWith(WhoisUtil.EXTENDPRX)) {
				resultsInfo = index.getPropValue(field);
				map.put(field.substring(WhoisUtil.EXTENDPRX.length()),
						resultsInfo);
			} else if (field.startsWith(WhoisUtil.JOINFILEDPRX)) {
				QueryType queryType = getQueryType();
				queryJoinEntity(queryType, index.getHandle(), role, format,
						map, field);
			} else {
				resultsInfo = index.getPropValue(field);
				resultsInfo = resultsInfo == null ? "" : resultsInfo;
				CharSequence id = "id";
				boolean fieldEndwithId = WhoisUtil
						.getDisplayKeyName(field, format)
						.substring(field.length() - 2).equals(id);
				if (fieldEndwithId && !format.equals("application/html")) {
					continue;
				} else {
					map.put(WhoisUtil.getDisplayKeyName(field, format),
							resultsInfo);
				}
			}
		}
	}

	@Override
	public QueryType getQueryType() {
		return null;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportJoinType(QueryType queryType,
			QueryJoinType queryJoinType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, Object> query(String q, String role, String format,
			PageBean... page) throws QueryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> queryJoins(String handle, String role,
			String format) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getJoinFieldIdColumnName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, Object> postHandleFuzzyField(Map<String, Object> map,
			String format) {
		// TODO Auto-generated method stub
		return map;
	}
}