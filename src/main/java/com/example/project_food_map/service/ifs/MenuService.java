package com.example.project_food_map.service.ifs;

import com.example.project_food_map.entity.MenuId;
import com.example.project_food_map.vo.Request;
import com.example.project_food_map.vo.Response;

public interface MenuService {

	// �s�W�\�I
	public Response addMenu(String menu, String storeName, int price, int menuPoint);

	// �ק��\�I
	public Response reviseMenu(Request request);
	
	// �R���\�I
	public Response deleteMenu(MenuId menuId);
	
}
