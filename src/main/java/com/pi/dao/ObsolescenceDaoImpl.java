package com.pi.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pi.model.Obsolescence;
import com.pi.model.StoreProcess;
import com.pi.util.InvalidStoreNumber;

@Repository("obsolDao")
public class ObsolescenceDaoImpl implements ObsolescenceDao {
	Logger logger = Logger.getLogger(ObsolescenceDaoImpl.class);
	private final static String SQL_TASK_ID_FORCED_OBSOLES = "SELECT TASK_ID FROM PHY_INV_TASKS_PO WHERE TASKS = 'Forced Obsolescence'";
	private final static String SQL_GET_ALL_OBSOLES = "SELECT * FROM PHY_INV_OBSOLESCENCE";

	private final static String SQL_INSERT_OBSOLES = "INSERT INTO PHY_INV_OBSOLESCENCE(STORE_NUMBER,STORE_SKU) VALUES (?,?)";
	private final static String SQL_TASKS_LOG_OBSOLES = "INSERT INTO PHY_INV_TASKS_LOG(ID, CREATED_DATE,TASK_ID,ACTION_PERFORMED,UPDATED_VALUES) VALUES(TASKS_LOG_SEQ.NEXTVAL,?,?,?,?)";

	private final static String SQL_DELETE_OBSOLES = "DELETE from PHY_INV_OBSOLESCENCE WHERE STORE_SKU = ?";

	private final static String SQL_TASKS_PO_OBSOLES = "update PHY_INV_TASKS_PO set STATUS=? WHERE TASKS = ?";

	private final static String SQL_ALL_DELETE_OBSOLES = "DELETE from PHY_INV_OBSOLESCENCE";

	final Timestamp timeStamp = new java.sql.Timestamp(new Date().getTime());
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public String createObsolence(final Obsolescence obs) {
		List<String> createObsolenceList = new ArrayList<String>();
		createObsolenceList.add(obs.getSkuNo());
		logger.info("46..." + obs.getSkuNo() + " " + obs.getoId());
		String SQL_CREATE_OBSOLES = "select * from PHY_INV_STORES_TAKING where store_number='"
				+ obs.getoId() + "'";
		System.out.println("query is " + SQL_CREATE_OBSOLES);
		SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_CREATE_OBSOLES);
		boolean flag = false;
		String result = "";
		while (rs.next()) {
			flag = true;
		}
		if (flag) {
			jdbcTemplate.execute(SQL_INSERT_OBSOLES,
					new PreparedStatementCallback<Boolean>() {
						public Boolean doInPreparedStatement(
								PreparedStatement ps) throws SQLException,
								DataAccessException {
							ps.setInt(1, obs.getoId());
							ps.setString(2, obs.getSkuNo());
							return ps.execute();
						}
					});
			result = "Record Updated successfully";
			logData("Insert", createObsolenceList);
		} else {
			result = "The store number you entered doesn't exist";
		}
		return result;

	}

	@Override
	public List<Obsolescence> getAllObsolence() {
		return jdbcTemplate.query(SQL_GET_ALL_OBSOLES,
				new ResultSetExtractor<List<Obsolescence>>() {

					@Override
					public List<Obsolescence> extractData(ResultSet rst)
							throws SQLException, DataAccessException {
						List<Obsolescence> oList = new ArrayList<Obsolescence>();
						int i = 0;
						StringBuffer skuNo = new StringBuffer("");
						while (rst.next()) {
							i = i + 1;
							skuNo.append(" " + rst.getString("STORE_SKU"));
							Obsolescence obsolen = new Obsolescence();
							obsolen.setSkuNo(rst.getString("STORE_SKU"));
							oList.add(obsolen);

						}
						return oList;
					}
				});
	}

	@Override
	public void deleteObsolence(Obsolescence obsolence) {
		final List<String> delObsoList = obsolence.getSkuNoList();
		try {
			jdbcTemplate.batchUpdate(SQL_DELETE_OBSOLES,
					new BatchPreparedStatementSetter() {
						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							String skuNo = delObsoList.get(i);
							ps.setString(1, skuNo);
						}

						public int getBatchSize() {
							return delObsoList.size();
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}

		logData("DELETE", delObsoList);
	}

	private void logData(String action, List<String> data) {
		int task_id = getTaskIdForObso();
/*		jdbcTemplate.update(SQL_TASKS_LOG_OBSOLES, timeStamp, task_id, action,
				StringUtils.join(data, ','));
		jdbcTemplate.update(SQL_TASKS_PO_OBSOLES, "SAVED",
				"Forced Obsolescence");*/
	}

	private int getTaskIdForObso() {
		SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_TASK_ID_FORCED_OBSOLES);
		int task_id = 0;
		while (rs.next()) {
			task_id = rs.getInt("task_id");
		}
		return task_id;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void obsFileUploadForProcess(List<String> sheetData,
			final List<Obsolescence> invData) throws InvalidStoreNumber {
		// first compare the two list
		String sql = "select ID from phy_inv_status";
		List<String> mainList = (List<String>) jdbcTemplate.queryForList(sql,
				String.class);
		logger.info(" *** ==  sheetData before retainAll == *** " + sheetData);
		logger.info(" *** ==  mainList  == *** " + mainList);

		sheetData.retainAll(mainList);
		logger.info(" *** ==  sheetData After retainAll == *** " + sheetData);
		logger.info(" *** ==  SheetData size == *** " + sheetData.size());

		if (sheetData.size() == 0) {
			jdbcTemplate.execute(SQL_ALL_DELETE_OBSOLES);
			logger.info(" *** == Delete All Record from  PHY_INV_OBSOLESCENCE table completed == *** ");
			// INSERT DATA

			jdbcTemplate.batchUpdate(SQL_INSERT_OBSOLES,
					new BatchPreparedStatementSetter() {

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {

							Obsolescence obsProcess = invData.get(i);
							String storeNo = obsProcess.getStoreNumber();
							ps.setString(1, storeNo);
							ps.setString(2, obsProcess.getSkuNo());

						}

						public int getBatchSize() {
							return invData.size();
						}
					});
			logger.info(" *** == Insert record into PHY_INV_OBSOLESCENCE table completed == *** ");

		} else {
			String message = sheetData.toString();
			logger.info(" *** == Invalid sku Number trying to insert PHY_INV_OBSOLESCENCE table  == *** "
					+ message);
			throw new InvalidStoreNumber(" Invalid Store No " + message);

		}

	}

}
