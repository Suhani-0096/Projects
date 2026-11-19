package com.rushi.inventory.dao;

import com.rushi.inventory.model.*;
import com.rushi.inventory.util.Connectivity;
import java.sql.*;
import java.util.*;

public class ProductDAO {
	public Product add(Product p) throws SQLException {
		String sql = "INSERT INTO products(name,category,price,quantity,supplier,product_type,extra_info) VALUES(?,?,?,?,?,?,?)";
		try (Connection c = Connectivity.con();
				PreparedStatement s = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			set(s, p);
			s.setString(6, p.getProductType());
			s.setString(7, p.getExtraInfo());
			s.executeUpdate();
			try (ResultSet r = s.getGeneratedKeys()) {
				if (r.next())
					p.setId(r.getInt(1));
			}
		}
		return p;
	}

	public List<Product> all() throws SQLException {
		return query("SELECT * FROM products ORDER BY id DESC", null);
	}

	public Product byId(int id) throws SQLException {
		List<Product> x = query("SELECT * FROM products WHERE id=?", id);
		return x.isEmpty() ? null : x.get(0);
	}

	public List<Product> search(String k) throws SQLException {
		return query(
				"SELECT * FROM products WHERE LOWER(name) LIKE ? OR LOWER(category) LIKE ? OR LOWER(supplier) LIKE ?",
				"%" + k.toLowerCase() + "%", "%" + k.toLowerCase() + "%", "%" + k.toLowerCase() + "%");
	}

	public boolean update(Product p) throws SQLException {
		String q = "UPDATE products SET name=?,category=?,price=?,quantity=?,supplier=?,product_type=?,extra_info=? WHERE id=?";
		try (Connection c = Connectivity.con(); PreparedStatement s = c.prepareStatement(q)) {
			set(s, p);
			s.setString(6, p.getProductType());
			s.setString(7, p.getExtraInfo());
			s.setInt(8, p.getId());
			return s.executeUpdate() > 0;
		}
	}

	public boolean delete(int id) throws SQLException {
		try (Connection c = Connectivity.con();
				PreparedStatement s = c.prepareStatement("DELETE FROM products WHERE id=?")) {
			s.setInt(1, id);
			return s.executeUpdate() > 0;
		}
	}

	private void set(PreparedStatement s, Product p) throws SQLException {
		s.setString(1, p.getName());
		s.setString(2, p.getCategory());
		s.setDouble(3, p.getPrice());
		s.setInt(4, p.getQuantity());
		s.setString(5, p.getSupplier());
	}

	private List<Product> query(String q, Object... a) throws SQLException {
		List<Product> out = new ArrayList<>();
		try (Connection c = Connectivity.con(); PreparedStatement s = c.prepareStatement(q)) {
			for (int i = 0; i < a.length; i++)
				s.setObject(i + 1, a[i]);
			try (ResultSet r = s.executeQuery()) {
				while (r.next())
					out.add(map(r));
			}
		}
		return out;
	}

	private Product map(ResultSet r) throws SQLException {
		String type = r.getString("product_type"), extra = r.getString("extra_info");
		Product p = "GROCERY".equalsIgnoreCase(type)
				? new GroceryProduct(r.getString("name"), r.getString("category"), r.getDouble("price"),
						r.getInt("quantity"), r.getString("supplier"), extra)
				: new ElectronicProduct(r.getString("name"), r.getString("category"), r.getDouble("price"),
						r.getInt("quantity"), r.getString("supplier"), extra);
		p.setId(r.getInt("id"));
		return p;
	}
}
