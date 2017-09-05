package com.pi.service;

import java.util.List;

import com.pi.model.Costing;

public interface CostingService {
	public void createStore(Costing store);

	public List<Costing> getAllStores();

}
