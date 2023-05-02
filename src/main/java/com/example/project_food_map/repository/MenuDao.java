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
	
	// 更新資訊-更改價錢
	@Transactional
	@Modifying
	@Query("update Menu m set m.price = :price where m.menuId = :menuId")
	public int updateMenuPriceByMenuId(@Param("menuId") MenuId menuId, @Param("price") int price);

	// 更新資訊-更改評分
	@Transactional
	@Modifying
	@Query("update Menu m set m.menuPoint = :menuPoint where m.menuId = :menuId")
	public int updateMenuPointByMenuId(@Param("menuId") MenuId menuId, @Param("menuPoint") int menuPoint);

	// 更新資訊-取得平均分數
	@Transactional
	@Modifying
	@Query("select AVG(m.menuPoint) from Menu m where m.menuId = :menuId")
	public int getAverageMenuPoint(@Param("menuId") MenuId menuId);
	
}
