package com.cnnic.whois.dao.query.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.WhoisUtil;
/**
 * dnr domain query dao
 * @author nic
 *
 */
@Repository
public class DnrDomainQueryDao extends AbstractDomainQueryDao {
	@Override
	public Map<String, Object> query(QueryParam param) throws QueryException {
		Map<String, Object> map = null;
		try {
			List<String> keyFields = columnCache
					.getDNRDomainKeyFileds();
			String sql = String.format(WhoisUtil.SELECT_LIST_DNRDOMAIN, param.getQ(),param.getQ());
			Map<String, Object> domainMap = query(
					sql, keyFields, param.getQ());
			if (domainMap != null) {
				map = rdapConformance(map);
				map.putAll(domainMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new QueryException(e);
		}
		return map;
	}

	@Override
	public Map<String, Object> getAll() throws QueryException {
		List<String> dnrKeyFields = columnCache
				.getDNRDomainKeyFileds();
		Map<String, Object> dnrDomains = queryBySql(GET_ALL_DNRDOMAIN,
				dnrKeyFields);
		return dnrDomains;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.DNRDOMAIN.equals(queryType);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.DNRDOMAIN;
	}

	@Override
	public List<String> getKeyFields(String role) {
		return permissionCache.getDNRDomainKeyFileds(role);
	}
}
