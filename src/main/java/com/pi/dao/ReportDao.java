package com.pi.dao;

import java.util.List;
import java.util.Map;

import com.pi.model.StoreProcess;

public interface ReportDao {
	public Map<String, List<String>> getReport(int storeId);

	public List<StoreProcess> getAllReport();
}
