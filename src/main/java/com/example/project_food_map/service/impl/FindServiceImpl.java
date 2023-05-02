package com.example.project_food_map.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.project_food_map.constants.RtnCode;
import com.example.project_food_map.repository.StoreDao;
import com.example.project_food_map.service.ifs.FindService;
import com.example.project_food_map.vo.Response;
import com.example.project_food_map.vo.StoreAndMenu;

@Service
public class FindServiceImpl implements FindService {

	@Autowired
	private StoreDao storeDao;

	@Override
	public Response findStoreByCityLimit(String city, int times) {
		// �����J���šA�d�ߵ��Ƭ��t
		if (!StringUtils.hasText(city)) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		if (times < 0) {
			return new Response(RtnCode.INCORRECT.getMessage());
		}
		// �j�M����
		List<StoreAndMenu> res = storeDao.findStoreAndMenuByCityLimit(city, times);
		if (CollectionUtils.isEmpty(res)) {
			return new Response(RtnCode.NOT_FOUND.getMessage());
		}
		return new Response(res, RtnCode.SUCCESS.getMessage());
	}

	@Override
	public Response findStoreByPoint(double point) {
		// ����W�X��J�d��
		if (point > 5.0 || point < 0.0) {
			return new Response(RtnCode.OUT_OF_LIMIT.getMessage());
		}
		// �M��d�򤺵��G
		List<StoreAndMenu> res = storeDao.findStoreAndMenuByPointGreaterEqualThan(point);
		if (CollectionUtils.isEmpty(res)) {
			return new Response(RtnCode.NOT_FOUND.getMessage());
		}
		return new Response(res, RtnCode.SUCCESS.getMessage());
	}

	@Override
	public Response findStoreAndMenuByPoint(double point, int menuPoint) {
		// ����W�X��J�d��
		if (point > 5.0 || point < 0.0 || menuPoint > 5 || menuPoint < 0) {
			return new Response(RtnCode.OUT_OF_LIMIT.getMessage());
		}
		// �M��d�򤺵��G
		List<StoreAndMenu> res = storeDao.findStoreAndMenuByPointAndMenuPointGreaterEqualThan(point, menuPoint);
		if (CollectionUtils.isEmpty(res)) {
			return new Response(RtnCode.NOT_FOUND.getMessage());
		}
		return new Response(res, RtnCode.SUCCESS.getMessage());
	}

}
