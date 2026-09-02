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
        
        // Check if user is logged in and is admin
        if (!SessionManager.isLoggedIn(request) || !SessionManager.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Forward to admin dashboard JSP
        request.getRequestDispatcher("/jsp/admin/adminDashboard.jsp").forward(request, response);
    }
}
