package com.example.project_food_map.vo;

public class StoreAndMenu {

	private String name;
	private String city;
	private double point;
	private String menu;
	private int price;
	private int menuPoint;

	public StoreAndMenu() {
		super();
	}

	public StoreAndMenu(String name, String city, double point, String menu, int price, int menuPoint) {
		super();
		this.name = name;
		this.city = city;
		this.point = point;
		this.menu = menu;
		this.price = price;
		this.menuPoint = menuPoint;
	}

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

	public double getPoint() {
		return point;
	}

	public void setPoint(double point) {
		this.point = point;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
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
