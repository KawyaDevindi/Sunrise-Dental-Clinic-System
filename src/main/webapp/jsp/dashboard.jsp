<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%
    // Debug - print session attributes
    System.out.println("=== dashboard.jsp START ===");
    System.out.println("Session ID: " + session.getId());
    System.out.println("Username: " + session.getAttribute("username"));
    System.out.println("Role: " + session.getAttribute("role"));
    System.out.println("FullName: " + session.getAttribute("fullName"));
    
    // Check if user is logged in
    if (session.getAttribute("username") == null) {
        System.out.println("❌ No username in session, redirecting to login");
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    String role = (String) session.getAttribute("role");
    System.out.println("✅ User role: " + role);
    
    // If admin, redirect to admin dashboard
    if ("admin".equals(role)) {
        System.out.println("🔀 Redirecting admin to admin dashboard");
        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        return;
    }
    
    String fullName = (String) session.getAttribute("fullName");
    System.out.println("✅ Staff dashboard loaded for: " + fullName);
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
            border: none;
            font-weight: 600;
            transition: background 0.3s;
            cursor: pointer;
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

        /* Logout Modal Styles */
        .logout-modal-overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            backdrop-filter: blur(8px);
            -webkit-backdrop-filter: blur(8px);
            z-index: 9999;
            justify-content: center;
            align-items: center;
            animation: fadeIn 0.3s ease;
        }
        .logout-modal-overlay.active {
            display: flex !important;
        }
        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }
        @keyframes slideUp {
            from { opacity: 0; transform: translateY(30px) scale(0.95); }
            to { opacity: 1; transform: translateY(0) scale(1); }
        }
        .logout-modal {
            background: white;
            border-radius: 20px;
            padding: 0;
            max-width: 400px;
            width: 90%;
            box-shadow: 0 25px 60px rgba(0, 0, 0, 0.3);
            animation: slideUp 0.35s cubic-bezier(0.34, 1.56, 0.64, 1);
            overflow: hidden;
        }
        .logout-modal .modal-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 25px 30px 20px;
            text-align: center;
        }
        .logout-modal .modal-header .icon-wrapper {
            width: 70px;
            height: 70px;
            background: rgba(255, 255, 255, 0.2);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 12px;
            font-size: 32px;
            border: 2px solid rgba(255, 255, 255, 0.3);
        }
        .logout-modal .modal-header h3 {
            color: white;
            font-size: 20px;
            font-weight: 700;
            margin: 0;
        }
        .logout-modal .modal-header .subtitle {
            color: rgba(255, 255, 255, 0.8);
            font-size: 13px;
            margin-top: 4px;
        }
        .logout-modal .modal-body {
            padding: 25px 30px 10px;
        }
        .logout-modal .modal-body .user-info {
            display: flex;
            align-items: center;
            gap: 15px;
            padding: 12px 16px;
            background: #f8f9fa;
            border-radius: 12px;
            margin-bottom: 18px;
        }
        .logout-modal .modal-body .user-info .avatar {
            width: 44px;
            height: 44px;
            border-radius: 50%;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 18px;
            font-weight: 700;
            flex-shrink: 0;
        }
        .logout-modal .modal-body .user-info .user-details {
            flex: 1;
        }
        .logout-modal .modal-body .user-info .user-details .name {
            font-weight: 600;
            color: #2c3e50;
            font-size: 15px;
        }
        .logout-modal .modal-body .user-info .user-details .role {
            font-size: 12px;
            color: #7f8c8d;
        }
        .logout-modal .modal-body .user-info .user-details .role .badge {
            background: #3498db;
            color: white;
            padding: 2px 10px;
            border-radius: 20px;
            font-size: 10px;
            font-weight: 600;
            margin-left: 5px;
        }
        .logout-modal .modal-body .warning-text {
            display: flex;
            align-items: center;
            gap: 10px;
            padding: 12px 16px;
            background: #fff5f5;
            border-radius: 10px;
            border-left: 3px solid #e74c3c;
            margin-bottom: 5px;
        }
        .logout-modal .modal-body .warning-text .icon {
            font-size: 18px;
        }
        .logout-modal .modal-body .warning-text p {
            margin: 0;
            font-size: 13px;
            color: #555;
            line-height: 1.4;
        }
        .logout-modal .modal-body .warning-text p strong {
            color: #e74c3c;
        }
        .logout-modal .modal-footer {
            padding: 15px 30px 25px;
            display: flex;
            gap: 12px;
        }
        .logout-modal .modal-footer .btn-cancel {
            flex: 1;
            padding: 12px 20px;
            background: #f1f2f6;
            border: none;
            border-radius: 12px;
            font-weight: 600;
            font-size: 14px;
            color: #555;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        .logout-modal .modal-footer .btn-cancel:hover {
            background: #e5e7eb;
            transform: translateY(-1px);
        }
        .logout-modal .modal-footer .btn-logout {
            flex: 1;
            padding: 12px 20px;
            background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
            border: none;
            border-radius: 12px;
            font-weight: 600;
            font-size: 14px;
            color: white;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(231, 76, 60, 0.3);
        }
        .logout-modal .modal-footer .btn-logout:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 25px rgba(231, 76, 60, 0.4);
        }
        .logout-modal .modal-version {
            text-align: center;
            padding: 0 30px 18px;
            font-size: 11px;
            color: #bbb;
            letter-spacing: 0.5px;
        }
    </style>
