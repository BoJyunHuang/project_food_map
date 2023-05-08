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

	// 找尋店家菜單存在與否
	public List<Menu> findByStoreName(String storeName);

	// 新增菜單
	@Transactional
	@Modifying
	@Query(value = "insert into menu (menu, store_name, price, menu_point) select :menu, :storeName, :price, :menuPoint "
			+ "where not exists (select 1 from menu where store_name = :storeName and menu = :menu)", nativeQuery = true)
	public int insertMenu(@Param("menu") String menu, @Param("storeName") String storeName, @Param("price") int price,
			@Param("menuPoint") int menuPoint);

	// 更新資訊-更改價錢
	@Transactional
	@Modifying
	@Query("update Menu m set m.price = :price where m.menu = :menu and m.storeName = :storeName")
	public int updateMenuPriceByMenuId(@Param("menu") String menu, @Param("storeName") String storeName,
			@Param("price") int price);

	// 更新資訊-更改評分
	@Transactional
	@Modifying
	@Query("update Menu m set m.menuPoint = :menuPoint where m.menu = :menu and m.storeName = :storeName")
	public int updateMenuPointByMenuId(@Param("menu") String menu, @Param("storeName") String storeName,
			@Param("menuPoint") int menuPoint);

	// 刪除菜單
	@Transactional
	@Modifying
	@Query("delete from Menu m where m.menu = :menu and m.storeName = :storeName")
	public int deleteMenu(@Param("menu") String menu, @Param("storeName") String storeName);

	// 取得平均分數
	@Query("select round(avg(m.menuPoint), 1) from Menu m where m.storeName = :storeName")
	public Double getAverageMenuPoint(@Param("storeName") String storeName);

}
