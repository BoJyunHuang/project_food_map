package com.example.project_food_map.service.ifs;

import com.example.project_food_map.entity.Menu;
import com.example.project_food_map.entity.MenuId;
import com.example.project_food_map.vo.Response;

public interface FoodMapService {

	// 新增餐點
	public Response addMenu(String menu, String storeName, int price, int menuPoint);

	// 修改餐點
	public Response reviseMenu(Menu menu);

	// 刪除餐點
	public Response deleteMenu(MenuId menuId);

	// 新增店家
	public Response addStore(String store, String city);

	// 修改店家
	public Response reviseStore(String store, String city);

	// 刪除店家
	public Response deleteStore(String store);

	// 依據城市找尋店家
	public Response findStoreByCityLimit(String city, int times);

	// 依據評價找尋店家
	public Response findStoreByPoint(double point);

	// 依據評價找尋店家 + 依據餐點評價含以上
	public Response findStoreAndMenuByPoint(double point, int menuPoint);

}
