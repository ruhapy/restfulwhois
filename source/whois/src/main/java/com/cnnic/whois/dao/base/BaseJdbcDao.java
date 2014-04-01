package com.cnnic.whois.dao.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Component;
/**
 * jdbc dao
 * @author nic
 *
 */
@Component
public class BaseJdbcDao {
	
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/**
	 * set jdbc template
	 * @param jdbcTemplate:jdbc template
	 */
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * query for object
	 * @param sql:query sql
	 * @param args:query params
	 * @param rowMapper:result
	 * @return query result
	 * @throws DataAccessException
	 */
	public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
		List<T> results = this.jdbcTemplate.query(sql, args, new RowMapperResultSetExtractor<T>(rowMapper, 1));
		return DataAccessUtils.singleResult(results);
	}
	
	/**
	 * query for object
	 * @param sql:query sql
	 * @param rowMapper:result
	 * @return query result
	 * @throws DataAccessException
	 */
	public <T> T queryForObject(String sql, RowMapper<T> rowMapper) throws DataAccessException {
		List<T> results = this.jdbcTemplate.query(sql, rowMapper);
		return DataAccessUtils.singleResult(results);
	}
}
