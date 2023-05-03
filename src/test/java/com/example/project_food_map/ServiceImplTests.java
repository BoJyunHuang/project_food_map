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
import com.example.project_food_map.service.ifs.FindService;
import com.example.project_food_map.service.impl.StoreAndMenuServiceImpl;
import com.example.project_food_map.vo.Request;
import com.example.project_food_map.vo.Response;

@SpringBootTest(classes = ProjectFoodMapApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 為了可以使用@BeforeAll和@AfterAll
public class ServiceImplTests {

	@Autowired
	private MenuDao menuDao;

	@Autowired
	private StoreDao storeDao;

	@Autowired
	private StoreAndMenuServiceImpl smSI;

	@Autowired
	private FindService fS;

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
	public void addMenuTest() {
		// 狀況:輸入空白
		Response res1 = smSI.addMenu(null, "TX1", 200, 3);
		Assert.isTrue(res1.getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// 狀況:價格負數
		Response res2 = smSI.addMenu("menu4", "TX1", -200, 3);
		Assert.isTrue(res2.getMessage().equals(RtnCode.OUT_OF_LIMIT.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 況狀:評分超過範圍(1~5)
		Response res3 = smSI.addMenu("menu4", "TX1", 200, 8);
		Assert.isTrue(res3.getMessage().equals(RtnCode.OUT_OF_LIMIT.getMessage()), RtnCode.TEST3_ERROR.getMessage());
		// 況狀:菜單重複
		Response res4 = smSI.addMenu("menu1", "TX1", 200, 3);
		Assert.isTrue(res4.getMessage().equals(RtnCode.ALREADY_EXISTED.getMessage()), RtnCode.TEST4_ERROR.getMessage());
		// 況狀:成功
		Response res5 = smSI.addMenu("menu4", "TX1", 200, 3);
		Assert.isTrue(res5.getMessage().equals(RtnCode.SUCCESSFUL.getMessage()), RtnCode.TEST5_ERROR.getMessage());
		MenuId menuId = new MenuId("menu4", "TX1");
		menuDao.deleteById(menuId);
	}

	@Test
	public void reviseMenuTest() {
		// 狀況:輸入空白
		Request r = new Request();
		Response res1 = smSI.reviseMenu(r);
		Assert.isTrue(res1.getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// 狀況:物件不存在
		r.setMenuId(new MenuId("menuX", "TX1"));
		Response res2 = smSI.reviseMenu(r);
		Assert.isTrue(res2.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 況狀:未修改任何物件
		r.setMenuId(new MenuId("menu1", "TX1"));
		Response res3 = smSI.reviseMenu(r);
		Assert.isTrue(res3.getMessage().equals(RtnCode.INCORRECT.getMessage()), RtnCode.TEST3_ERROR.getMessage());
		// 況狀:成功
		r.setPrice(250);
		Response res4 = smSI.reviseMenu(r);
		Assert.isTrue(res4.getMessage().equals(RtnCode.SUCCESSFUL.getMessage()), RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	public void deleteMenuTest() {
		// 狀況:輸入空白
		MenuId menuId = new MenuId();
		Response res1 = smSI.deleteMenu(menuId);
		Assert.isTrue(res1.getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// 狀況:物件不存在
		menuId.setMenu("menu1");
		menuId.setStoreName("TXX");
		Response res2 = smSI.deleteMenu(menuId);
		Assert.isTrue(res2.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 況狀:成功
		menuId.setStoreName("TX1");
		Response res4 = smSI.deleteMenu(menuId);
		Assert.isTrue(res4.getMessage().equals(RtnCode.SUCCESSFUL.getMessage()), RtnCode.TEST4_ERROR.getMessage());
		Menu menu1 = new Menu("menu1", "TX1", 200, 3);
		menuDao.save(menu1);
	}

	@Test
	public void addStoreTest() {
		// 狀況:輸入空白
		Response res1 = smSI.addStore(null, "city1");
		Assert.isTrue(res1.getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// 狀況:輸入重複
		Response res2 = smSI.addStore("TX1", "city1");
		Assert.isTrue(res2.getMessage().equals(RtnCode.ALREADY_EXISTED.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 狀況:成功新增
		Response res3 = smSI.addStore("TX10", "city1");
		Assert.isTrue(res3.getMessage().equals(RtnCode.SUCCESS.getMessage()), RtnCode.TEST3_ERROR.getMessage());
		Store s1 = new Store("TX10", "city1");
		storeDao.delete(s1);
	}

	@Test
	public void reviseStoreTest() {
		// 狀況:輸入空白
		Response res1 = smSI.reviseStore(null, "city1");
		Assert.isTrue(res1.getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// 狀況:不存在物件
		Response res2 = smSI.reviseStore("TXX", "city1");
		Assert.isTrue(res2.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 狀況:成功修改
		Response res3 = smSI.reviseStore("TX1", "city5");
		Assert.isTrue(res3.getMessage().equals(RtnCode.SUCCESSFUL.getMessage()), RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	public void deleteStoreTest() {
		// 狀況:輸入空白
		Store s = new Store("TXX", "cityX");
		storeDao.save(s);
		Response res1 = smSI.deleteStore(null);
		Assert.isTrue(res1.getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// 狀況:不存在物件
		Response res2 = smSI.deleteStore("TX99");
		Assert.isTrue(res2.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 狀況:成功修改
		Response res3 = smSI.deleteStore("TXX");
		Assert.isTrue(res3.getMessage().equals(RtnCode.SUCCESSFUL.getMessage()), RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	public void findStoreByCityLimitTest() {
		// 狀況:輸入空白
		Response res1 = fS.findStoreByCityLimit(null, 3);
		Assert.isTrue(res1.getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// 狀況:限制搜索筆數錯誤
		Response res2 = fS.findStoreByCityLimit("city1", -3);
		Assert.isTrue(res2.getMessage().equals(RtnCode.INCORRECT.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 狀況:不存在物件
		Response res3 = fS.findStoreByCityLimit("cityX", 3);
		Assert.isTrue(res3.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST3_ERROR.getMessage());
		// 狀況:成功
		Response res4 = fS.findStoreByCityLimit("city1", 3);
		Assert.isTrue(res4.getMessage().equals(RtnCode.SUCCESS.getMessage()), RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	public void findStoreByPointTest() {
		// 狀況:輸入範圍錯誤
		Response res1 = fS.findStoreByPoint(5.1);
		Assert.isTrue(res1.getMessage().equals(RtnCode.OUT_OF_LIMIT.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// 狀況:不存在符合條件物件
		Response res2 = fS.findStoreByPoint(4.9);
		Assert.isTrue(res2.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 狀況:成功
		Response res3 = fS.findStoreByPoint(3.5);
		Assert.isTrue(res3.getMessage().equals(RtnCode.SUCCESS.getMessage()), RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	public void findStoreAndMenuByPoint() {
		// 狀況:輸入範圍錯誤
		Response res1 = fS.findStoreAndMenuByPoint(5.1, 3);
		Assert.isTrue(res1.getMessage().equals(RtnCode.OUT_OF_LIMIT.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// 狀況:不存在符合條件物件
		Response res2 = fS.findStoreAndMenuByPoint(4.9, 3);
		Assert.isTrue(res2.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 狀況:成功
		Response res3 = fS.findStoreAndMenuByPoint(3.5, 4);
		Assert.isTrue(res3.getMessage().equals(RtnCode.SUCCESS.getMessage()), RtnCode.TEST3_ERROR.getMessage());
	}

}
