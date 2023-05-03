package com.example.project_food_map.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.project_food_map.constants.RtnCode;
import com.example.project_food_map.entity.Menu;
import com.example.project_food_map.entity.MenuId;
import com.example.project_food_map.entity.Store;
import com.example.project_food_map.repository.MenuDao;
import com.example.project_food_map.repository.StoreDao;
import com.example.project_food_map.service.ifs.MenuService;
import com.example.project_food_map.service.ifs.StoreService;
import com.example.project_food_map.vo.Request;
import com.example.project_food_map.vo.Response;

@Service
public class StoreAndMenuServiceImpl implements StoreService, MenuService {

	@Autowired
	private StoreDao storeDao;

	@Autowired
	private MenuDao menuDao;

	@Override
	public Response addMenu(String menu, String storeName, int price, int menuPoint) {
		// 防止輸入為空、價錢為負、評價不再範圍內
		if (!StringUtils.hasText(menu) || !StringUtils.hasText(storeName)) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		if (price < 0 || menuPoint > 5 || menuPoint < 0) {
			return new Response(RtnCode.OUT_OF_LIMIT.getMessage());
		}
		// 檢查重複
		MenuId menuId = new MenuId(menu, storeName);
		boolean isExist = menuDao.existsById(menuId);
		if (isExist) {
			return new Response(RtnCode.ALREADY_EXISTED.getMessage());
		}
		// 儲存新檔，並更新評分
		Menu newMenu = new Menu(menu, storeName, price, menuPoint);
		menuDao.save(newMenu);
		double avg = menuDao.getAverageMenuPoint(storeName);
		storeDao.updatePointById(menuId.getStoreName(), avg);
		return new Response(RtnCode.SUCCESSFUL.getMessage());
	}

	@Override
	public Response reviseMenu(Request request) {
		boolean flag = false;
		// 防止輸入為空
		if (request == null || request.getMenuId() == null || request.getMenuId().getMenu() == null
				|| request.getMenuId().getStoreName() == null) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// 確認目標存在
		boolean isExist = menuDao.existsById(request.getMenuId());
		if (!isExist) {
			return new Response(RtnCode.NOT_FOUND.getMessage());
		}
		// 修改價錢
		if (request.getPrice() > 0) {
			menuDao.updateMenuPriceByMenuId(request.getMenuId().getMenu(), request.getMenuId().getStoreName(),
					request.getPrice());
			flag = true;
		}
		// 修改評分
		if (request.getMenuPoint() > 0 && request.getMenuPoint() < 6) {
			menuDao.updateMenuPointByMenuId(request.getMenuId().getMenu(), request.getMenuId().getStoreName(),
					request.getMenuPoint());
			// 取得平均並修改總分
			double avg = menuDao.getAverageMenuPoint(request.getMenuId().getStoreName());
			storeDao.updatePointById(request.getMenuId().getStoreName(), avg);
			flag = true;
		}
		if (!flag) {
			return new Response(RtnCode.INCORRECT.getMessage());
		}
		return new Response(RtnCode.SUCCESSFUL.getMessage());
	}

	@Override
	public Response deleteMenu(MenuId menuId) {
		// 防止輸入為空
		if (menuId == null || !StringUtils.hasText(menuId.getMenu()) || !StringUtils.hasText(menuId.getStoreName())) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// 確認餐單存在
		boolean isExist = menuDao.existsById(menuId);
		if (!isExist) {
			return new Response(RtnCode.NOT_FOUND.getMessage());
		}
		// 刪除菜單
		menuDao.deleteById(menuId);
		// 取得平均並修改總分
		double avg = menuDao.getAverageMenuPoint(menuId.getStoreName());
		storeDao.updatePointById(menuId.getStoreName(), avg);
		return new Response(RtnCode.SUCCESSFUL.getMessage());
	}

	@Override
	public Response addStore(String store, String city) {
		// 防止輸入為空
		if (!StringUtils.hasText(store) || !StringUtils.hasText(city)) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// 檢查重複
		boolean isExist = storeDao.existsById(store);
		if (isExist) {
			return new Response(RtnCode.ALREADY_EXISTED.getMessage());
		}
		// 儲存新檔
		Store newStore = new Store(store, city);
		return new Response(storeDao.save(newStore), RtnCode.SUCCESS.getMessage());
	}

	@Override
	public Response reviseStore(String store, String city) {
		// 防止輸入為空
		if (!StringUtils.hasText(store) || !StringUtils.hasText(city)) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// 檢查存在
		boolean isExist = storeDao.existsById(store);
		if (!isExist) {
			return new Response(RtnCode.NOT_FOUND.getMessage());
		}
		// 更新資料
		storeDao.updateStoreCityById(store, city);
		return new Response(RtnCode.SUCCESSFUL.getMessage());
	}

	@Override
	public Response deleteStore(String store) {
		// 防止輸入為空
		if (!StringUtils.hasText(store)) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// 檢查存在
		boolean isExist = storeDao.existsById(store);
		if (!isExist) {
			return new Response(RtnCode.NOT_FOUND.getMessage());
		}
		// 檢查菜單存在
		List<Menu> res = menuDao.findByStoreName(store);
		if (!CollectionUtils.isEmpty(res)) {
			for (Menu m : res) {
				MenuId menuId = new MenuId(m.getMenu(), m.getStoreName());
				deleteMenu(menuId);
			}
		}
		storeDao.deleteById(store);
		return new Response(RtnCode.SUCCESSFUL.getMessage());
	}

}
