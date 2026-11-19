package com.rushi.inventory.service;

import com.rushi.inventory.dao.ProductDAO;
import com.rushi.inventory.model.Product;
import com.rushi.inventory.util.InputValidator;
import java.sql.SQLException;
import java.util.*;

public class InventoryService {
	private final ProductDAO dao = new ProductDAO();
	private final List<Product> cache = new ArrayList<>();

	public Product add(Product p) throws SQLException {
		validate(p);
		Product x = dao.add(p);
		cache.add(x);
		return x;
	}

	public List<Product> all() throws SQLException {
		cache.clear();
		cache.addAll(dao.all());
		return new ArrayList<>(cache);
	}

	public Product byId(int id) throws SQLException {
		if (id <= 0)
			throw new IllegalArgumentException("Invalid ID.");
		return dao.byId(id);
	}

	public List<Product> search(String k) throws SQLException {
		return dao.search(InputValidator.required(k, "Keyword"));
	}

	public boolean update(Product p) throws SQLException {
		if (p.getId() <= 0)
			throw new IllegalArgumentException("Invalid ID.");
		validate(p);
		boolean ok = dao.update(p);
		if (ok)
			all();
		return ok;
	}

	public boolean delete(int id) throws SQLException {
		if (id <= 0)
			throw new IllegalArgumentException("Invalid ID.");
		boolean ok = dao.delete(id);
		if (ok)
			all();
		return ok;
	}

	public int units() throws SQLException {
		return all().stream().mapToInt(Product::getQuantity).sum();
	}

	public double value() throws SQLException {
		return all().stream().mapToDouble(Product::getStockValue).sum();
	}

	private void validate(Product p) {
		InputValidator.required(p.getName(), "Name");
		InputValidator.required(p.getCategory(), "Category");
		InputValidator.required(p.getSupplier(), "Supplier");
		InputValidator.price(p.getPrice());
		InputValidator.quantity(p.getQuantity());
	}
}
