<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Access Denied - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            font-family: 'Times New Roman', serif;
        }
        .error-container {
            background: white;
            padding: 50px 40px;
            border-radius: 15px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            text-align: center;
            max-width: 500px;
        }
        .error-container .icon {
            font-size: 72px;
        }
        .error-container h1 {
            color: #e74c3c;
            font-size: 28px;
            margin: 10px 0;
        }
        .error-container p {
            color: #7f8c8d;
            font-size: 16px;
            margin-bottom: 20px;
        }
        .btn-back {
            background: #667eea;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }
        .btn-back:hover {
            background: #5a67d8;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="icon">🚫</div>
        <h1>Access Denied</h1>
        <p>You do not have permission to access this page.</p>
        <a href="${pageContext.request.contextPath}/dashboard" class="btn-back">Back to Dashboard</a>
    </div>
</body>
</html>