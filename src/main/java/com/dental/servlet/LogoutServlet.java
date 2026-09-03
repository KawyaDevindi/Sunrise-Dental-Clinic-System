package com.dental.servlet;

import com.dental.util.SessionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get username before invalidating session for logging
        String username = SessionManager.getUsername(request);
        
        // Invalidate session
        SessionManager.invalidateSession(request);
        
        // Log the logout
        System.out.println("🔐 User logged out: " + username);
        
        // Redirect to login page with success message
        response.sendRedirect(request.getContextPath() + "/login?logout=success");
    }
}