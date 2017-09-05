package com.pi.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StoreProcess {
	private String storeNo;
	private String storeName; 
	private boolean status;
	private List<String> storeNoList = new ArrayList<String>();
	private List<String> uncheckList = new ArrayList<String>();
	private List<String> textAreaList = new ArrayList<String>();
	private String processDate;

	// new code
	private List<StoreProcess> storeList = new ArrayList<StoreProcess>();

	
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public List<StoreProcess> getStoreList() {
		return storeList;
	}

	public void setStoreList(List<StoreProcess> storeList) {
		this.storeList = storeList;
	}

	// end
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public List<String> getStoreNoList() {
		return storeNoList;
	}

	public void setStoreNoList(List<String> storeNoList) {
		this.storeNoList = storeNoList;
	}

	public List<String> getUncheckList() {
		return uncheckList;
	}

	public void setUncheckList(List<String> uncheckList) {
		this.uncheckList = uncheckList;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getStoreNo() {
		return storeNo;
	}

	public void setTextAreaList(List<String> textAreaList) {

		this.textAreaList = textAreaList;
	}

	public List<String> getTextAreaList() {
		return textAreaList;
	}

	public void setProcessDate(String processDate) {
		this.processDate = processDate;
	}

	public String getProcessDate() {
		return processDate;
	}

}
