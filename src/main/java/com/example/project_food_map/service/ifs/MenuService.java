package com.example.project_food_map.service.ifs;

import com.example.project_food_map.entity.MenuId;
import com.example.project_food_map.vo.Request;
import com.example.project_food_map.vo.Response;

public interface MenuService {

	// 新增餐點
	public Response addMenu(String menu, String storeName, int price, int menuPoint);

	// 修改餐點
	public Response reviseMenu(Request request);
	
	// 刪除餐點
	public Response deleteMenu(MenuId menuId);
	
}
