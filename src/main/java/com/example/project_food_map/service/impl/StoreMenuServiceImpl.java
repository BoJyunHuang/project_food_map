package com.example.project_food_map.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project_food_map.repository.MenuDao;
import com.example.project_food_map.repository.StoreDao;
import com.example.project_food_map.service.ifs.MenuService;
import com.example.project_food_map.service.ifs.StoreService;

@Service
public class StoreMenuServiceImpl implements StoreService, MenuService{

	@Autowired
	private StoreDao sDao;
	
	@Autowired
	private MenuDao mDao;
	
	@Override
	public void addMenu() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reviseMenu() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addStore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reviseStore() {
		// TODO Auto-generated method stub
		
	}

}
