package com.example.project_food_map.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "store")
public class Store {
	
	@Id
	@Column(name = "name")
	private String name;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "point")
	private double point = 0;

	public Store() {
		super();
	}

	public Store(String name, String city) {
		super();
		this.name = name;
		this.city = city;
	}

	public Store(String name, String city, double point) {
		super();
		this.name = name;
		this.city = city;
		this.point = point;
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
	
}
