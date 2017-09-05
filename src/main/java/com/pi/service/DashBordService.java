package com.pi.service;

import java.util.List;

import com.pi.model.DashBord;

public interface DashBordService {
	public void createProcess(DashBord process);

	public List<DashBord> getAllProcess();

}
