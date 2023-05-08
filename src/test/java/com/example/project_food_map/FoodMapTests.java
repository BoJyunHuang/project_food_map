package com.example.project_food_map;

import java.util.ArrayList;
import java.util.Arrays;

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
import com.example.project_food_map.service.impl.FoodMapServiceImpl;

@SpringBootTest(classes = ProjectFoodMapApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 為了可以使用@BeforeAll和@AfterAll
public class FoodMapTests {

	@Autowired
	private MenuDao menuDao;

	@Autowired
	private StoreDao storeDao;

	@Autowired
	private FoodMapServiceImpl fMSI;

	@BeforeEach
	public void BeforeEach() {
		// 測試資料，店家三間
		storeDao.saveAll(new ArrayList<>(Arrays.asList(new Store("TX1", "city1", 3.3), new Store("TX2", "city1", 4.5),
				new Store("TX3", "city2"))));
		// 餐點五樣
		menuDao.saveAll(new ArrayList<>(Arrays.asList(new Menu("menu1", "TX1", 200, 3),
				new Menu("menu2", "TX1", 250, 4), new Menu("menu3", "TX1", 300, 3), new Menu("menu1", "TX2", 350, 4),
				new Menu("menu2", "TX2", 400, 5))));
	}

	@AfterAll
	public void AfterAll() {
		// 清除測試資料
		storeDao.deleteAllById(new ArrayList<>(Arrays.asList("TX1", "TX2", "TX3")));
		menuDao.deleteAllById(new ArrayList<>(Arrays.asList(new MenuId("menu1", "TX1"), new MenuId("menu2", "TX1"),
				new MenuId("menu3", "TX1"), new MenuId("menu1", "TX2"), new MenuId("menu2", "TX2"))));
	}

	@Test
	public void insertMenuTest() {
		// 新增空菜單(會新增，要注意)
		// Assert.isTrue(menuDao.insertMenu("", "", 0, 0) == 1, RtnCode.TEST1_ERROR.getMessage());
		// 新增菜單已存在
		Assert.isTrue(menuDao.insertMenu("menu1", "TX1", 200, 3) == 0, RtnCode.TEST2_ERROR.getMessage());
		// 新增菜單成功
		Assert.isTrue(menuDao.insertMenu("menuX", "TX1", 200, 3) == 1, RtnCode.TEST2_ERROR.getMessage());
		menuDao.deleteById(new MenuId("menuX", "TX1"));
	}

	@Test
	public void updateMenuPriceByMenuIdTest() {
		// 更改價錢
		Assert.isTrue(menuDao.updateMenuPriceByMenuId("menu1", "TX1", 1000) == 1, RtnCode.TEST1_ERROR.getMessage());
		// 變更店家不存在
		Assert.isTrue(menuDao.updateMenuPriceByMenuId("menuX", "TX1", 1000) == 0, RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	public void updateMenuPointByMenuIdTest() {
		// 更改評價
		Assert.isTrue(menuDao.updateMenuPointByMenuId("menu1", "TX1", 1) == 1, RtnCode.TEST1_ERROR.getMessage());
		// 變更店家不存在
		Assert.isTrue(menuDao.updateMenuPointByMenuId("menuX", "TX1", 5) == 0, RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	public void deleteMenuTest() {
		// 刪除失敗
		Assert.isTrue(menuDao.deleteMenu("menuX", "TX1") == 0, RtnCode.TEST1_ERROR.getMessage());
		// 刪除成功
		menuDao.save(new Menu("menuX", "TX1", 100, 1));
		Assert.isTrue(menuDao.deleteMenu("menuX", "TX1") == 1, RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	public void getAverageMenuPointTest() {
		// 沒有菜單
		Assert.isTrue(menuDao.getAverageMenuPoint("TXX") == null, RtnCode.TEST1_ERROR.getMessage());
		// 取得評價平均
		Assert.isTrue(menuDao.getAverageMenuPoint("TX1") == 3.3, RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	public void insertStoreTest() {
		// 新增空店家(會新增，要注意) // null不行
		// Assert.isTrue(storeDao.insertStore("", "", 0) == 1, RtnCode.TEST1_ERROR.getMessage());
		// 新增店家已存在
		Assert.isTrue(storeDao.insertStore("TX1", "city1", 0) == 0, RtnCode.TEST2_ERROR.getMessage());
		// 新增店家成功
		Assert.isTrue(storeDao.insertStore("TXX", "city1", 0) == 1, RtnCode.TEST3_ERROR.getMessage());
		storeDao.deleteById("TXX");
	}

	@Test
	public void updateStoreCityByIdTest() {
		// null測試
		Assert.isTrue(storeDao.updateStoreCityById(null, null) == 0, RtnCode.TEST1_ERROR.getMessage());
		// 變更城市測試
		Assert.isTrue(storeDao.updateStoreCityById("TX1", "city3") == 1, RtnCode.TEST2_ERROR.getMessage());
		// 變更店家不存在
		Assert.isTrue(storeDao.updateStoreCityById("TXX", "city3") == 0, RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	public void updateStorePointByIdTest() {
		// 變更分數測試
		Assert.isTrue(storeDao.updatePointById("TX1", 5.0) == 1, RtnCode.TEST1_ERROR.getMessage());
		// 變更店家不存在
		Assert.isTrue(storeDao.updatePointById("TXX", 4.7) == 0, RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	public void deleteStoreTest() {
		// 刪除null
		Assert.isTrue(storeDao.deleteStore(null) == 0, RtnCode.TEST1_ERROR.getMessage());
		// 刪除失敗
		Assert.isTrue(storeDao.deleteStore("TXX") == 0, RtnCode.TEST2_ERROR.getMessage());
		// 刪除成功
		storeDao.save(new Store("TXX", "city1", 1));
		Assert.isTrue(storeDao.deleteStore("TXX") == 1, RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	public void findStoreAndMenuByCityTest() {
		// 查詢null
		Assert.isTrue(storeDao.findStoreAndMenuByCity(null).size() == 0, RtnCode.TEST1_ERROR.getMessage());
		// 查詢""
		Assert.isTrue(storeDao.findStoreAndMenuByCity("").size() == 0, RtnCode.TEST2_ERROR.getMessage());
		// 查詢城市不存在
		Assert.isTrue(storeDao.findStoreAndMenuByCity("cityX").size() == 0, RtnCode.TEST3_ERROR.getMessage());
		// 查詢城市中店家菜單數量
		Assert.isTrue(storeDao.findStoreAndMenuByCity("city1").size() == 5, RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	public void findStoreAndMenuByPointGreaterEqualThanTest() {
		// 查詢評價以上店家
		Assert.isTrue(storeDao.findByStorePointGreaterEqualThan(4.1).size() == 2, RtnCode.TEST1_ERROR.getMessage());
		// 查詢評價以上不存在
		Assert.isTrue(storeDao.findByStorePointGreaterEqualThan(5).size() == 0, RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	public void findByStoreAndMenuPointGreaterEqualThanTest() {
		// 查詢評價以上及餐點評價以上店家
		Assert.isTrue(storeDao.findByStoreAndMenuPointGreaterEqualThan(3, 4).size() == 3,
				RtnCode.TEST1_ERROR.getMessage());
	}

	@Test
	public void addMenuTest() {
		// 狀況:輸入空白
		Assert.isTrue(fMSI.addMenu(null, "TX1", 200, 3).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// 狀況:價格負數
		Assert.isTrue(fMSI.addMenu("menu4", "TX1", -200, 3).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// 況狀:評分超過範圍(1~5)
		Assert.isTrue(fMSI.addMenu("menu4", "TX1", 200, 8).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		// 況狀:菜單重複
		Assert.isTrue(fMSI.addMenu("menu1", "TX1", 200, 3).getMessage().equals(RtnCode.ALREADY_EXISTED.getMessage()),
				RtnCode.TEST4_ERROR.getMessage());
		// 況狀:成功
		Assert.isTrue(fMSI.addMenu("menu4", "TX1", 200, 3).getMessage().equals(RtnCode.SUCCESSFUL.getMessage()),
				RtnCode.TEST5_ERROR.getMessage());
		menuDao.deleteById(new MenuId("menu4", "TX1"));
	}

	@Test
	public void reviseMenuTest() {
		// 狀況:輸入空白
		Assert.isTrue(fMSI.reviseMenu(new Menu()).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// 狀況:物件不存在
		Assert.isTrue(
				fMSI.reviseMenu(new Menu("menuX", "TX1", 0, 0)).getMessage().equals(RtnCode.INCORRECT.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// 況狀:未修改任何物件
		Assert.isTrue(
				fMSI.reviseMenu(new Menu("menu1", "TX1", 0, 0)).getMessage().equals(RtnCode.INCORRECT.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		// 況狀:成功
		Assert.isTrue(
				fMSI.reviseMenu(new Menu("menu1", "TX1", 250, 4)).getMessage().equals(RtnCode.SUCCESSFUL.getMessage()),
				RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	public void deleteMenuImplTest() {
		// 狀況:輸入空白
		Assert.isTrue(fMSI.deleteMenu(new MenuId()).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// 狀況:物件不存在
		Assert.isTrue(fMSI.deleteMenu(new MenuId("menu1", "TXX")).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// 況狀:成功
		Assert.isTrue(fMSI.deleteMenu(new MenuId("menu1", "TX1")).getMessage().equals(RtnCode.SUCCESSFUL.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		menuDao.save(new Menu("menu1", "TX1", 200, 3));
	}

	@Test
	public void addStoreTest() {
		// 狀況:輸入空白
		Assert.isTrue(fMSI.addStore(null, "city1").getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// 狀況:輸入重複
		Assert.isTrue(fMSI.addStore("TX1", "city1").getMessage().equals(RtnCode.ALREADY_EXISTED.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// 狀況:成功新增
		Assert.isTrue(fMSI.addStore("TX10", "city1").getMessage().equals(RtnCode.SUCCESSFUL.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		storeDao.delete(new Store("TX10", "city1"));
	}

	@Test
	public void reviseStoreTest() {
		// 狀況:輸入空白
		Assert.isTrue(fMSI.reviseStore(null, "city1").getMessage().equals(RtnCode.INCORRECT.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// 狀況:不存在物件
		Assert.isTrue(fMSI.reviseStore("TXX", "city1").getMessage().equals(RtnCode.INCORRECT.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// 狀況:成功修改
		Assert.isTrue(fMSI.reviseStore("TX1", "city5").getMessage().equals(RtnCode.SUCCESSFUL.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	public void deleteStoreImplTest() {
		// 狀況:輸入空白
		storeDao.save(new Store("TXX", "cityX"));
		Assert.isTrue(fMSI.deleteStore(null).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// 狀況:不存在物件
		Assert.isTrue(fMSI.deleteStore("TX99").getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// 狀況:成功刪除
		menuDao.save(new Menu("menuX", "TXX", 100, 4));
		Assert.isTrue(fMSI.deleteStore("TXX").getMessage().equals(RtnCode.SUCCESSFUL.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	public void findStoreByCityLimitTest() {
		// 狀況:輸入空白
		Assert.isTrue(fMSI.findStoreByCityLimit(null, 3).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// 狀況:限制搜索筆數錯誤
		Assert.isTrue(fMSI.findStoreByCityLimit("city1", -3).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// 狀況:不存在物件
		Assert.isTrue(fMSI.findStoreByCityLimit("cityX", 3).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		// 狀況:成功
		Assert.isTrue(fMSI.findStoreByCityLimit("city1", 3).getMessage().equals(RtnCode.SUCCESS.getMessage()),
				RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	public void findStoreByPointTest() {
		// 狀況:輸入範圍錯誤
		Assert.isTrue(fMSI.findStoreByPoint(5.1).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// 狀況:不存在符合條件物件
		Assert.isTrue(fMSI.findStoreByPoint(4.9).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// 狀況:成功
		Assert.isTrue(fMSI.findStoreByPoint(3.5).getMessage().equals(RtnCode.SUCCESS.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	public void findStoreAndMenuByPoint() {
		// 狀況:輸入範圍錯誤
		Assert.isTrue(fMSI.findStoreAndMenuByPoint(5.1, 3).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// 狀況:不存在符合條件物件
		Assert.isTrue(fMSI.findStoreAndMenuByPoint(4.9, 3).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// 狀況:成功
		Assert.isTrue(fMSI.findStoreAndMenuByPoint(3.5, 4).getMessage().equals(RtnCode.SUCCESS.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
	}

}
