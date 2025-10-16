package com.dictionary.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnectionManager {
    private static final DBConnectionManager INSTANCE = new DBConnectionManager();
    
    private static final String URL = "jdbc:mysql://localhost:3306/dic_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "root";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private DBConnectionManager() {
        try {
            Class.forName(DRIVER);
            // Auto-initialize database upon instance creation
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    public static DBConnectionManager getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /**
     * Initializes the database by creating the necessary tables if they do not exist.
     * Called automatically in the constructor during Singleton instantiation.
     * It creates 'Root' and 'Word' tables with appropriate schema for the dictionary system.
     */
    private void initializeDatabase() {
        String createRootTableSQL = """
            CREATE TABLE IF NOT EXISTS Root (
                ID INT AUTO_INCREMENT PRIMARY KEY,
                root_letters VARCHAR(4) UNIQUE NOT NULL
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
            """;

        String createWordTableSQL = """
            CREATE TABLE IF NOT EXISTS Word (
                ID INT AUTO_INCREMENT PRIMARY KEY,
                arabic_form VARCHAR(50) NOT NULL,
                base_form VARCHAR(50),
                urdu_meaning TEXT,
                part_of_speech VARCHAR(20),
                root_id INT,
                FOREIGN KEY (root_id) REFERENCES Root(ID) ON DELETE CASCADE,
                INDEX idx_word_root (root_id),
                INDEX idx_word_base (base_form)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
            """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(createRootTableSQL);
            stmt.executeUpdate(createWordTableSQL);
            
            System.out.println("Database tables initialized successfully (created if not exists).");
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            // Do not throw here to avoid preventing Singleton creation; log and continue
            // In production, consider a flag or shutdown hook
            e.printStackTrace();
        }
    }
}