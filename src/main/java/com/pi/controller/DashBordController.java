package com.pi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pi.model.DashBord;
import com.pi.service.DashBordService;

@RestController
public class DashBordController {
	@Autowired
	private DashBordService dashBordService;

	@RequestMapping(value = "/tasks", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<DashBord> getAllProcess() {
		List<DashBord> tasks = dashBordService.getAllProcess();
		return tasks;

	}

}
