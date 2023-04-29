package com.example.project_food_map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project_food_map.entity.Store;

@Repository
public interface StoreDao extends JpaRepository<Store, String>{

	//TODO
}
