package com.cnnic.whois.dao.query.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cnnic.whois.bean.QueryJoinType;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.WhoisUtil;
/**
 * dnr entity query dao
 * @author nic
 *
 */
@Repository
public class DnrEntityQueryDao extends EntityQueryDao {

	@Override
	public QueryType getQueryType() {
		return QueryType.DNRENTITY;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.DNRENTITY.equals(queryType);
	}

	@Override
	protected boolean supportJoinType(QueryType queryType,
			QueryJoinType queryJoinType) {
		if (!QueryJoinType.ENTITIES.equals(queryJoinType)) {
			return false;
		}
		if (RirEntityQueryDao.joinRirEntity(queryType)) {
			return false;
		}
		return true;
	}

	@Override
	public Map<String, Object> getAll() throws QueryException {
		List<String> keyFields = columnCache
				.getDNREntityKeyFileds();
		return getAllEntity(GET_ALL_DNRENTITY, keyFields);
	}
	
	@Override
	public Object querySpecificJoinTable(String key, String handle)
			throws SQLException {
		return querySpecificJoinTable(key, handle,
				WhoisUtil.SELECT_JOIN_LIST_JOINDNRENTITY,
				columnCache.getDNREntityKeyFileds());
	}
	
	@Override
	public List<String> getKeyFields(String role) {
		return permissionCache.getDNREntityKeyFileds(role);
	}
}
