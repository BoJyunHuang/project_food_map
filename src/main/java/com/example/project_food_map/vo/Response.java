package com.example.project_food_map.vo;

import java.util.List;

import com.example.project_food_map.entity.Menu;
import com.example.project_food_map.entity.Store;

public class Response {

	private String message;
	private Store store;
	private Menu menu;
	private List<Menu> menuList;

	public Response() {
		super();
	}

	public Response(String message) {
		super();
		this.message = message;
	}

	public Response(Store store, String message) {
		super();
		this.store = store;
		this.message = message;
	}

	public Response(Menu menu, String message) {
		super();
		this.menu = menu;
		this.message = message;
	}

	public Response(List<Menu> menuList, String message) {
		super();
		this.menuList = menuList;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

}
