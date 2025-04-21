package com.myapp.webappfirewall.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DBLogger {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/firewall_db";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";

    public static void logRequest(String method, String uri, String query, String ip, boolean blocked) {
        String sql = "INSERT INTO request_logs (method, uri, query, ip_address, blocked) VALUES (?, ?, ?, ?, ?)";

        try {
            // ✅ Load the MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, method);
                stmt.setString(2, uri);
                stmt.setString(3, query);
                stmt.setString(4, ip);
                stmt.setBoolean(5, blocked);
                stmt.executeUpdate();

            }
        } catch (Exception e) {
            System.err.println("❌ Failed to log request: " + e.getMessage());
        }
    }
}
