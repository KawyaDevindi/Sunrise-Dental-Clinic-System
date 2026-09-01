package com.dental.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SessionManager {
    
    private static final String SESSION_USERNAME = "username";
    private static final String SESSION_FULLNAME = "fullName";
    private static final String SESSION_ROLE = "role";
    private static final int SESSION_TIMEOUT = 30 * 60; // 30 minutes
    
    public static void createSession(HttpServletRequest request, HttpServletResponse response, 
                                     String username, String fullName, String role) {
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_USERNAME, username);
        session.setAttribute(SESSION_FULLNAME, fullName);
        session.setAttribute(SESSION_ROLE, role);
        session.setMaxInactiveInterval(SESSION_TIMEOUT);
        
        // Create cookie for remember me
        Cookie userCookie = new Cookie("username", username);
        userCookie.setMaxAge(24 * 60 * 60); // 1 day
        userCookie.setPath("/");
        response.addCookie(userCookie);
    }
    
    public static boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(SESSION_USERNAME) != null;
    }
    
    public static String getUsername(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? (String) session.getAttribute(SESSION_USERNAME) : null;
    }
    
    public static String getFullName(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? (String) session.getAttribute(SESSION_FULLNAME) : null;
    }
    
    public static void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
    
    public static String getRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? (String) session.getAttribute(SESSION_ROLE) : null;
    }
    
    public static boolean isAdmin(HttpServletRequest request) {
        String role = getRole(request);
        return "admin".equals(role);
    }
}
