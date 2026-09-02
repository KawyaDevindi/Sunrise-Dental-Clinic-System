package com.dental.filter;

import com.dental.util.SessionManager;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@WebFilter("/*")
public class AuthFilter implements Filter {
    
    // Public pages that don't require authentication
    private static final Set<String> PUBLIC_PAGES = new HashSet<>(Arrays.asList(
        "/login",
        "/css/",
        "/js/",
        "/images/"
    ));
    
    // Admin-only pages
    private static final Set<String> ADMIN_PAGES = new HashSet<>(Arrays.asList(
        "/admin/dashboard",
        "/admin/users",
        "/admin/users/add",
        "/admin/users/edit",
        "/admin/users/delete",
        "/admin/reports"
    ));
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("🔐 AuthFilter initialized");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        String path = req.getRequestURI().substring(req.getContextPath().length());
        
        // Allow public pages
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Check if user is logged in
        if (!SessionManager.isLoggedIn(req)) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        // Check if user is active
        if (!SessionManager.isUserActive(req)) {
            SessionManager.invalidateSession(req);
            res.sendRedirect(req.getContextPath() + "/login?error=Account+is+deactivated");
            return;
        }
        
        // Check role-based access
        String role = SessionManager.getRole(req);
        
        if (isAdminPath(path) && !"admin".equals(role)) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Admin only");
            return;
        }
        
        // Allow access
        chain.doFilter(request, response);
    }
    
    private boolean isPublicPath(String path) {
        for (String publicPath : PUBLIC_PAGES) {
            if (path.startsWith(publicPath)) {
                return true;
            }
        }
        return path.equals("/") || path.isEmpty();
    }
    
    private boolean isAdminPath(String path) {
        for (String adminPath : ADMIN_PAGES) {
            if (path.startsWith(adminPath)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void destroy() {
        System.out.println("🔐 AuthFilter destroyed");
    }
}
