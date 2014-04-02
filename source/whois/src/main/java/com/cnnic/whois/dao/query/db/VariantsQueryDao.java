package com.cnnic.whois.dao.query.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cnnic.whois.bean.QueryJoinType;
import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
import com.cnnic.whois.execption.QueryException;
import com.cnnic.whois.util.WhoisUtil;
/**
 * variants query dao
 * @author nic
 *
 */
@Repository
public class VariantsQueryDao extends AbstractDbQueryDao {
	public static final String GET_ALL_VARIANTS = "select * from variants ";

	@Override
	public Map<String, Object> query(QueryParam param)
			throws QueryException {
		Map<String, Object> map = null;
		try {
			String selectSql = WhoisUtil.SELECT_LIST_VARIANTS + "'"
					+ param.getQ() + "'";
			map = query(selectSql, columnCache
					.getVariantsKeyFileds(), "$mul$variants");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new QueryException(e);
		}
		return map;
	}

	@Override
	public Map<String, Object> getAll() throws QueryException {
		Map<String, Object> map = null;
		try {
			map = query(GET_ALL_VARIANTS, columnCache.getVariantsKeyFileds(), "$mul$variants");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new QueryException(e);
		}
		return map;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.VARIANTS;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.VARIANTS.equals(queryType);
	}

	@Override
	protected boolean supportJoinType(QueryType queryType,
			QueryJoinType queryJoinType) {
		return QueryJoinType.VARIANTS.equals(queryJoinType);
	}

	@Override
	public Object querySpecificJoinTable(String key, String handle) throws SQLException {
		return querySpecificJoinTable(key, handle,
				WhoisUtil.SELECT_JOIN_LIST_VARIANTS, columnCache.getVariantsKeyFileds());
	}

	@Override
	public List<String> getKeyFields(String role) {
		return permissionCache.getVariantsKeyFileds(role);
	}
}
