package com.example.project_food_map.service.ifs;

import com.example.project_food_map.vo.Response;

public interface FindService {

	// 依據城市找尋店家
	public Response findStoreByCityLimit(String city, int times);

	// 依據評價找尋店家
	public Response findStoreByPoint(double point);

	// 依據評價找尋店家 + 依據餐點評價含以上
	public Response findStoreAndMenuByPoint(double point, int menuPoint);

}
