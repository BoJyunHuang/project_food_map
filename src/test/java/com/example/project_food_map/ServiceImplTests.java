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
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // ���F�i�H�ϥ�@BeforeAll�M@AfterAll
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
		// ���ո�ơA���a�T��
		Store s1 = new Store("TX1", "city1");
		s1.setPoint(3.3);
		Store s2 = new Store("TX2", "city1");
		s2.setPoint(4.5);
		Store s3 = new Store("TX3", "city2");
		List<Store> storeList = new ArrayList<>(Arrays.asList(s1, s2, s3));
		// �\�I����
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
		// �M�����ո��
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
		// ���p:��J�ť�
		Response res1 = smSI.addMenu(null, "TX1", 200, 3);
		Assert.isTrue(res1.getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// ���p:����t��
		Response res2 = smSI.addMenu("menu4", "TX1", -200, 3);
		Assert.isTrue(res2.getMessage().equals(RtnCode.OUT_OF_LIMIT.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// �p��:�����W�L�d��(1~5)
		Response res3 = smSI.addMenu("menu4", "TX1", 200, 8);
		Assert.isTrue(res3.getMessage().equals(RtnCode.OUT_OF_LIMIT.getMessage()), RtnCode.TEST3_ERROR.getMessage());
		// �p��:��歫��
		Response res4 = smSI.addMenu("menu1", "TX1", 200, 3);
		Assert.isTrue(res4.getMessage().equals(RtnCode.ALREADY_EXISTED.getMessage()), RtnCode.TEST4_ERROR.getMessage());
		// �p��:���\
		Response res5 = smSI.addMenu("menu4", "TX1", 200, 3);
		Assert.isTrue(res5.getMessage().equals(RtnCode.SUCCESSFUL.getMessage()), RtnCode.TEST5_ERROR.getMessage());
		MenuId menuId = new MenuId("menu4", "TX1");
		menuDao.deleteById(menuId);
	}

	@Test
	public void reviseMenuTest() {
		// ���p:��J�ť�
		Request r = new Request();
		Response res1 = smSI.reviseMenu(r);
		Assert.isTrue(res1.getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// ���p:���󤣦s�b
		r.setMenuId(new MenuId("menuX", "TX1"));
		Response res2 = smSI.reviseMenu(r);
		Assert.isTrue(res2.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// �p��:���ק���󪫥�
		r.setMenuId(new MenuId("menu1", "TX1"));
		Response res3 = smSI.reviseMenu(r);
		Assert.isTrue(res3.getMessage().equals(RtnCode.INCORRECT.getMessage()), RtnCode.TEST3_ERROR.getMessage());
		// �p��:���\
		r.setPrice(250);
		Response res4 = smSI.reviseMenu(r);
		Assert.isTrue(res4.getMessage().equals(RtnCode.SUCCESSFUL.getMessage()), RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	public void deleteMenuTest() {
		// ���p:��J�ť�
		MenuId menuId = new MenuId();
		Response res1 = smSI.deleteMenu(menuId);
		Assert.isTrue(res1.getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// ���p:���󤣦s�b
		menuId.setMenu("menu1");
		menuId.setStoreName("TXX");
		Response res2 = smSI.deleteMenu(menuId);
		Assert.isTrue(res2.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// �p��:���\
		menuId.setStoreName("TX1");
		Response res4 = smSI.deleteMenu(menuId);
		Assert.isTrue(res4.getMessage().equals(RtnCode.SUCCESSFUL.getMessage()), RtnCode.TEST4_ERROR.getMessage());
		Menu menu1 = new Menu("menu1", "TX1", 200, 3);
		menuDao.save(menu1);
	}

	@Test
	public void addStoreTest() {
		// ���p:��J�ť�
		Response res1 = smSI.addStore(null, "city1");
		Assert.isTrue(res1.getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// ���p:��J����
		Response res2 = smSI.addStore("TX1", "city1");
		Assert.isTrue(res2.getMessage().equals(RtnCode.ALREADY_EXISTED.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// ���p:���\�s�W
		Response res3 = smSI.addStore("TX10", "city1");
		Assert.isTrue(res3.getMessage().equals(RtnCode.SUCCESS.getMessage()), RtnCode.TEST3_ERROR.getMessage());
		Store s1 = new Store("TX10", "city1");
		storeDao.delete(s1);
	}

	@Test
	public void reviseStoreTest() {
		// ���p:��J�ť�
		Response res1 = smSI.reviseStore(null, "city1");
		Assert.isTrue(res1.getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// ���p:���s�b����
		Response res2 = smSI.reviseStore("TXX", "city1");
		Assert.isTrue(res2.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// ���p:���\�ק�
		Response res3 = smSI.reviseStore("TX1", "city5");
		Assert.isTrue(res3.getMessage().equals(RtnCode.SUCCESSFUL.getMessage()), RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	public void deleteStoreTest() {
		// ���p:��J�ť�
		Store s = new Store("TXX", "cityX");
		storeDao.save(s);
		Response res1 = smSI.deleteStore(null);
		Assert.isTrue(res1.getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// ���p:���s�b����
		Response res2 = smSI.deleteStore("TX99");
		Assert.isTrue(res2.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// ���p:���\�ק�
		Response res3 = smSI.deleteStore("TXX");
		Assert.isTrue(res3.getMessage().equals(RtnCode.SUCCESSFUL.getMessage()), RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	public void findStoreByCityLimitTest() {
		// ���p:��J�ť�
		Response res1 = fS.findStoreByCityLimit(null, 3);
		Assert.isTrue(res1.getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// ���p:����j�����ƿ��~
		Response res2 = fS.findStoreByCityLimit("city1", -3);
		Assert.isTrue(res2.getMessage().equals(RtnCode.INCORRECT.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// ���p:���s�b����
		Response res3 = fS.findStoreByCityLimit("cityX", 3);
		Assert.isTrue(res3.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST3_ERROR.getMessage());
		// ���p:���\
		Response res4 = fS.findStoreByCityLimit("city1", 3);
		Assert.isTrue(res4.getMessage().equals(RtnCode.SUCCESS.getMessage()), RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	public void findStoreByPointTest() {
		// ���p:��J�d����~
		Response res1 = fS.findStoreByPoint(5.1);
		Assert.isTrue(res1.getMessage().equals(RtnCode.OUT_OF_LIMIT.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// ���p:���s�b�ŦX���󪫥�
		Response res2 = fS.findStoreByPoint(4.9);
		Assert.isTrue(res2.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// ���p:���\
		Response res3 = fS.findStoreByPoint(3.5);
		Assert.isTrue(res3.getMessage().equals(RtnCode.SUCCESS.getMessage()), RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	public void findStoreAndMenuByPoint() {
		// ���p:��J�d����~
		Response res1 = fS.findStoreAndMenuByPoint(5.1, 3);
		Assert.isTrue(res1.getMessage().equals(RtnCode.OUT_OF_LIMIT.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// ���p:���s�b�ŦX���󪫥�
		Response res2 = fS.findStoreAndMenuByPoint(4.9, 3);
		Assert.isTrue(res2.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// ���p:���\
		Response res3 = fS.findStoreAndMenuByPoint(3.5, 4);
		Assert.isTrue(res3.getMessage().equals(RtnCode.SUCCESS.getMessage()), RtnCode.TEST3_ERROR.getMessage());
	}

}