</head>
<body>
    <div class="dashboard-wrapper">
        <div class="dashboard-header">
            <h1>🦷 Sunrise Dental Clinic</h1>
            <div class="user-info">
                <span>👋 Welcome, <strong><%= fullName %></strong></span>
                <span class="staff-badge">👤 Staff</span>
                <button onclick="showLogoutModal()" class="btn-logout">🚪 Logout</button>
            </div>
        </div>
        
        <div class="welcome-section">
            <h2>Welcome to the Dental Management System</h2>
            <p>Efficiently manage appointments, patients, and billing all in one place.</p>
        </div>
        
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

    <!-- Logout Modal -->
    <div id="logoutModal" class="logout-modal-overlay">
        <div class="logout-modal">
            <div class="modal-header">
                <div class="icon-wrapper">🔐</div>
                <h3>Log out from System?</h3>
                <div class="subtitle">You will need to login again</div>
            </div>
            <div class="modal-body">
                <div class="user-info">
                    <div class="avatar"><%= fullName != null && fullName.length() > 0 ? fullName.charAt(0) : "U" %></div>
                    <div class="user-details">
                        <div class="name"><%= fullName %></div>
                        <div class="role">
                            <span>Active session</span>
                            <span class="badge">STAFF</span>
                        </div>
                    </div>
                </div>
                <div class="warning-text">
                    <span class="icon">⚠️</span>
                    <p>Your current session will be <strong>terminated</strong> and you'll be redirected to the login page.</p>
                </div>
            </div>
            <div class="modal-footer">
                <button onclick="hideLogoutModal()" class="btn-cancel">Cancel</button>
                <button onclick="confirmLogout()" class="btn-logout">Yes, Log out</button>
            </div>
            <div class="modal-version">V1.0 • Sunrise Dental Clinic</div>
        </div>
    </div>

    <script>
        function showLogoutModal() {
            document.getElementById('logoutModal').classList.add('active');
            document.body.style.overflow = 'hidden';
        }
        function hideLogoutModal() {
            document.getElementById('logoutModal').classList.remove('active');
            document.body.style.overflow = '';
        }
        function confirmLogout() {
            window.location.href = '${pageContext.request.contextPath}/logout';
        }
        document.getElementById('logoutModal').addEventListener('click', function(e) {
            if (e.target === this) hideLogoutModal();
        });
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') hideLogoutModal();
        });
    </script>
</body>
</html>