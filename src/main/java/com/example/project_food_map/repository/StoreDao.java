package com.example.project_food_map.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.project_food_map.entity.Store;
import com.example.project_food_map.vo.StoreAndMenu;

@Repository
public interface StoreDao extends JpaRepository<Store, String> {

	// ��s��T-��﫰��
	@Transactional
	@Modifying
	@Query("update Store s set s.city = :city where s.name = :name")
	public int updateStoreCityById(@Param("name") String name, @Param("city") String city);

	// ��s��T-������
	@Transactional
	@Modifying
	@Query("update Store s set s.point = :point where s.name = :name")
	public int updatePointById(@Param("name") String name, @Param("point") double point);

	// �̷ӫ�����쩱�a���\�I�A�åB�n�����
	@Transactional
	@Modifying
	@Query(value = "select new com.example.project_food_map.vo.StoreAndMenu(s.name, s.city, s.point, m.menu, m.price, m.menuPoint) "
			+ "from Store s join Menu m on s.name = m.storeName where s.city = :city")
	public List<StoreAndMenu> findStoreAndMenuByCityLimit(@Param("city") String city);

	// �̷ӵ�����쩱�a���\�I�A�åB�̩��a�����Ƨ�
	@Transactional
	@Modifying
	@Query("select new com.example.project_food_map.vo.StoreAndMenu(s.name, s.city, s.point, m.menu, m.price, m.menuPoint) "
			+ "from Store s join Menu m on s.name = m.storeName where s.point >= :point order by s.point desc")
	public List<StoreAndMenu> findStoreAndMenuByPointGreaterEqualThan(@Param("point") double point);

	// �̷ӵ�����쩱�a���\�I�A�åB�̩��a�����Ƨ�
	@Transactional
	@Modifying
	@Query("select new com.example.project_food_map.vo.StoreAndMenu(s.name, s.city, s.point, m.menu, m.price, m.menuPoint) "
			+ "from Store s join Menu m on s.name = m.storeName where s.point >= :point and m.menuPoint >= :menuPoint "
			+ "order by s.point desc, m.menuPoint desc")
	public List<StoreAndMenu> findStoreAndMenuByPointAndMenuPointGreaterEqualThan(@Param("point") double point,
			@Param("menuPoint") int menuPoint);
}
