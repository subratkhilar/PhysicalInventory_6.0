package com.pi.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.pi.model.StoreProcess;

@Repository("reportDao")
public class ReportDaoImpl implements ReportDao {
	private final static String SQL_PARTICIPATING_STORES = "SELECT STORE_NUMBER,PARTICIPATING_STORE FROM PHY_INV_STORES_TAKING where participating_store='Y'";
	private final static String SQL_FLOOR_DATA = "select a.* from phy_inv_sales_totals a,phy_inv_report_desc b where b.id=a.id and b.PRODUCT_TYPE='FLOOR COVERING' and a.STORE_ID='1059'";
	private final static String SQL_SPRAY_DATA = "select a.* from phy_inv_sales_totals a,phy_inv_report_desc b where b.id=a.id and b.PRODUCT_TYPE='SPRAY EQUIPMENT' and a.STORE_ID='1059'";
	private final static String SQL_BRUSHES_ROLLERS_DATA = "select a.* from phy_inv_sales_totals a,phy_inv_report_desc b where b.id=a.id and b.PRODUCT_TYPE='BRUSHES'||' '||'&'||' '||'ROLLERS' and a.STORE_ID='1059'";
	private final static String SQL_PAINT_DATA = "select a.* from phy_inv_sales_totals a,phy_inv_report_desc b where b.id=a.id and b.PRODUCT_TYPE='PAINT' and a.STORE_ID='1059'";
	private final static String SQL_ASSOC_PRODUCTS_DATA = "select a.* from phy_inv_sales_totals a,phy_inv_report_desc b where b.id=a.id and b.PRODUCT_TYPE='ASSOC PRODUCTS' and a.STORE_ID='1059'";
	private final static String SQL_WALL_COVERING_DATA = "select a.* from phy_inv_sales_totals a,phy_inv_report_desc b where b.id=a.id and b.PRODUCT_TYPE='WALL COVERING' and a.STORE_ID='1059'";
	private final static String SQL_WINDOW_TREAT_DATA = "select a.* from phy_inv_sales_totals a,phy_inv_report_desc b where b.id=a.id and b.PRODUCT_TYPE='WINDOW TREATMENT' and a.STORE_ID='1059'";
	// getting the values for the report header
	private final static String SQL_HEADER_DATA = "select division, division_name, area, area_name, district, district_name, "
			+ "store_number,store_name from costcntr_ecomm_v where store_number='70001T'";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	ResultSet floorRs = null;
	ResultSet rs = null;
	public DecimalFormat decimalFormat = new DecimalFormat("##.00");

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Map<String, List<String>> getReport(int storeId) {
		final List<String> columns = new ArrayList<String>();

		SqlRowSet rptHeaderRs = jdbcTemplate.queryForRowSet(SQL_HEADER_DATA);
		List<String> headerList = new ArrayList<String>();
		while (rptHeaderRs.next()) {
			String div = rptHeaderRs.getString("division") + " "
					+ rptHeaderRs.getString("division_name");
			String area = rptHeaderRs.getString("area") + " "
					+ rptHeaderRs.getString("area_name");
			String dist = rptHeaderRs.getString("district")
					+ rptHeaderRs.getString("district_name");
			String store = rptHeaderRs.getString("store_number") + " "
					+ rptHeaderRs.getString("store_name");
			headerList.add(div);
			headerList.add(area);
			headerList.add(dist);
			headerList.add(store);
		}

		jdbcTemplate.query("select * from phy_inv_sales_totals",
				new ResultSetExtractor<Integer>() {

					@Override
					public Integer extractData(final ResultSet rs)
							throws SQLException, DataAccessException {
						final ResultSetMetaData rsmd = rs.getMetaData();
						final int columnCount = rsmd.getColumnCount();
						for (int i = 1; i <= columnCount; i++) {
							columns.add(rsmd.getColumnName(i));
						}
						return columnCount;
					}
				});
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		SqlRowSet floorRs = jdbcTemplate.queryForRowSet(SQL_FLOOR_DATA);
		List<String> floorList = new ArrayList<String>();
		List<String> ty_sales_totals = new ArrayList<String>();
		boolean floorFlag = true;
		while (floorRs.next()) {
			floorFlag = false;
			ty_sales_totals.add(floorRs.getString("TY_SALES"));

			for (int i = 1; i < columns.size() - 1; i++) {
				floorList.add(formatNumber(floorRs.getString(columns.get(i))));
			}
		}
		if (floorFlag) {
			for (int i = 1; i < columns.size() - 1; i++) {
				floorList.add(" ");
			}
		}

		SqlRowSet sparyRS = jdbcTemplate.queryForRowSet(SQL_SPRAY_DATA);
		List<String> sparyList = new ArrayList<String>();

		boolean sparyFlag = true;
		while (sparyRS.next()) {
			sparyFlag = false;
			ty_sales_totals.add(sparyRS.getString("TY_SALES"));
			for (int i = 1; i < columns.size() - 1; i++) {
				sparyList.add(formatNumber(sparyRS.getString(columns.get(i))));
			}
		}
		if (sparyFlag) {
			for (int i = 1; i < columns.size() - 1; i++) {
				sparyList.add(" ");
			}
		}
		SqlRowSet paintRS = jdbcTemplate.queryForRowSet(SQL_PAINT_DATA);
		List<String> paintList = new ArrayList<String>();
		boolean paintFlag = true;
		while (paintRS.next()) {
			paintFlag = false;
			ty_sales_totals.add(paintRS.getString("TY_SALES"));

			for (int i = 1; i < columns.size() - 1; i++) {
				paintList.add(formatNumber(paintRS.getString(columns.get(i))));
			}
		}
		if (paintFlag) {
			for (int i = 1; i < columns.size() - 1; i++) {
				paintList.add(" ");
			}
		}

		SqlRowSet brushRollerRS = jdbcTemplate
				.queryForRowSet(SQL_BRUSHES_ROLLERS_DATA);
		List<String> brushRollerList = new ArrayList<String>();
		boolean brushRollerFlag = true;
		while (brushRollerRS.next()) {
			ty_sales_totals.add(brushRollerRS.getString("TY_SALES"));
			brushRollerFlag = false;
			for (int i = 1; i < columns.size() - 1; i++) {
				brushRollerList.add(formatNumber(brushRollerRS
						.getString(columns.get(i))));
			}
		}
		if (brushRollerFlag) {
			for (int i = 1; i < columns.size() - 1; i++) {
				brushRollerList.add(" ");
			}
		}
		SqlRowSet assocProdcutsRS = jdbcTemplate
				.queryForRowSet(SQL_ASSOC_PRODUCTS_DATA);
		List<String> assocProdcutsList = new ArrayList<String>();
		boolean assocProdcutsFlag = true;
		while (assocProdcutsRS.next()) {
			assocProdcutsFlag = false;
			ty_sales_totals.add(assocProdcutsRS.getString("TY_SALES"));

			for (int i = 1; i < columns.size() - 1; i++) {
				assocProdcutsList.add(formatNumber(assocProdcutsRS
						.getString(columns.get(i))));
			}
		}
		if (assocProdcutsFlag) {
			for (int i = 1; i < columns.size() - 1; i++) {
				assocProdcutsList.add(" ");
			}
		}
		SqlRowSet wallCoveringRS = jdbcTemplate
				.queryForRowSet(SQL_WALL_COVERING_DATA);
		List<String> wallCoveringList = new ArrayList<String>();
		boolean wallCoveringFlag = true;
		while (wallCoveringRS.next()) {
			ty_sales_totals.add(wallCoveringRS.getString("TY_SALES"));
			wallCoveringFlag = false;
			for (int i = 1; i < columns.size() - 1; i++) {
				wallCoveringList.add(formatNumber(wallCoveringRS
						.getString(columns.get(i))));
			}
		}
		if (wallCoveringFlag) {
			for (int i = 1; i < columns.size() - 1; i++) {
				wallCoveringList.add(" ");
			}
		}
		SqlRowSet windowTreatRS = jdbcTemplate
				.queryForRowSet(SQL_WINDOW_TREAT_DATA);
		List<String> windowTreatList = new ArrayList<String>();
		boolean windowTreatFlag = true;
		while (windowTreatRS.next()) {
			ty_sales_totals.add(windowTreatRS.getString("TY_SALES"));
			windowTreatFlag = false;
			for (int i = 1; i < columns.size() - 1; i++) {
				windowTreatList.add(formatNumber(windowTreatRS
						.getString(columns.get(i))));
			}
		}
		if (windowTreatFlag) {
			for (int i = 1; i < columns.size() - 1; i++) {
				windowTreatList.add(" ");
			}
		}

		String desStr = "TY SALES $ ( 11/09 - 10/10 ),LY SALES $ ( 11/08 - 10/09 ) ,TY GM % ( 11/09 - 10/10 ) ,  LY GM % ( 11/08 - 10/09 ) , NET BOOK INVENTORY ,TINT USE ADJUSTMENT ,( + )  CLOSING REPORTS, \"A\"   CHRGD TO STORE-NOT RECVD( INT ) ,\"B\"   RECVD-NOT CHRGD TO STORE( EXT ) ,"
				+ "\"C\"   RECVD-NOT CHRGD TO STORE( INT ) ,   \"D\"   OPEN IBARS/ISTS/EXPENSED MDSE TRAN , \"E\"   OPEN CHARGE-BACKS(EXT) ,\"F\"   CHRGD TO STORE-NOT RECVD( EXT ) , \"R\"   OPEN DOSCREPANCY REPORTS( EXT ) ,\"Z\"   OPEN MOUTH SALES REPORTS ,"
				+ " =   ADJUSTED BOOK INVENTORY ,GROSS PHYSICAL INVENTORY ( AS COUNTED ) , (+)   PHYSICAL INV ADJUSTMENTS ,=  ADJUSTED GROSS PHYSICAL INVENTORY , =  TY INVENTORY SHRINK/( PICKUP )%( 3710 A ,TY INVENTORY SHRINK/( PICKUP )% TO SLS ,CURRENT YEAR OBSOLESCENCE ,(+) CURRENT YEAR OBS ADJUSTMENTS ,"
				+ " = ADJUSTED CURRENT YEAR OBS ( 3700 ACCT ) , = ADJUSTED CURRENT YEAR OBS % TO SLS ,TOTAL P&L ADJUSTMENT $ , TOTAL P&L ADJUSTMENT % TO SALES,  LY INVENTORY SHRINK/ (PICKUP) , LY ADJUSTED CURRENT YEAR OBS ,"
				+ "LY TOTAL P&L ADJUSTMENT  $,LY P&L ADJUSTMENT % TO SALES,TOTAL P&L ADJUSTMENT $ CHANGE TY VS LY ";

		List<String> descList = new ArrayList<String>(Arrays.asList(desStr
				.split(",")));

		System.out.println("descList size " + descList.size() + " paintList "
				+ paintList.size() + " brushRollerList "
				+ brushRollerList.size());
		System.out.println("assocProdcutsList size " + assocProdcutsList.size()
				+ " floorList " + floorList.size() + " sparyList "
				+ sparyList.size());
		System.out.println("wallCoveringList size " + wallCoveringList.size()
				+ " windowTreatList " + windowTreatList.size());

		List<String> totalList = new ArrayList<String>();
		for (int i = 0; i < descList.size(); i++) {
			double result = reverseformatNumber(paintList.get(i))
					+ reverseformatNumber(brushRollerList.get(i))
					+ reverseformatNumber(assocProdcutsList.get(i))
					+ reverseformatNumber(floorList.get(i))
					+ reverseformatNumber(sparyList.get(i))
					+ reverseformatNumber(wallCoveringList.get(i))
					+ reverseformatNumber(windowTreatList.get(i));
			totalList.add(formatNumber(decimalFormat.format(result) + ""));

			map.put("description", descList);
			map.put("paint", paintList);
			map.put("brush", brushRollerList);
			map.put("assocprod", assocProdcutsList);
			map.put("floor", floorList);
			map.put("spray", sparyList);
			map.put("wall", wallCoveringList);
			map.put("windowtreat", windowTreatList);
			map.put("totalList", totalList);
			map.put("headerList", headerList);
		}
		return map;
	}

	private String formatNumber(String string) {
		String formateedString = " ";
		if (null != string && string.length() > 0) {
			float number = Float.parseFloat(string);
			if (number < 0) {
				number = -number;
				formateedString = NumberFormat.getNumberInstance(Locale.US)
						.format(number) + "-";
			} else {
				formateedString = NumberFormat.getNumberInstance(Locale.US)
						.format(number);
			}
		}
		return formateedString;
	}

	// again remove comma and append - to the front(reverse operation)
	private static double reverseformatNumber(String string) {
		float result = 0;
		if (null != string && string.length() > 0) {
			if (string.contains(",")) {
				string = string.replaceAll(",", "");
			}
			if (string.indexOf("-") > 0) {
				string = "-" + string.substring(0, string.lastIndexOf("-") - 1);
			}
			try {
				result = Float.parseFloat(string);
			} catch (Exception e) {
				result = 0;
			}
		}
		return result;
	}

	@Override
	public List<StoreProcess> getAllReport() {
		return jdbcTemplate.query(SQL_PARTICIPATING_STORES,
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
}
