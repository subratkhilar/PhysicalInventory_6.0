package com.pi.dao;

import java.util.List;

import com.pi.model.DashBord;

public interface DashBordDao {
	public void createProcess(DashBord process);

	public List<DashBord> getAllProcess();

	public void updateProcess(int pid);

	public void deleteProcess(int pid);

	public DashBord getProcessByd(int pid);

}
