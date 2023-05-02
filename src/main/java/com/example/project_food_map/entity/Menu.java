package com.example.project_food_map.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "menu")
@IdClass(value = MenuId.class)
public class Menu {
	
	@Id
	@Column(name = "menu")
	private String menu;
	
	@Id
	@Column(name = "store_name")
	private String storeName;
	
	@Column(name = "price")
	private int price;
	
	@Column(name = "menu_point")
	private int menuPoint;

	public Menu() {
		super();
	}

	public Menu(String menu, String storeName, int price, int menuPoint) {
		super();
		this.menu = menu;
		this.storeName = storeName;
		this.price = price;
		this.menuPoint = menuPoint;
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

}
