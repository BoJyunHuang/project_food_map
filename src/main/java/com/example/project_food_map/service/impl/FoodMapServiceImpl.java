package com.example.project_food_map.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.project_food_map.constants.RtnCode;
import com.example.project_food_map.entity.Menu;
import com.example.project_food_map.entity.MenuId;
import com.example.project_food_map.repository.MenuDao;
import com.example.project_food_map.repository.StoreDao;
import com.example.project_food_map.service.ifs.FoodMapService;
import com.example.project_food_map.vo.Response;
import com.example.project_food_map.vo.StoreAndMenu;

@Service
public class FoodMapServiceImpl implements FoodMapService {

	@Autowired
	private StoreDao storeDao;

	@Autowired
	private MenuDao menuDao;

	@Override
	public Response addMenu(String menu, String storeName, int price, int menuPoint) {
		// 0.防止輸入為空、價錢為負、評價不再範圍內
		if (!StringUtils.hasText(menu) || !StringUtils.hasText(storeName) || price < 0 || menuPoint > 5
				|| menuPoint < 0) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// 1.新增菜單
		return menuDao.insertMenu(menu, storeName, price, menuPoint) == 0
				? new Response(RtnCode.ALREADY_EXISTED.getMessage())
				// 2.更新店家評分
				: storeDao.updatePointById(storeName, menuDao.getAverageMenuPoint(storeName)) == 0
						? new Response(RtnCode.INCORRECT.getMessage())
						: new Response(RtnCode.SUCCESSFUL.getMessage());
	}

	@Override
	public Response reviseMenu(Menu menu) {
		// 0.防止輸入為空
		if (menu == null || menu.getMenu() == null || menu.getStoreName() == null) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// 1-1.修改價錢
		int rev1 = menu.getPrice() > 0
				? menuDao.updateMenuPriceByMenuId(menu.getMenu(), menu.getStoreName(), menu.getPrice())
				: 0;
		// 1-2.修改評分
		int rev2 = (menu.getMenuPoint() > 0 && menu.getMenuPoint() < 6)
				// 取得平均並修改總分
				? (menuDao.updateMenuPointByMenuId(menu.getMenu(), menu.getStoreName(), menu.getMenuPoint()) == 1
						? storeDao.updatePointById(menu.getStoreName(),
								menuDao.getAverageMenuPoint(menu.getStoreName()))
						: 0)
				: 0;
		// 2.確認修改
		return (rev1 == 0 && rev2 == 0) ? new Response(RtnCode.INCORRECT.getMessage())
				: new Response(RtnCode.SUCCESSFUL.getMessage());
	}

	@Override
	public Response deleteMenu(MenuId menuId) {
		// 0.防止輸入為空
		if (menuId == null || !StringUtils.hasText(menuId.getStoreName()) || !StringUtils.hasText(menuId.getMenu())) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// 1.刪除菜單
		if (menuDao.deleteMenu(menuId.getMenu(), menuId.getStoreName()) == 1) {
			// 取得平均並修改總分
			storeDao.updatePointById(menuId.getStoreName(), menuDao.getAverageMenuPoint(menuId.getStoreName()));
			return new Response(RtnCode.SUCCESSFUL.getMessage());
		}
		return new Response(RtnCode.NOT_FOUND.getMessage());
	}

	@Override
	public Response addStore(String store, String city) {
		// 0.防止輸入為空
		if (!StringUtils.hasText(store) || !StringUtils.hasText(city)) {
			return new Response(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// 1.新增店家
		return storeDao.insertStore(store, city, 0) == 0 ? new Response(RtnCode.ALREADY_EXISTED.getMessage())
				: new Response(RtnCode.SUCCESSFUL.getMessage());
	}

	@Override
	public Response reviseStore(String store, String city) {
		// 1.更新資訊
		return storeDao.updateStoreCityById(store, city) == 0 ? new Response(RtnCode.INCORRECT.getMessage())
				: new Response(RtnCode.SUCCESSFUL.getMessage());
	}

	@Override
	public Response deleteStore(String store) {
		// 1.確認店家存在
		if (storeDao.existsById(StringUtils.hasText(store) ? store : "")) {
			// 2.刪除菜單
			List<Menu> res = menuDao.findByStoreName(store);
			if (!CollectionUtils.isEmpty(res)) {
				for (int i = 0; i < res.size(); i++) {
					menuDao.deleteMenu(res.get(i).getMenu(), res.get(i).getStoreName());
				}
			}
			return storeDao.deleteStore(store) == 0 ? new Response(RtnCode.INCORRECT.getMessage())
					: new Response(RtnCode.SUCCESSFUL.getMessage());
		}
		return new Response(RtnCode.NOT_FOUND.getMessage());
	}

	@Override
	public Response findStoreByCityLimit(String city, int times) {
		// 1.搜尋城市
		List<StoreAndMenu> res = storeDao.findStoreAndMenuByCity(city);
		return (CollectionUtils.isEmpty(res) || times < 0) ? new Response(RtnCode.NOT_FOUND.getMessage())
				: new Response(res.subList(0, times), RtnCode.SUCCESS.getMessage());
	}

	@Override
	public Response findStoreByPoint(double point) {
		// 尋找範圍內結果
		List<StoreAndMenu> res = storeDao.findByStorePointGreaterEqualThan(point);
		return CollectionUtils.isEmpty(res) ? new Response(RtnCode.NOT_FOUND.getMessage())
				: new Response(res, RtnCode.SUCCESS.getMessage());
	}

	@Override
	public Response findStoreAndMenuByPoint(double point, int menuPoint) {
		// 尋找範圍內結果
		List<StoreAndMenu> res = storeDao.findByStoreAndMenuPointGreaterEqualThan(point, menuPoint);
		return CollectionUtils.isEmpty(res) ? new Response(RtnCode.NOT_FOUND.getMessage())
				: new Response(res, RtnCode.SUCCESS.getMessage());
	}

}
