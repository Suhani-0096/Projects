package com.rushi.inventory.model;

public class ElectronicProduct extends Product {
	private String warranty;

	public ElectronicProduct(String n, String c, double p, int q, String s, String w) {
		super(n, c, p, q, s);
		warranty = w;
	}

	public String getProductType() {
		return "ELECTRONIC";
	}

	public String getExtraInfo() {
		return "Warranty: " + warranty;
	}

	public String getWarranty() {
		return warranty;
	}
}
