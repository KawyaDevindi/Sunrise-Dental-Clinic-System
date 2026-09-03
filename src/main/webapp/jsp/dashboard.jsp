<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%
    if (session.getAttribute("username") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    String role = (String) session.getAttribute("role");
    if ("admin".equals(role)) {
        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .dashboard-wrapper {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        .dashboard-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            background: white;
            padding: 20px 30px;
            border-radius: 12px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }
        .dashboard-header h1 {
            color: #2c3e50;
            font-size: 24px;
        }
        .dashboard-header .user-info {
            display: flex;
            align-items: center;
            gap: 15px;
        }
        .dashboard-header .user-info span {
            color: #34495e;
            font-weight: 600;
        }
        .dashboard-header .user-info .staff-badge {
            background: #3498db;
            color: white;
            padding: 3px 12px;
            border-radius: 20px;
            font-size: 11px;
            font-weight: bold;
        }
        .dashboard-header .btn-logout {
            background: #e74c3c;
            color: white;
            padding: 8px 20px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            transition: background 0.3s;
            cursor: pointer;
            border: none;
            font-size: 14px;
        }
        .dashboard-header .btn-logout:hover {
            background: #c0392b;
        }
        .menu-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 25px;
            margin-top: 10px;
        }
        .menu-card {
            background: white;
            padding: 35px 20px;
            border-radius: 12px;
            text-align: center;
            text-decoration: none;
            color: #2c3e50;
            box-shadow: 0 2px 10px rgba(0,0,0,0.08);
            transition: all 0.3s ease;
            border: 2px solid transparent;
        }
        .menu-card:hover {
            transform: translateY(-8px);
            box-shadow: 0 12px 35px rgba(0,0,0,0.15);
            border-color: #3498db;
        }
        .menu-card .icon {
            font-size: 48px;
            display: block;
            margin-bottom: 12px;
        }
        .menu-card .label {
            font-size: 16px;
            font-weight: 700;
        }
        .menu-card .description {
            font-size: 12px;
            color: #7f8c8d;
            margin-top: 5px;
        }
        .welcome-section {
            background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
            color: white;
            padding: 30px;
            border-radius: 12px;
            margin-bottom: 30px;
        }
        .welcome-section h2 {
            font-size: 28px;
            margin-bottom: 5px;
        }
        .welcome-section p {
            opacity: 0.9;
            font-size: 14px;
        }
        @media (max-width: 600px) {
            .dashboard-header {
                flex-direction: column;
                gap: 15px;
                text-align: center;
            }
            .dashboard-header .user-info {
                flex-wrap: wrap;
                justify-content: center;
            }
        }
    </style>
    <script>
        function confirmLogout() {
            if (confirm('Are you sure you want to logout?')) {
                window.location.href = '${pageContext.request.contextPath}/logout';
            }
            return false;
        }
    </script>
</head>
<body>
    <div class="dashboard-wrapper">
        <!-- Header -->
        <div class="dashboard-header">
            <h1>🦷 Sunrise Dental Clinic</h1>
            <div class="user-info">
                <span>👋 Welcome, <strong><%= session.getAttribute("fullName") %></strong></span>
                <span class="staff-badge">👤 Staff</span>
                <button onclick="confirmLogout()" class="btn-logout">🚪 Logout</button>
            </div>
        </div>
        
        <!-- Welcome -->
        <div class="welcome-section">
            <h2>Welcome to the Dental Management System</h2>
            <p>Efficiently manage appointments, patients, and billing all in one place.</p>
        </div>
        
        <!-- Menu Grid -->
        <div class="menu-grid">
            <a href="${pageContext.request.contextPath}/appointment" class="menu-card">
                <span class="icon">📝</span>
                <span class="label">Register Appointment</span>
                <div class="description">Schedule new patient appointment</div>
            </a>
            <a href="${pageContext.request.contextPath}/appointment?action=search" class="menu-card">
                <span class="icon">🔍</span>
                <span class="label">View Appointment</span>
                <div class="description">Search and view appointment details</div>
            </a>
            <a href="${pageContext.request.contextPath}/bill" class="menu-card">
                <span class="icon">💰</span>
                <span class="label">Generate Bill</span>
                <div class="description">Create and print patient bills</div>
            </a>
            <a href="${pageContext.request.contextPath}/report" class="menu-card">
                <span class="icon">📊</span>
                <span class="label">Reports</span>
                <div class="description">View clinic reports and analytics</div>
            </a>
            <a href="${pageContext.request.contextPath}/jsp/help.jsp" class="menu-card">
                <span class="icon">❓</span>
                <span class="label">Help</span>
                <div class="description">System user guide and support</div>
            </a>
        </div>
    </div>
</body>
</html>