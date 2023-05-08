package com.example.project_food_map.service.ifs;

import com.example.project_food_map.entity.Menu;
import com.example.project_food_map.entity.MenuId;
import com.example.project_food_map.vo.Response;

public interface FoodMapService {

	// �s�W�\�I
	public Response addMenu(String menu, String storeName, int price, int menuPoint);

	// �ק��\�I
	public Response reviseMenu(Menu menu);

	// �R���\�I
	public Response deleteMenu(MenuId menuId);

	// �s�W���a
	public Response addStore(String store, String city);

	// �ק况�a
	public Response reviseStore(String store, String city);

	// �R�����a
	public Response deleteStore(String store);

	// �̾ګ�����M���a
	public Response findStoreByCityLimit(String city, int times);

	// �̾ڵ�����M���a
	public Response findStoreByPoint(double point);

	// �̾ڵ�����M���a + �̾��\�I�����t�H�W
	public Response findStoreAndMenuByPoint(double point, int menuPoint);

}
