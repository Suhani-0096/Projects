package com.rushi.inventory.model;

public class GroceryProduct extends Product {
	private String expiryDate;

	public GroceryProduct(String n, String c, double p, int q, String s, String e) {
		super(n, c, p, q, s);
		expiryDate = e;
	}

	public String getProductType() {
		return "GROCERY";
	}

	public String getExtraInfo() {
		return "Expiry: " + expiryDate;
	}

	public String getExpiryDate() {
		return expiryDate;
	}
}
