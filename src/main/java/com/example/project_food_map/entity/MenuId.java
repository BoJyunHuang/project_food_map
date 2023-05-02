package com.example.project_food_map.entity;

import java.io.Serializable;

public class MenuId implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String menu;
	
	private String storeName;

	public MenuId() {
		super();
	}

	public MenuId(String menu, String storeName) {
		super();
		this.menu = menu;
		this.storeName = storeName;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
