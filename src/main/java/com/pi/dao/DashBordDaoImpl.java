package com.pi.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.pi.model.DashBord;

@Repository("dashBordDao")
public class DashBordDaoImpl implements DashBordDao {
	private String sql = null;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void createProcess(DashBord process) {

	}

	@Override
	public List<DashBord> getAllProcess() {
		sql = "SELECT * FROM INVENTORY_PROCESS";
		return jdbcTemplate.query(sql,
				new ResultSetExtractor<List<DashBord>>() {
					@Override
					public List<DashBord> extractData(ResultSet rst)
							throws SQLException, DataAccessException {
						List<DashBord> pList = new ArrayList<DashBord>();
						while (rst.next()) {
							DashBord process = new DashBord();
							process.setpId(rst.getInt("PID"));
							process.setProcessName(rst
									.getString("PROCESS_NAME"));
							process.setStatus(rst.getString("STATUS"));
							pList.add(process);
						}
						return pList;
					}
				});
	}

	@Override
	public void updateProcess(int pid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteProcess(int pid) {
		// TODO Auto-generated method stub

	}

	@Override
	public DashBord getProcessByd(int pid) {
		// TODO Auto-generated method stub
		return null;
	}

}
