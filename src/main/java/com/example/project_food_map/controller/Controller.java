package com.example.project_food_map.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.project_food_map.service.ifs.FoodMapService;
import com.example.project_food_map.vo.Request;
import com.example.project_food_map.vo.Response;

@RestController
public class Controller {

	@Autowired
	public FoodMapService foodMapService;

	@PostMapping(value = "add_store")
	public Response addStore(@RequestBody Request request) {
		return foodMapService.addStore(request.getName(), request.getCity());
	}

	@GetMapping(value = "revise_store")
	public Response reviseStore(@RequestBody Request request) {
		return foodMapService.reviseStore(request.getName(), request.getCity());
	}

	@DeleteMapping(value = "delete_store")
	public Response deleteStore(@RequestBody Request request) {
		return foodMapService.deleteStore(request.getName());
	}

	@PostMapping(value = "add_menu")
	public Response addMenu(@RequestBody Request request) {
		return foodMapService.addMenu(request.getMenu(), request.getStoreName(), request.getPrice(),
				request.getMenuPoint());
	}

	@GetMapping(value = "revise_menu")
	public Response reviseMenu(@RequestBody Request request) {
		return foodMapService.reviseMenu(request.getMenuEntity());
	}

	@DeleteMapping(value = "delete_menu")
	public Response deleteMenu(@RequestBody Request request) {
		return foodMapService.deleteMenu(request.getMenuId());
	}
	
	@GetMapping(value = "find_store_by_city_limit")
	public Response findStoreByCityLimit(@RequestBody Request request) {
		return foodMapService.findStoreByCityLimit(request.getCity(), request.getTimes());
	}
	
	@GetMapping(value = "find_store_by_point")
	public Response findStoreByPoint(@RequestBody Request request) {
		return foodMapService.findStoreByPoint(request.getPoint());
	}
	
	@GetMapping(value = "find_store_and_menu_by_point")
	public Response findStoreAndMenuByPoint(@RequestBody Request request) {
		return foodMapService.findStoreAndMenuByPoint(request.getPoint(), request.getMenuPoint());
	}

}
