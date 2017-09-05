package com.pi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pi.model.StoreProcess;
import com.pi.service.ReportService;

@RestController
public class ReportController {

	@Autowired
	private ReportService rptService;

	@RequestMapping(value = "/listOfRpt", method = RequestMethod.GET, headers = "Accept=application/json")
	public Map<String, List<String>> getAllReport(
			@RequestParam(value = "storeNo") String storeNo) {
		Map<String, List<String>> rptList = rptService.getReports(9953);
		return rptList;
	}

	@RequestMapping(value = "/listOfReportForDashboard", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<StoreProcess> getAllReport() {
		List<StoreProcess> storeList = rptService.getAllReport();
		return storeList;
	}
}
