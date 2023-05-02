package com.example.project_food_map.service.ifs;

import com.example.project_food_map.vo.Response;

public interface StoreService {

	// 新增店家
	public Response addStore(String store, String city);

	// 修改店家
	public Response reviseStore(String store, String city);
	
	// 刪除店家
	public Response deleteStore(String store);

}
