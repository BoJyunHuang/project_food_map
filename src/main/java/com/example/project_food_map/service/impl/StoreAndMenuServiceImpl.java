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
		// �����J���šB�������t�B�������A�d��
		if (!StringUtils.hasText(menu) || !StringUtils.hasText(storeName)) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		if (price < 0 || menuPoint > 5 || menuPoint < 0) {
			return new Response(RtnCode.OUT_OF_LIMIT.getMessage());
		}
		// �ˬd����
		MenuId menuId = new MenuId(menu, storeName);
		boolean isExist = menuDao.existsById(menuId);
		if (isExist) {
			return new Response(RtnCode.ALREADY_EXISTED.getMessage());
		}
		// �x�s�s��
		Menu newMenu = new Menu(menu, storeName, price, menuPoint);
		return new Response(menuDao.save(newMenu), RtnCode.SUCCESS.getMessage());
	}

	@Override
	public Response reviseMenu(Request request) {
		boolean flag = false;
		// �����J����
		if (request == null || request.getMenuId() == null || request.getMenuId().getMenu() == null
				|| request.getMenuId().getStoreName() == null) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// �T�{�ؼЦs�b
		boolean isExist = menuDao.existsById(request.getMenuId());
		if (!isExist) {
			return new Response(RtnCode.NOT_FOUND.getMessage());
		}
		// �ק����
		if (request.getPrice() > 0) {
			menuDao.updateMenuPriceByMenuId(request.getMenuId(), request.getPrice());
			flag = true;
		}
		// �ק����
		if (request.getMenuPoint() > 0 || request.getMenuPoint() < 6) {
			menuDao.updateMenuPriceByMenuId(request.getMenuId(), request.getMenuPoint());
			// ���o�����íק��`��
			double avg = menuDao.getAverageMenuPoint(request.getMenuId());
			storeDao.updateStorePointById(request.getMenuId().getStoreName(), avg);
			flag = true;
		}
		if (!flag) {
			return new Response(RtnCode.INCORRECT.getMessage());
		}
		return new Response(RtnCode.SUCCESSFUL.getMessage());
	}

	@Override
	public Response deleteMenu(MenuId menuId) {
		// �����J����
		if (menuId == null || StringUtils.hasText(menuId.getMenu()) || StringUtils.hasText(menuId.getStoreName())) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// �T�{�\��s�b
		boolean isExist = menuDao.existsById(menuId);
		if (!isExist) {
			return new Response(RtnCode.NOT_FOUND.getMessage());
		}
		// �R�����
		menuDao.deleteById(menuId);
		// ���o�����íק��`��
		double avg = menuDao.getAverageMenuPoint(menuId);
		storeDao.updateStorePointById(menuId.getStoreName(), avg);
		return new Response(RtnCode.SUCCESSFUL.getMessage());
	}

	@Override
	public Response addStore(String store, String city) {
		// �����J����
		if (!StringUtils.hasText(store) || !StringUtils.hasText(city)) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// �ˬd����
		boolean isExist = storeDao.existsById(store);
		if (isExist) {
			return new Response(RtnCode.ALREADY_EXISTED.getMessage());
		}
		// �x�s�s��
		Store newStore = new Store(store, city);
		return new Response(storeDao.save(newStore), RtnCode.SUCCESS.getMessage());
	}

	@Override
	public Response reviseStore(String store, String city) {
		// �����J����
		if (!StringUtils.hasText(store) || !StringUtils.hasText(city)) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// �ˬd�s�b
		boolean isExist = storeDao.existsById(store);
		if (!isExist) {
			return new Response(RtnCode.NOT_FOUND.getMessage());
		}
		// ��s���
		storeDao.updateStoreCityById(store, city);
		return new Response(RtnCode.SUCCESSFUL.getMessage());
	}

	@Override
	public Response deleteStore(String store) {
		// �����J����
		if (StringUtils.hasText(store)) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// �ˬd�s�b
		boolean isExist = storeDao.existsById(store);
		if (!isExist) {
			return new Response(RtnCode.NOT_FOUND.getMessage());
		}
		// �ˬd���s�b
		List<Menu> res = menuDao.findByStoreName(store);
		if (!CollectionUtils.isEmpty(res)){
			for (Menu m : res) {
				MenuId menuId = new MenuId(m.getMenu(),m.getStoreName());
				deleteMenu(menuId);
			}
		}
		storeDao.deleteById(store);
		return new Response(RtnCode.SUCCESSFUL.getMessage());
	}

}
