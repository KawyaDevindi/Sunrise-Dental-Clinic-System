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
        
        System.out.println("🔐 LogoutServlet - Processing logout request...");
        
        // Get username before invalidating session for logging
        String username = SessionManager.getUsername(request);
        System.out.println("👤 User logging out: " + username);
        
        // Invalidate session
        SessionManager.invalidateSession(request);
        System.out.println("✅ Session invalidated for: " + username);
        
        // Redirect to login page with success message
        response.sendRedirect(request.getContextPath() + "/login?logout=success");
        System.out.println("🔀 Redirecting to login page...");
    }
}