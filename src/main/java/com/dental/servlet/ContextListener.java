package com.dental.servlet;

import com.dental.dao.DatabaseConnection;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("==========================================");
        System.out.println("🦷 Sunrise Dental Clinic System Started");
        System.out.println("==========================================");
        System.out.println("Server: Apache Tomcat 10");
        System.out.println("Database URL: jdbc:mysql://localhost:3306/dental_clinic");
        System.out.println("Status: Initializing...");
        System.out.println("==========================================");
        
        // Test database connection on startup
        try {
            DatabaseConnection.getInstance().getConnection();
            System.out.println("✅ Database connection test: SUCCESS");
        } catch (Exception e) {
            System.err.println("❌ Database connection test: FAILED - " + e.getMessage());
        }
        System.out.println("==========================================");
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DatabaseConnection.getInstance().closeConnection();
        System.out.println("🦷 Sunrise Dental Clinic System Shutdown");
        System.out.println("==========================================");
    }
}