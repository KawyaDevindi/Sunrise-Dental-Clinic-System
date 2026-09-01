package com.dental.service;

import com.dental.dao.UserDAO;
import com.dental.model.User;

public class AuthService {
    private UserDAO userDAO;
    
    public AuthService() {
        this.userDAO = new UserDAO();
    }
    
    public boolean authenticate(String username, String password) {
        User user = userDAO.validateUser(username, password);
        return user != null;
    }
    
    public User getUserDetails(String username) {
        return userDAO.getUserByUsername(username);
    }
    
    public String getFullName(String username) {
        User user = userDAO.getUserByUsername(username);
        return user != null ? user.getFullName() : null;
    }
}
