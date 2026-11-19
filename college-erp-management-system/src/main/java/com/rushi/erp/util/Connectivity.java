package com.rushi.erp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Connectivity {
    private static final String URL =
        "jdbc:mysql://localhost:3306/college_erp?useSSL=false&serverTimezone=Asia/Kolkata&allowPublicKeyRetrieval=true";
    private static final String USER="root";
    private static final String PASSWORD="Rushi@213";

    private Connectivity(){}

    public static Connection con() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL,USER,PASSWORD);
        } catch(ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.",e);
        }
    }
}
