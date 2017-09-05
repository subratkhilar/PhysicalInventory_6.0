package com.pi.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pi.model.StoreProcess;
import com.pi.util.InvalidStoreNumber;

@Repository("processDao")
public class ProcessDaoImpl implements ProcessDao {

	Logger logger = Logger.getLogger(ProcessDaoImpl.class);
	private final static String SQL_UPDATE_STORES_PO_Y = "UPDATE PHY_INV_STORES_TAKING SET PARTICIPATING_STORE='Y',UPDATED_DATE=?,INV_RUN_DATE = ? WHERE STORE_NUMBER=?";
	private final static String SQL_UPDATE_STORES_PO_N = "UPDATE PHY_INV_STORES_TAKING SET PARTICIPATING_STORE='N',UPDATED_DATE=?, INV_RUN_DATE = ? WHERE STORE_NUMBER=?";
	private final static String SQL_TASKS_PO = "UPDATE PHY_INV_TASKS_PO SET STATUS=? WHERE TASKS = ?";

	// private final static String SQL_TASKS_LOG =
	// "INSERT INTO PHY_INV_TASKS_LOG(ID, CREATED_DATE,TASK_ID,ACTION_PERFORMED,UPDATED_VALUES) VALUES(TASKS_LOG_SEQ.NEXTVAL,?,?,?,?)";
	// private final static String SQL_STORE_REQUEST_ID =
	// "INSERT INTO PHY_INV_TASKS_LOG(ID, REQUEST_ID,CREATED_DATE,TASK_ID,ACTION_PERFORMED,UPDATED_VALUES) VALUES(TASKS_LOG_SEQ.NEXTVAL,?,?,?,?,?)";

	private final static String SQL_ALL_STORES_PO = "SELECT STORE_NUMBER,PARTICIPATING_STORE FROM PHY_INV_STORES_TAKING";
	private final static String SQL_GET_ALL_SELECTED_STORES = "SELECT STORE_NUMBER,PARTICIPATING_STORE FROM PHY_INV_STORES_TAKING where participating_store='Y'";
	private final static String SQL_GET_TASK_ID_WITH_STR_TAKING_DATA = "SELECT TASK_ID FROM PHY_INV_TASKS_PO WHERE TASKS = 'Stores Taking Data'";

	private static String SQL_DELETE_ALL_STORES_PO = "DELETE FROM PHY_INV_STORES_TAKING";
	private static String SQL_INSERT_ALL_STORES_PO = "INSERT INTO PHY_INV_STORES_TAKING (STORE_NUMBER,STORE_NAME,CREATED_DATE,CREATED_ID,PARTICIPATING_STORE) VALUES(?,?,?,?,?)";

	ResultSet rst = null;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void updateStore(StoreProcess process) {
		final List<String> stNoList = process.getStoreNoList();
		final List<String> unselctedList = process.getUncheckList();
		final String processDate = process.getProcessDate();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parsedDate = null;
		try {
			parsedDate = dateFormat.parse(processDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		final Timestamp invRunDate = new java.sql.Timestamp(
				parsedDate.getTime());
		final Timestamp timeStamp = new java.sql.Timestamp(new Date().getTime());

		if (null != stNoList && stNoList.size() > 0) {
			jdbcTemplate.batchUpdate(SQL_UPDATE_STORES_PO_Y,
					new BatchPreparedStatementSetter() {

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							String storeNo = stNoList.get(i);
							ps.setTimestamp(1, timeStamp);
							ps.setTimestamp(2, invRunDate);
							ps.setString(3, storeNo);
						}

						public int getBatchSize() {
							return stNoList.size();
						}
					});
			// update to the log table
			int task_id = getTaskId();
			logger.info("Unselected List ******" + unselctedList);

			/*
			 * jdbcTemplate.update(SQL_TASKS_LOG, timeStamp, task_id, "UPDATED",
			 * StringUtils.join(unselctedList, ','));
			 */
			// update the tasks_po_table
			jdbcTemplate.update(SQL_TASKS_PO, "SAVED", "Stores Taking Data");
			logger.info("========== updated in PHY_INV_TASKS_LOG table completed ====");

		}

		if (null != unselctedList && unselctedList.size() > 0) {

			jdbcTemplate.batchUpdate(SQL_UPDATE_STORES_PO_N,
					new BatchPreparedStatementSetter() {

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							String storeNo = unselctedList.get(i);
							ps.setTimestamp(1, timeStamp);
							ps.setString(2, null);
							ps.setString(3, storeNo);
						}

						public int getBatchSize() {
							return unselctedList.size();
						}
					});
			// // update to the log table
			// int task_id = getTaskId();
			// jdbcTemplate.update(SQL_TASKS_LOG, timeStamp, task_id, "updated",
			// StringUtils.join(unselctedList, ','));
			// // update the tasks_po_table
			// jdbcTemplate.update(SQL_TASKS_PO, "SAVED", "Stores Taking Data");
			// System.out.println("updated in PHY_INV_TASKS_LOG table ");
		}

	}

