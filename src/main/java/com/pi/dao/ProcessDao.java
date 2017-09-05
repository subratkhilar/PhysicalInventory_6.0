package com.pi.dao;

import java.util.List;

import com.pi.model.StoreProcess;
import com.pi.util.InvalidStoreNumber;

public interface ProcessDao {
	public void updateStore(StoreProcess process);

	public List<StoreProcess> getAllStorePo();

	public int storeTaskId(String taskId, List<String> stoNo);

	public List<StoreProcess> getAllReport();

	public String dateAsString();

	public void invFileUploadForProcess(List<String> sheetData,List<StoreProcess> invData)  throws InvalidStoreNumber;

}
