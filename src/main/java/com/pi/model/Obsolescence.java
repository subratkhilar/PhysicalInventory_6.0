package com.pi.model;

import java.util.ArrayList;
import java.util.List;

public class Obsolescence {

	private int oId;
	private String skuNo;
	private String storeNumber;
	private List<String> skuNoList = new ArrayList<String>();

	
	public String getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}

	public int getoId() {
		return oId;
	}

	public void setoId(int oId) {
		this.oId = oId;
	}

	public String getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}

	public void setSkuNoList(List<String> skuNoList) {
		this.skuNoList = skuNoList;
	}

	public List<String> getSkuNoList() {
		return skuNoList;
	}

}
