package com.dental.servlet;

import com.dental.model.User;
import com.dental.service.AuthService;
import com.dental.util.SessionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AuthService authService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        authService = new AuthService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // If already logged in, redirect based on role
        if (SessionManager.isLoggedIn(request)) {
            String role = SessionManager.getRole(request);
            if ("admin".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
            return;
        }
        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Validate input
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Please enter both username and password.");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            return;
        }
        
        // Authenticate user
        User user = authService.authenticateUser(username, password);
        
        if (user != null) {
            // Check if user is active
            if (!user.isActive()) {
                request.setAttribute("error", "Your account has been deactivated. Please contact admin.");
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
                return;
            }
            
            // Create session and cookie
            SessionManager.createSession(request, response, username, user.getFullName(), 
                                         user.getRole(), user.getUserId(), user.isActive());
            
            // Redirect based on role
            if ("admin".equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
        } else {
            request.setAttribute("error", "Invalid username or password. Please try again.");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }
}