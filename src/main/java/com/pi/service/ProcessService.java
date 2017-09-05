package com.pi.service;

import java.util.List;

import com.pi.model.StoreProcess;
import com.pi.util.InvalidStoreNumber;

public interface ProcessService {
	public void updateStore(StoreProcess process);

	public int storeTaskId(String taskId,List<String> stoNo);

	public List<StoreProcess> getAllStorePo();

	public String dateAsString();

	public void invFileUploadForProcess(List<String> sheetData,List<StoreProcess> invData)  throws InvalidStoreNumber;

}
