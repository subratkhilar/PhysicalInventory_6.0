package com.pi.model;

import java.util.ArrayList;
import java.util.List;

public class Report {
	private List<String> reportList = new ArrayList<String>();
	private int storeId;

	public void setReportList(List<String> reportList) {
		this.reportList = reportList;
	}

	public List<String> getReportList() {
		return reportList;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public int getStoreId() {
		return storeId;
	}

}
