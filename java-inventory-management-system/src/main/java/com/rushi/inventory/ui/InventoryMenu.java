package com.rushi.inventory.ui;

import com.rushi.inventory.model.*;
import com.rushi.inventory.service.InventoryService;
import java.sql.SQLException;
import java.util.*;

public class InventoryMenu {
	private final InventoryService service = new InventoryService();
	private final Scanner sc = new Scanner(System.in);

	public void start() {
		while (true) {
			System.out.println("\n1.Add  2.View  3.Search  4.Update  5.Delete  6.Stock Summary  0.Exit");
			try {
				int c = num("Choice: ");
				switch (c) {
				case 1 -> add();
				case 2 -> view();
				case 3 -> search();
				case 4 -> update();
				case 5 -> delete();
				case 6 -> summary();
				case 0 -> {
					return;
				}
				default -> System.out.println("Invalid choice");
				}
			} catch (SQLException e) {
				System.out.println("Database error: " + e.getMessage());
			} catch (IllegalArgumentException e) {
				System.out.println("Validation: " + e.getMessage());
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
		}
	}

	private void add() throws SQLException {
		String t = type();
		Product p = base(t);
		service.add(p);
		System.out.println("Added with ID " + p.getId());
	}

	private Product base(String t) {
		String n = text("Name: "), c = text("Category: ");
		double p = decimal("Price: ");
		int q = num("Quantity: ");
		String s = text("Supplier: ");
		return "1".equals(t) ? new ElectronicProduct(n, c, p, q, s, text("Warranty: "))
				: new GroceryProduct(n, c, p, q, s, text("Expiry date: "));
	}

	private void view() throws SQLException {
		print(service.all());
	}

	private void search() throws SQLException {
		print(service.search(text("Search: ")));
	}

	private void update() throws SQLException {
		int id = num("ID: ");
		Product old = service.byId(id);
		if (old == null) {
			System.out.println("Not found");
			return;
		}
		Product p = base(type());
		p.setId(id);
		System.out.println(service.update(p) ? "Updated" : "Failed");
	}

	private void delete() throws SQLException {
		int id = num("ID: ");
		if (service.byId(id) == null) {
			System.out.println("Not found");
			return;
		}
		if (text("Type YES: ").equalsIgnoreCase("YES"))
			System.out.println(service.delete(id) ? "Deleted" : "Failed");
	}

	private void summary() throws SQLException {
		System.out.println("Units: " + service.units());
		System.out.printf("Stock Value: ₹%.2f%n", service.value());
	}

	private String type() {
		while (true) {
			String x = text("1=Electronic, 2=Grocery: ");
			if (x.equals("1") || x.equals("2"))
				return x;
			System.out.println("Enter 1 or 2");
		}
	}

	private void print(List<Product> p) {
		if (p.isEmpty())
			System.out.println("No products.");
		else
			p.forEach(System.out::println);
	}

	private String text(String m) {
		System.out.print(m);
		return sc.nextLine().trim();
	}

	private int num(String m) {
		while (true)
			try {
				return Integer.parseInt(text(m));
			} catch (NumberFormatException e) {
				System.out.println("Enter a valid integer.");
			}
	}

	private double decimal(String m) {
		while (true)
			try {
				return Double.parseDouble(text(m));
			} catch (NumberFormatException e) {
				System.out.println("Enter a valid number.");
			}
	}
}
