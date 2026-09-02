package com.dental.servlet.admin;

import com.dental.dao.UserDAO;
import com.dental.model.User;
import com.dental.util.SessionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users")
public class UserManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check admin access
        if (!SessionManager.isLoggedIn(request) || !SessionManager.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("delete".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("id"));
            boolean deleted = userDAO.deleteUser(userId);
            if (deleted) {
                request.setAttribute("success", "User deleted successfully!");
            } else {
                request.setAttribute("error", "Cannot delete admin user or user not found.");
            }
        } else if ("deactivate".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("id"));
            boolean deactivated = userDAO.deactivateUser(userId);
            if (deactivated) {
                request.setAttribute("success", "User deactivated successfully!");
            } else {
                request.setAttribute("error", "Cannot deactivate admin user.");
            }
        } else if ("activate".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("id"));
            boolean activated = userDAO.activateUser(userId);
            if (activated) {
                request.setAttribute("success", "User activated successfully!");
            } else {
                request.setAttribute("error", "Failed to activate user.");
            }
        }
        
        // Get all users
        List<User> users = userDAO.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/jsp/admin/manageUsers.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check admin access
        if (!SessionManager.isLoggedIn(request) || !SessionManager.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            addUser(request, response);
        } else if ("update".equals(action)) {
            updateUser(request, response);
        }
    }
    
    private void addUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        boolean isActive = "on".equals(request.getParameter("isActive"));
        
        // Validate
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            fullName == null || fullName.trim().isEmpty()) {
            request.setAttribute("error", "Please fill all required fields.");
            doGet(request, response);
            return;
        }
        
        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(password.trim());
        user.setFullName(fullName.trim());
        user.setEmail(email != null ? email.trim() : "");
        user.setRole(role != null ? role : "staff");
        user.setActive(isActive);
        
        boolean added = userDAO.addUser(user);
        if (added) {
            request.setAttribute("success", "User added successfully!");
        } else {
            request.setAttribute("error", "Failed to add user. Username may already exist.");
        }
        
        doGet(request, response);
    }
    
    private void updateUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int userId = Integer.parseInt(request.getParameter("userId"));
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        boolean isActive = "on".equals(request.getParameter("isActive"));
        
        User user = userDAO.getUserById(userId);
        if (user == null) {
            request.setAttribute("error", "User not found.");
            doGet(request, response);
            return;
        }
        
        user.setFullName(fullName.trim());
        user.setEmail(email != null ? email.trim() : "");
        user.setRole(role != null ? role : "staff");
        user.setActive(isActive);
        
        boolean updated = userDAO.updateUser(user);
        if (updated) {
            request.setAttribute("success", "User updated successfully!");
        } else {
            request.setAttribute("error", "Failed to update user.");
        }
        
        doGet(request, response);
    }
}