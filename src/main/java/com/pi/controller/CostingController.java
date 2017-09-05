package com.pi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pi.model.Costing;
import com.pi.service.CostingService;

@RestController
public class CostingController {
	@Autowired
	private CostingService costingService;
	
	@RequestMapping(value = "/getAllStores", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<Costing> getAllStroes() {
		
		List<Costing> storeList = costingService.getAllStores();
		return storeList;

	}
}
