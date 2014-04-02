package com.cnnic.whois.dao.query.cache;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cnnic.whois.bean.QueryParam;
import com.cnnic.whois.bean.QueryType;
/**
 * phone query dao
 * @author nic
 *
 */
@Repository("cachePhonesQueryDao")
public class PhonesQueryDao extends AbstractCacheQueryDao {
	@Override
	protected List<String> getCacheKeySplits(QueryParam param) {
		return super.getHandleCacheKeySplits(param);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.PHONES;
	}

	@Override
	public boolean supportType(QueryType queryType) {
		return QueryType.PHONES.equals(queryType);
	}

//	@Override
//	protected boolean needInitCache() {
//		return true;
//	}
//
//	@Override
//	protected void initCache() {
//		super.initCacheWithOneKey("$mul$phones", "phonesId");
//	}
}