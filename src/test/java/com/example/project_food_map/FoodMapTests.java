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
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // ���F�i�H�ϥ�@BeforeAll�M@AfterAll
public class FoodMapTests {

	@Autowired
	private MenuDao menuDao;

	@Autowired
	private StoreDao storeDao;

	@Autowired
	private FoodMapServiceImpl fMSI;

	@BeforeEach
	public void BeforeEach() {
		// ���ո�ơA���a�T��
		storeDao.saveAll(new ArrayList<>(Arrays.asList(new Store("TX1", "city1", 3.3), new Store("TX2", "city1", 4.5),
				new Store("TX3", "city2"))));
		// �\�I����
		menuDao.saveAll(new ArrayList<>(Arrays.asList(new Menu("menu1", "TX1", 200, 3),
				new Menu("menu2", "TX1", 250, 4), new Menu("menu3", "TX1", 300, 3), new Menu("menu1", "TX2", 350, 4),
				new Menu("menu2", "TX2", 400, 5))));
	}

	@AfterAll
	public void AfterAll() {
		// �M�����ո��
		storeDao.deleteAllById(new ArrayList<>(Arrays.asList("TX1", "TX2", "TX3")));
		menuDao.deleteAllById(new ArrayList<>(Arrays.asList(new MenuId("menu1", "TX1"), new MenuId("menu2", "TX1"),
				new MenuId("menu3", "TX1"), new MenuId("menu1", "TX2"), new MenuId("menu2", "TX2"))));
	}

	@Test
	public void insertMenuTest() {
		// �s�W�ŵ��(�|�s�W�A�n�`�N)
		// Assert.isTrue(menuDao.insertMenu("", "", 0, 0) == 1, RtnCode.TEST1_ERROR.getMessage());
		// �s�W���w�s�b
		Assert.isTrue(menuDao.insertMenu("menu1", "TX1", 200, 3) == 0, RtnCode.TEST2_ERROR.getMessage());
		// �s�W��榨�\
		Assert.isTrue(menuDao.insertMenu("menuX", "TX1", 200, 3) == 1, RtnCode.TEST2_ERROR.getMessage());
		menuDao.deleteById(new MenuId("menuX", "TX1"));
	}

