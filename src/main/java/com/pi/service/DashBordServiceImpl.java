package com.pi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pi.dao.DashBordDao;
import com.pi.model.DashBord;

@Service("dashBordService")
public class DashBordServiceImpl implements DashBordService {
	@Autowired
	private DashBordDao dashBordDao;

	@Override
	public void createProcess(DashBord process) {

	}

	@Override
	public List<DashBord> getAllProcess() {

		return dashBordDao.getAllProcess();
	}

}
