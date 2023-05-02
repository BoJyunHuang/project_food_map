package com.example.project_food_map.service.ifs;

import com.example.project_food_map.vo.Response;

public interface FindService {

	// �̾ګ�����M���a
	public Response findStoreByCityLimit(String city, int times);

	// �̾ڵ�����M���a
	public Response findStoreByPoint(double point);

	// �̾ڵ�����M���a + �̾��\�I�����t�H�W
	public Response findStoreAndMenuByPoint(double point, int menuPoint);

}