	@Override
	public List<StoreProcess> getAllStorePo() {

		return jdbcTemplate.query(SQL_ALL_STORES_PO,
				new ResultSetExtractor<List<StoreProcess>>() {

					@Override
					public List<StoreProcess> extractData(ResultSet rst)
							throws SQLException, DataAccessException {
						List<StoreProcess> sList = new ArrayList<StoreProcess>();
						while (rst.next()) {

							StoreProcess store = new StoreProcess();

							store.setStoreNo(rst.getString("STORE_NUMBER"));
							String status = rst
									.getString("PARTICIPATING_STORE");
							if (status.equals("Y")) {
								store.setStatus(true);
							} else {
								store.setStatus(false);
							}
							sList.add(store);
						}
						return sList;
					}
				});
	}

	@Override
	public int storeTaskId(String requestId, List<String> stoNo) {
		/*
		 * int task_id = getTaskId(); jdbcTemplate.update(SQL_TASKS_PO,
		 * "APPROVED", "Stores Taking Data");
		 * 
		 * return jdbcTemplate.update(SQL_STORE_REQUEST_ID, requestId, new
		 * Date(), task_id,"WEBSERVICE CALLED", StringUtils.join(stoNo,','));
		 */
		return 1;
	}

	private int getTaskId() {

		SqlRowSet rs = jdbcTemplate
				.queryForRowSet(SQL_GET_TASK_ID_WITH_STR_TAKING_DATA);
		int task_id = 0;
		while (rs.next()) {
			task_id = rs.getInt("task_id");
		}
		return task_id;
	}

	@Override
	public List<StoreProcess> getAllReport() {
		return jdbcTemplate.query(SQL_GET_ALL_SELECTED_STORES,
				new ResultSetExtractor<List<StoreProcess>>() {
					@Override
					public List<StoreProcess> extractData(ResultSet rst)
							throws SQLException, DataAccessException {
						List<StoreProcess> sList = new ArrayList<StoreProcess>();
						while (rst.next()) {
							StoreProcess store = new StoreProcess();
							store.setStoreNo(rst.getString("STORE_NUMBER"));
							String status = rst
									.getString("PARTICIPATING_STORE");
							if (status.equals("Y")) {
								store.setStatus(true);
							} else {
								store.setStatus(false);
							}
							sList.add(store);
						}
						return sList;
					}
				});
	}

	@Override
	public String dateAsString() {
		String query = "select INV_RUN_DATE from PHY_INV_STORES_TAKING where PARTICIPATING_STORE = 'Y'";
		SqlRowSet rs = jdbcTemplate.queryForRowSet(query);
		String dateAsStr = "";
		while (rs.next()) {
			dateAsStr = rs.getString("INV_RUN_DATE");
		}
		return dateAsStr;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void invFileUploadForProcess(List<String> sheetData,
			final List<StoreProcess> invData) throws InvalidStoreNumber {
		final Timestamp timeStamp = new java.sql.Timestamp(new Date().getTime());

		// first compare the two list
		String sql = "select ID from phy_inv_status";
		// List<String> mainList = jdbcTemplate.query(sql,new
		// BeanPropertyRowMapper(String.class));

		List<String> mainList = (List<String>) jdbcTemplate.queryForList(sql,
				String.class);
		logger.info(" sheetData before retainAll ::::::::::: " + sheetData);
		logger.info(" mainList before retainAll ::::::::::: " + mainList);

		sheetData.retainAll(mainList);

		logger.info(" sheetData After retainAll ::::::::::: " + sheetData);
		logger.info(" sheetData size After retainAll ::::::::::: "
				+ sheetData.size());
		if (sheetData.size() == 0) {
			jdbcTemplate.execute(SQL_DELETE_ALL_STORES_PO);
			logger.info(" ==Delete All Record from PHY_INV_STORES_TAKING table coompleted == ");
			// INSERT DATA
			jdbcTemplate.batchUpdate(SQL_INSERT_ALL_STORES_PO,
					new BatchPreparedStatementSetter() {

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {

							StoreProcess storeProcess = invData.get(i);
							String storeNo = storeProcess.getStoreNo();
							ps.setString(1, storeNo);
							ps.setString(2, storeProcess.getStoreName());
							ps.setTimestamp(3, timeStamp);
							ps.setString(4, "SYSTEM");
							ps.setString(5, "N");

						}

						public int getBatchSize() {
							return invData.size();
						}
					});
			
					logger.info(" == Insert  Record into PHY_INV_STORES_TAKING table coompleted == ");

		} else {
			String message = sheetData.toString();
			logger.info(" == Invalid store Number List == " + message);
			throw new InvalidStoreNumber(" Invalid Store No " + message);

		}

	}

}
