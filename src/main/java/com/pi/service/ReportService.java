package com.pi.service;

import java.util.List;
import java.util.Map;

import com.pi.model.StoreProcess;

public interface ReportService {

	public Map<String, List<String>> getReports(int storeId);

	public List<StoreProcess> getAllReport();

}