	@Test
	public void updateMenuPriceByMenuIdTest() {
		// ������
		Assert.isTrue(menuDao.updateMenuPriceByMenuId("menu1", "TX1", 1000) == 1, RtnCode.TEST1_ERROR.getMessage());
		// �ܧ󩱮a���s�b
		Assert.isTrue(menuDao.updateMenuPriceByMenuId("menuX", "TX1", 1000) == 0, RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	public void updateMenuPointByMenuIdTest() {
		// ������
		Assert.isTrue(menuDao.updateMenuPointByMenuId("menu1", "TX1", 1) == 1, RtnCode.TEST1_ERROR.getMessage());
		// �ܧ󩱮a���s�b
		Assert.isTrue(menuDao.updateMenuPointByMenuId("menuX", "TX1", 5) == 0, RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	public void deleteMenuTest() {
		// �R������
		Assert.isTrue(menuDao.deleteMenu("menuX", "TX1") == 0, RtnCode.TEST1_ERROR.getMessage());
		// �R�����\
		menuDao.save(new Menu("menuX", "TX1", 100, 1));
		Assert.isTrue(menuDao.deleteMenu("menuX", "TX1") == 1, RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	public void getAverageMenuPointTest() {
		// �S�����
		Assert.isTrue(menuDao.getAverageMenuPoint("TXX") == null, RtnCode.TEST1_ERROR.getMessage());
		// ���o��������
		Assert.isTrue(menuDao.getAverageMenuPoint("TX1") == 3.3, RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	public void insertStoreTest() {
		// �s�W�ũ��a(�|�s�W�A�n�`�N) // null����
		// Assert.isTrue(storeDao.insertStore("", "", 0) == 1, RtnCode.TEST1_ERROR.getMessage());
		// �s�W���a�w�s�b
		Assert.isTrue(storeDao.insertStore("TX1", "city1", 0) == 0, RtnCode.TEST2_ERROR.getMessage());
		// �s�W���a���\
		Assert.isTrue(storeDao.insertStore("TXX", "city1", 0) == 1, RtnCode.TEST3_ERROR.getMessage());
		storeDao.deleteById("TXX");
	}

	@Test
	public void updateStoreCityByIdTest() {
		// null����
		Assert.isTrue(storeDao.updateStoreCityById(null, null) == 0, RtnCode.TEST1_ERROR.getMessage());
		// �ܧ󫰥�����
		Assert.isTrue(storeDao.updateStoreCityById("TX1", "city3") == 1, RtnCode.TEST2_ERROR.getMessage());
		// �ܧ󩱮a���s�b
		Assert.isTrue(storeDao.updateStoreCityById("TXX", "city3") == 0, RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	public void updateStorePointByIdTest() {
		// �ܧ���ƴ���
		Assert.isTrue(storeDao.updatePointById("TX1", 5.0) == 1, RtnCode.TEST1_ERROR.getMessage());
		// �ܧ󩱮a���s�b
		Assert.isTrue(storeDao.updatePointById("TXX", 4.7) == 0, RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	public void deleteStoreTest() {
		// �R��null
		Assert.isTrue(storeDao.deleteStore(null) == 0, RtnCode.TEST1_ERROR.getMessage());
		// �R������
		Assert.isTrue(storeDao.deleteStore("TXX") == 0, RtnCode.TEST2_ERROR.getMessage());
		// �R�����\
		storeDao.save(new Store("TXX", "city1", 1));
		Assert.isTrue(storeDao.deleteStore("TXX") == 1, RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	public void findStoreAndMenuByCityTest() {
		// �d��null
		Assert.isTrue(storeDao.findStoreAndMenuByCity(null).size() == 0, RtnCode.TEST1_ERROR.getMessage());
		// �d��""
		Assert.isTrue(storeDao.findStoreAndMenuByCity("").size() == 0, RtnCode.TEST2_ERROR.getMessage());
		// �d�߫������s�b
		Assert.isTrue(storeDao.findStoreAndMenuByCity("cityX").size() == 0, RtnCode.TEST3_ERROR.getMessage());
		// �d�߫��������a���ƶq
		Assert.isTrue(storeDao.findStoreAndMenuByCity("city1").size() == 5, RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	public void findStoreAndMenuByPointGreaterEqualThanTest() {
		// �d�ߵ����H�W���a
		Assert.isTrue(storeDao.findByStorePointGreaterEqualThan(4.1).size() == 2, RtnCode.TEST1_ERROR.getMessage());
		// �d�ߵ����H�W���s�b
		Assert.isTrue(storeDao.findByStorePointGreaterEqualThan(5).size() == 0, RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	public void findByStoreAndMenuPointGreaterEqualThanTest() {
		// �d�ߵ����H�W���\�I�����H�W���a
		Assert.isTrue(storeDao.findByStoreAndMenuPointGreaterEqualThan(3, 4).size() == 3,
				RtnCode.TEST1_ERROR.getMessage());
	}

	@Test
	public void addMenuTest() {
		// ���p:��J�ť�
		Assert.isTrue(fMSI.addMenu(null, "TX1", 200, 3).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// ���p:����t��
		Assert.isTrue(fMSI.addMenu("menu4", "TX1", -200, 3).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// �p��:�����W�L�d��(1~5)
		Assert.isTrue(fMSI.addMenu("menu4", "TX1", 200, 8).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		// �p��:��歫��
		Assert.isTrue(fMSI.addMenu("menu1", "TX1", 200, 3).getMessage().equals(RtnCode.ALREADY_EXISTED.getMessage()),
				RtnCode.TEST4_ERROR.getMessage());
		// �p��:���\
		Assert.isTrue(fMSI.addMenu("menu4", "TX1", 200, 3).getMessage().equals(RtnCode.SUCCESSFUL.getMessage()),
				RtnCode.TEST5_ERROR.getMessage());
		menuDao.deleteById(new MenuId("menu4", "TX1"));
	}

	@Test
	public void reviseMenuTest() {
		// ���p:��J�ť�
		Assert.isTrue(fMSI.reviseMenu(new Menu()).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// ���p:���󤣦s�b
		Assert.isTrue(
				fMSI.reviseMenu(new Menu("menuX", "TX1", 0, 0)).getMessage().equals(RtnCode.INCORRECT.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// �p��:���ק���󪫥�
		Assert.isTrue(
				fMSI.reviseMenu(new Menu("menu1", "TX1", 0, 0)).getMessage().equals(RtnCode.INCORRECT.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		// �p��:���\
		Assert.isTrue(
				fMSI.reviseMenu(new Menu("menu1", "TX1", 250, 4)).getMessage().equals(RtnCode.SUCCESSFUL.getMessage()),
				RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	public void deleteMenuImplTest() {
		// ���p:��J�ť�
		Assert.isTrue(fMSI.deleteMenu(new MenuId()).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// ���p:���󤣦s�b
		Assert.isTrue(fMSI.deleteMenu(new MenuId("menu1", "TXX")).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// �p��:���\
		Assert.isTrue(fMSI.deleteMenu(new MenuId("menu1", "TX1")).getMessage().equals(RtnCode.SUCCESSFUL.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		menuDao.save(new Menu("menu1", "TX1", 200, 3));
	}

	@Test
	public void addStoreTest() {
		// ���p:��J�ť�
		Assert.isTrue(fMSI.addStore(null, "city1").getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// ���p:��J����
		Assert.isTrue(fMSI.addStore("TX1", "city1").getMessage().equals(RtnCode.ALREADY_EXISTED.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// ���p:���\�s�W
		Assert.isTrue(fMSI.addStore("TX10", "city1").getMessage().equals(RtnCode.SUCCESSFUL.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		storeDao.delete(new Store("TX10", "city1"));
	}

	@Test
	public void reviseStoreTest() {
		// ���p:��J�ť�
		Assert.isTrue(fMSI.reviseStore(null, "city1").getMessage().equals(RtnCode.INCORRECT.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// ���p:���s�b����
		Assert.isTrue(fMSI.reviseStore("TXX", "city1").getMessage().equals(RtnCode.INCORRECT.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// ���p:���\�ק�
		Assert.isTrue(fMSI.reviseStore("TX1", "city5").getMessage().equals(RtnCode.SUCCESSFUL.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	public void deleteStoreImplTest() {
		// ���p:��J�ť�
		storeDao.save(new Store("TXX", "cityX"));
		Assert.isTrue(fMSI.deleteStore(null).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// ���p:���s�b����
		Assert.isTrue(fMSI.deleteStore("TX99").getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// ���p:���\�R��
		menuDao.save(new Menu("menuX", "TXX", 100, 4));
		Assert.isTrue(fMSI.deleteStore("TXX").getMessage().equals(RtnCode.SUCCESSFUL.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	public void findStoreByCityLimitTest() {
		// ���p:��J�ť�
		Assert.isTrue(fMSI.findStoreByCityLimit(null, 3).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// ���p:����j�����ƿ��~
		Assert.isTrue(fMSI.findStoreByCityLimit("city1", -3).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// ���p:���s�b����
		Assert.isTrue(fMSI.findStoreByCityLimit("cityX", 3).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		// ���p:���\
		Assert.isTrue(fMSI.findStoreByCityLimit("city1", 3).getMessage().equals(RtnCode.SUCCESS.getMessage()),
				RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	public void findStoreByPointTest() {
		// ���p:��J�d����~
		Assert.isTrue(fMSI.findStoreByPoint(5.1).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// ���p:���s�b�ŦX���󪫥�
		Assert.isTrue(fMSI.findStoreByPoint(4.9).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// ���p:���\
		Assert.isTrue(fMSI.findStoreByPoint(3.5).getMessage().equals(RtnCode.SUCCESS.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	public void findStoreAndMenuByPoint() {
		// ���p:��J�d����~
		Assert.isTrue(fMSI.findStoreAndMenuByPoint(5.1, 3).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// ���p:���s�b�ŦX���󪫥�
		Assert.isTrue(fMSI.findStoreAndMenuByPoint(4.9, 3).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// ���p:���\
		Assert.isTrue(fMSI.findStoreAndMenuByPoint(3.5, 4).getMessage().equals(RtnCode.SUCCESS.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
	}

}
