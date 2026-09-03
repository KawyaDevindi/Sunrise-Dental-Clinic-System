<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        /* Additional inline styles for login page */
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            font-family: 'Times New Roman', serif;
        }
        .login-container {
            width: 100%;
            max-width: 420px;
            padding: 20px;
        }
        .login-box {
            background: white;
            padding: 40px 35px;
            border-radius: 15px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            animation: slideUp 0.6s ease;
        }
        @keyframes slideUp {
            from { opacity: 0; transform: translateY(30px); }
            to { opacity: 1; transform: translateY(0); }
        }
        .login-box .logo {
            text-align: center;
            font-size: 48px;
            margin-bottom: 5px;
        }
        .login-box h1 {
            color: #2c3e50;
            text-align: center;
            font-size: 24px;
            margin-bottom: 2px;
        }
        .login-box .subtitle {
            text-align: center;
            color: #7f8c8d;
            font-size: 14px;
            margin-bottom: 25px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            margin-bottom: 6px;
            color: #34495e;
            font-weight: 600;
            font-size: 14px;
        }
        .form-group input {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 14px;
            transition: border-color 0.3s;
            box-sizing: border-box;
        }
        .form-group input:focus {
            border-color: #667eea;
            outline: none;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        .login-btn {
            width: 100%;
            padding: 14px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.3s;
        }
        .login-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
        }
        .error-message {
            background: #fed7d7;
            color: #c0392b;
            padding: 12px;
            border-radius: 8px;
            margin-bottom: 20px;
            text-align: center;
            border-left: 4px solid #c0392b;
        }
        .success-message {
            background: #d5f5e3;
            color: #27ae60;
            padding: 12px;
            border-radius: 8px;
            margin-bottom: 20px;
            text-align: center;
            border-left: 4px solid #27ae60;
        }
        .footer {
            text-align: center;
            margin-top: 20px;
            color: #95a5a6;
            font-size: 12px;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="login-box">
            <div class="logo">🦷</div>
            <h1>Sunrise Dental Clinic</h1>
            <p class="subtitle">Advanced Appointment Management System</p>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="error-message">⚠️ <%= request.getAttribute("error") %></div>
            <% } %>
            
            <% if (request.getAttribute("message") != null) { %>
                <div class="success-message">✅ <%= request.getAttribute("message") %></div>
            <% } %>
            
            <form action="${pageContext.request.contextPath}/login" method="post" autocomplete="off">
                <div class="form-group">
                    <label>👤 Username</label>
                    <input type="text" name="username" placeholder="Enter your username" required>
                </div>
                <div class="form-group">
                    <label>🔒 Password</label>
                    <input type="password" name="password" placeholder="Enter your password" required>
                </div>
                <button type="submit" class="login-btn">Sign In</button>
            </form>
            
            <div class="footer">
                <p>&copy; 2026 Sunrise Dental Clinic. All rights reserved.</p>
            </div>
        </div>
    </div>
</body>
</html>