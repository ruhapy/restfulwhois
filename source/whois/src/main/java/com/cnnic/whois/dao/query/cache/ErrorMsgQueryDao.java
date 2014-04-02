package com.cnnic.whois.dao.query.cache;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
/**
 * error query dao
 * @author nic
 *
 */
@Repository("cacheErrorMsgQueryDao")
public class ErrorMsgQueryDao extends AbstractCacheQueryDao {
	@Override
	protected List<String> getCacheKeySplits(QueryParam param) {
		return super.getHandleCacheKeySplits(param);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.ERRORMSG;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.ERRORMSG.equals(queryType);
	}

	@Override
	protected boolean needInitCache() {
		return true;
	}

	@Override
	protected void initCache() {
		super.initCacheWithOneKey("$mul$errormessage", "Error_Code");
	}
}