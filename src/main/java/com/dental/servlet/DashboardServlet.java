package com.dental.servlet;

import com.dental.util.SessionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("🔍 DashboardServlet - GET request");
        
        // Check if user is logged in
        if (!SessionManager.isLoggedIn(request)) {
            System.out.println("❌ DashboardServlet - Not logged in, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Check if user is admin - if so, redirect to admin dashboard
        String role = SessionManager.getRole(request);
        if ("admin".equals(role)) {
            System.out.println("👤 Admin user redirected to admin dashboard");
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }
        
        // Forward to dashboard JSP
        System.out.println("✅ DashboardServlet - Forwarding to dashboard.jsp");
        request.getRequestDispatcher("/jsp/dashboard.jsp").forward(request, response);
    }
}