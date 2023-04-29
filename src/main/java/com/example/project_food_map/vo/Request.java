package com.example.project_food_map.vo;

import java.util.List;

import com.example.project_food_map.entity.Menu;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {

	private String name;
	private String city;

	private String menu;
	@JsonProperty("store_name")
	private String storeName;
	private int price;
	@JsonProperty("menu_point")
	private int menuPoint;
	@JsonProperty("menu_list")
	private List<Menu> menuList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getMenuPoint() {
		return menuPoint;
	}

	public void setMenuPoint(int menuPoint) {
		this.menuPoint = menuPoint;
	}

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}
}
