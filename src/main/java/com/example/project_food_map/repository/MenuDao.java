package com.example.project_food_map.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.project_food_map.entity.Menu;
import com.example.project_food_map.entity.MenuId;

@Repository
public interface MenuDao extends JpaRepository<Menu, MenuId> {

	// ��M���a���s�b�P�_
	public List<Menu> findByStoreName(String storeName);

	// ��s��T-������
	@Transactional
	@Modifying
	@Query("update Menu m set m.price = :price where m.menu = :menu and m.storeName = :storeName")
	public int updateMenuPriceByMenuId(@Param("menu") String menu, @Param("storeName") String storeName,
			@Param("price") int price);

	// ��s��T-������
	@Transactional
	@Modifying
	@Query("update Menu m set m.menuPoint = :menuPoint where m.menu = :menu and m.storeName = :storeName")
	public int updateMenuPointByMenuId(@Param("menu") String menu, @Param("storeName") String storeName,
			@Param("menuPoint") int menuPoint);

	// ���o��������
	@Query("select round(avg(m.menuPoint), 1) from Menu m where m.storeName = :storeName")
	public double getAverageMenuPoint(@Param("storeName") String storeName);

}
