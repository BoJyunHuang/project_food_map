package com.example.project_food_map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.project_food_map.constants.RtnCode;
import com.example.project_food_map.entity.Menu;
import com.example.project_food_map.entity.MenuId;
import com.example.project_food_map.entity.Store;
import com.example.project_food_map.repository.MenuDao;
import com.example.project_food_map.repository.StoreDao;
import com.example.project_food_map.vo.StoreAndMenu;

@SpringBootTest(classes = ProjectFoodMapApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 為了可以使用@BeforeAll和@AfterAll
public class DaoTests {

	@Autowired
	private MenuDao menuDao;

	@Autowired
	private StoreDao storeDao;

	@BeforeEach
	public void BeforeEach() {
		// 測試資料，店家三間
		Store s1 = new Store("TX1", "city1");
		s1.setPoint(3.3);
		Store s2 = new Store("TX2", "city1");
		s2.setPoint(4.5);
		Store s3 = new Store("TX3", "city2");
		List<Store> storeList = new ArrayList<>(Arrays.asList(s1, s2, s3));
		// 餐點五樣
		Menu m1 = new Menu("menu1", "TX1", 200, 3);
		Menu m2 = new Menu("menu2", "TX1", 250, 4);
		Menu m3 = new Menu("menu3", "TX1", 300, 3);
		Menu m4 = new Menu("menu1", "TX2", 350, 4);
		Menu m5 = new Menu("menu2", "TX2", 400, 5);
		List<Menu> menuList = new ArrayList<>(Arrays.asList(m1, m2, m3, m4, m5));
		storeDao.saveAll(storeList);
		menuDao.saveAll(menuList);
	}

	@AfterAll
	public void AfterAll() {
		// 清除測試資料
		List<String> storeList = new ArrayList<>(Arrays.asList("TX1", "TX2", "TX3"));
		MenuId menuId1 = new MenuId("menu1", "TX1");
		MenuId menuId2 = new MenuId("menu2", "TX1");
		MenuId menuId3 = new MenuId("menu3", "TX1");
		MenuId menuId4 = new MenuId("menu1", "TX2");
		MenuId menuId5 = new MenuId("menu2", "TX2");
		List<MenuId> menuIdList = new ArrayList<>(Arrays.asList(menuId1, menuId2, menuId3, menuId4, menuId5));
		storeDao.deleteAllById(storeList);
		menuDao.deleteAllById(menuIdList);
	}

	@Test
	public void updateStoreCityByIdTest() {
		// 變更城市測試
		int res1 = storeDao.updateStoreCityById("TX1", "city3");
		Assert.isTrue(res1 == 1, RtnCode.TEST1_ERROR.getMessage());
		// 變更城市不存在
		int res2 = storeDao.updateStoreCityById("TXX", "city3");
		Assert.isTrue(res2 == 0, RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	public void updateStorePointByIdTest() {
		// 變更分數測試
		int res1 = storeDao.updatePointById("TX1", 5.0);
		Assert.isTrue(res1 == 1, RtnCode.TEST1_ERROR.getMessage());
		// 變更店家不存在
		int res2 = storeDao.updatePointById("TXX", 4.7);
		Assert.isTrue(res2 == 0, RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	public void findStoreAndMenuByCityLimitTest() {
		// 查詢城市中店家
		List<StoreAndMenu> res1 = storeDao.findStoreAndMenuByCityLimit("city1");
		Assert.isTrue(res1.subList(0, 3).size() == 3, RtnCode.TEST1_ERROR.getMessage());
	}

	@Test
	public void findStoreAndMenuByPointGreaterEqualThanTest() {
		// 查詢評價以上店家
		List<StoreAndMenu> res1 = storeDao.findStoreAndMenuByPointGreaterEqualThan(3);
		Assert.isTrue(res1.size() == 5, RtnCode.TEST1_ERROR.getMessage());
	}

	@Test
	public void findStoreAndMenuByPointAndMenuPointGreaterEqualThanTest() {
		// 查詢評價以上及餐點評價以上店家
		List<StoreAndMenu> res1 = storeDao.findStoreAndMenuByPointAndMenuPointGreaterEqualThan(3, 4);
		Assert.isTrue(res1.size() == 3, RtnCode.TEST1_ERROR.getMessage());
	}

	@Test
	public void updateMenuPriceByMenuIdTest() {
		// 更改價錢
		MenuId menuId = new MenuId("menu1", "TX1");
		int res1 = menuDao.updateMenuPriceByMenuId(menuId.getMenu(), menuId.getStoreName(), 1000);
		Assert.isTrue(res1 == 1, RtnCode.TEST1_ERROR.getMessage());
		// 變更店家不存在
		menuId.setMenu("menuX");
		int res2 = menuDao.updateMenuPriceByMenuId(menuId.getMenu(), menuId.getStoreName(), 1000);
		Assert.isTrue(res2 == 0, RtnCode.TEST2_ERROR.getMessage());
	}
	
	@Test
	public void updateMenuPointByMenuIdTest() {
		// 更改評價
		MenuId menuId = new MenuId("menu1", "TX1");
		int res1 = menuDao.updateMenuPointByMenuId(menuId.getMenu(), menuId.getStoreName(), 1);
		Assert.isTrue(res1 == 1, RtnCode.TEST1_ERROR.getMessage());
		// 變更店家不存在
		menuId.setMenu("menuX");
		int res2 = menuDao.updateMenuPointByMenuId(menuId.getMenu(), menuId.getStoreName(), 5);
		Assert.isTrue(res2 == 0, RtnCode.TEST2_ERROR.getMessage());
	}
	
	@Test
	public void getAverageMenuPointTest() {
		// 取得評價平均
		 double res1 = menuDao.getAverageMenuPoint("TX1");
		Assert.isTrue(res1 == 3.3, RtnCode.TEST1_ERROR.getMessage());
	}

}
