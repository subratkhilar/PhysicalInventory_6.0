package com.pi.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.pi.model.Costing;

@Repository("costingDao")
public class CostingDaoImpl implements CostingDao {

	private String sql = null;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Costing> getAllStores() {
		sql = "SELECT * FROM PHY_INV_STORE";
		return jdbcTemplate.query(sql, new ResultSetExtractor<List<Costing>>() {

			@Override
			public List<Costing> extractData(ResultSet rst)
					throws SQLException, DataAccessException {
				List<Costing> sList = new ArrayList<Costing>();
				while (rst.next()) {

					Costing store = new Costing();
					store.setId(rst.getInt("id"));
					store.setStoreNo(rst.getString("storeNo"));
					sList.add(store);
				}
				return sList;
			}

		});
	}

}
