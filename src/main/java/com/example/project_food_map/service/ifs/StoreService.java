package com.example.project_food_map.service.ifs;

import com.example.project_food_map.vo.Response;

public interface StoreService {

	// �s�W���a
	public Response addStore(String store, String city);

	// �ק况�a
	public Response reviseStore(String store, String city);
	
	// �R�����a
	public Response deleteStore(String store);

}
