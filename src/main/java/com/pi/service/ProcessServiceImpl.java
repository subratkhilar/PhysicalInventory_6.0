package com.pi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pi.dao.ProcessDao;
import com.pi.model.StoreProcess;
import com.pi.util.InvalidStoreNumber;

@Service("processService")
public class ProcessServiceImpl implements ProcessService {
	@Autowired
	private ProcessDao processDao;

	@Override
	public void updateStore(StoreProcess process) {
		processDao.updateStore(process);

	}

	@Override
	public List<StoreProcess> getAllStorePo() {

		return processDao.getAllStorePo();
	}

	@Override
	public int storeTaskId(String taskId, List<String> stoNo) {
		return processDao.storeTaskId(taskId,stoNo);
	}

	@Override
	public String dateAsString() {
		return processDao.dateAsString();
	}

	@Override
	public void invFileUploadForProcess(List<String> sheetData,List<StoreProcess> invData)  throws InvalidStoreNumber {
		 processDao.invFileUploadForProcess(sheetData,invData);
		
	}

	/*
	 * @Override public List<StoreProcess> getAllReport() { return
	 * processDao.getAllReport(); }
	 */
}
