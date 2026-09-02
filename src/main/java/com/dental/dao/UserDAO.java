package com.dental.dao;

import com.dental.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    
    public User validateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            
            if (conn == null) {
                System.err.println("❌ Database connection is NULL! Cannot validate user.");
                return null;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    user.setActive(rs.getBoolean("is_active"));
                    user.setCreatedAt(rs.getString("created_at"));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ SQL Error in validateUser: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            
            if (conn == null) {
                System.err.println("❌ Database connection is NULL! Cannot get user.");
                return null;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    user.setActive(rs.getBoolean("is_active"));
                    user.setCreatedAt(rs.getString("created_at"));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ SQL Error in getUserByUsername: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            
            if (conn == null) {
                System.err.println("❌ Database connection is NULL! Cannot get user.");
                return null;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    user.setActive(rs.getBoolean("is_active"));
                    user.setCreatedAt(rs.getString("created_at"));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ SQL Error in getUserById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            
            if (conn == null) {
                System.err.println("❌ Database connection is NULL! Cannot get users.");
                return users;
            }
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    user.setActive(rs.getBoolean("is_active"));
                    user.setCreatedAt(rs.getString("created_at"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ SQL Error in getAllUsers: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }
    
    public boolean addUser(User user) {
        String sql = "INSERT INTO users (username, password, full_name, email, role, is_active) VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            
            if (conn == null) {
                System.err.println("❌ Database connection is NULL! Cannot add user.");
                return false;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getFullName());
                pstmt.setString(4, user.getEmail());
                pstmt.setString(5, user.getRole());
                pstmt.setBoolean(6, user.isActive());
                
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ SQL Error in addUser: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET full_name = ?, email = ?, role = ?, is_active = ? WHERE user_id = ?";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            
            if (conn == null) {
                System.err.println("❌ Database connection is NULL! Cannot update user.");
                return false;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, user.getFullName());
                pstmt.setString(2, user.getEmail());
                pstmt.setString(3, user.getRole());
                pstmt.setBoolean(4, user.isActive());
                pstmt.setInt(5, user.getUserId());
                
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ SQL Error in updateUser: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ? AND role != 'admin'";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            
            if (conn == null) {
                System.err.println("❌ Database connection is NULL! Cannot delete user.");
                return false;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ SQL Error in deleteUser: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deactivateUser(int userId) {
        String sql = "UPDATE users SET is_active = FALSE WHERE user_id = ? AND role != 'admin'";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            
            if (conn == null) {
                System.err.println("❌ Database connection is NULL! Cannot deactivate user.");
                return false;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ SQL Error in deactivateUser: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean activateUser(int userId) {
        String sql = "UPDATE users SET is_active = TRUE WHERE user_id = ?";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            
            if (conn == null) {
                System.err.println("❌ Database connection is NULL! Cannot activate user.");
                return false;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ SQL Error in activateUser: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}