package com.dental.servlet.admin;

import com.dental.util.SessionManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("🔍 AdminDashboardServlet - GET request");
        
        // Check if user is logged in
        if (!SessionManager.isLoggedIn(request)) {
            System.out.println("❌ AdminDashboardServlet - Not logged in, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Check if user is admin
        if (!SessionManager.isAdmin(request)) {
            System.out.println("❌ AdminDashboardServlet - Not admin, redirecting to staff dashboard");
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        
        // Forward to admin dashboard JSP
        System.out.println("✅ AdminDashboardServlet - Forwarding to adminDashboard.jsp");
        request.getRequestDispatcher("/jsp/admin/adminDashboard.jsp").forward(request, response);
    }
}