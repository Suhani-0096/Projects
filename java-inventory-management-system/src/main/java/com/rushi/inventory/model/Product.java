package com.rushi.inventory.model;

public abstract class Product {
	private int id;
	private String name, category, supplier;
	private double price;
	private int quantity;

	protected Product() {
	}

	protected Product(String n, String c, double p, int q, String s) {
		name = n;
		category = c;
		price = p;
		quantity = q;
		supplier = s;
	}

	public abstract String getProductType();

	public abstract String getExtraInfo();

	public int getId() {
		return id;
	}

	public void setId(int v) {
		id = v;
	}

	public String getName() {
		return name;
	}

	public void setName(String v) {
		name = v;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String v) {
		category = v;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double v) {
		price = v;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int v) {
		quantity = v;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String v) {
		supplier = v;
	}

	public double getStockValue() {
		return price * quantity;
	}

	public String toString() {
		return String.format("ID=%d | %s | %s | Price=%.2f | Qty=%d | Supplier=%s | Type=%s | %s", id, name, category,
				price, quantity, supplier, getProductType(), getExtraInfo());
	}
}
