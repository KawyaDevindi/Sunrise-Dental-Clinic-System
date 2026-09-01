<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("username") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Help - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .help-container {
            max-width: 900px;
            margin: 30px auto;
            background: white;
            padding: 35px 40px;
            border-radius: 12px;
            box-shadow: 0 2px 20px rgba(0,0,0,0.1);
        }
        .help-container h2 {
            color: #2c3e50;
            border-bottom: 2px solid #667eea;
            padding-bottom: 15px;
            margin-bottom: 25px;
        }
        .help-section {
            margin-bottom: 30px;
            padding: 20px;
            background: #f8f9fa;
            border-radius: 10px;
            border-left: 4px solid #667eea;
        }
        .help-section h3 {
            color: #2c3e50;
            margin-bottom: 10px;
            font-size: 18px;
        }
        .help-section h3 .icon {
            margin-right: 10px;
        }
        .help-section ol, .help-section ul {
            padding-left: 25px;
            color: #34495e;
        }
        .help-section li {
            margin-bottom: 8px;
            line-height: 1.6;
        }
        .help-section .tip {
            background: #ebf5fb;
            padding: 12px 18px;
            border-radius: 8px;
            margin-top: 10px;
            border-left: 3px solid #3498db;
        }
        .help-section .tip strong {
            color: #2c3e50;
        }
        .btn-back {
            background: #95a5a6;
            color: white;
            padding: 10px 25px;
            border: none;
            border-radius: 8px;
            text-decoration: none;
            display: inline-block;
            margin-top: 10px;
        }
        .btn-back:hover { background: #7f8c8d; }
        .quick-links {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
            gap: 15px;
            margin-bottom: 20px;
        }
        .quick-links a {
            background: #667eea;
            color: white;
            padding: 15px;
            border-radius: 8px;
            text-align: center;
            text-decoration: none;
            font-weight: 600;
            transition: transform 0.2s, background 0.3s;
        }
        .quick-links a:hover {
            transform: translateY(-3px);
            background: #5a67d8;
        }
        @media (max-width: 600px) {
            .help-container { padding: 20px; }
            .quick-links { grid-template-columns: 1fr 1fr; }
        }
    </style>
</head>
<body>
    <div class="help-container">
        <h2>❓ Help & User Guide</h2>
        
        <div class="quick-links">
            <a href="#login">🔑 Login</a>
            <a href="#register">📝 Register Appointment</a>
            <a href="#view">🔍 View Appointment</a>
            <a href="#bill">💰 Generate Bill</a>
            <a href="#reports">📊 Reports</a>
        </div>
        
        <div id="login" class="help-section">
            <h3><span class="icon">🔑</span> 1. User Authentication (Login)</h3>
            <ol>
                <li>Open the application and you will see the <strong>Login</strong> page.</li>
                <li>Enter your <strong>Username</strong> and <strong>Password</strong>.</li>
                <li>Click the <strong>"Sign In"</strong> button.</li>
                <li>If the credentials are correct, you will be directed to the <strong>Dashboard</strong>.</li>
                <li>If you enter wrong credentials, an error message will be shown.</li>
            </ol>
            <div class="tip">
                <strong>💡 Tip:</strong> Demo credentials: <code>admin / admin@123</code> or <code>staff1 / staff@123</code>
            </div>
        </div>
        
        <div id="register" class="help-section">
            <h3><span class="icon">📝</span> 2. Register New Appointment</h3>
            <ol>
                <li>From the Dashboard, click <strong>"Register Appointment"</strong>.</li>
                <li>Fill in the patient details:
                    <ul>
                        <li><strong>Patient Full Name</strong> - Full name of the patient</li>
                        <li><strong>Address</strong> - Complete address</li>
                        <li><strong>Contact Number</strong> - Sri Lankan phone number (e.g., +94771234567)</li>
                        <li><strong>Email Address</strong> - Optional but recommended</li>
                    </ul>
                </li>
                <li>Select the <strong>Dentist</strong> and <strong>Treatment Type</strong> from the dropdown lists.</li>
                <li>Choose the <strong>Appointment Date</strong> and <strong>Time</strong>.</li>
                <li>Click <strong>"Register Appointment"</strong>.</li>
                <li>If successful, you will see a confirmation with the <strong>Appointment Number</strong>.</li>
            </ol>
            <div class="tip">
                <strong>💡 Tip:</strong> All fields marked with <strong>*</strong> are required. Invalid entries will show validation errors.
            </div>
        </div>
        
        <div id="view" class="help-section">
            <h3><span class="icon">🔍</span> 3. View Appointment Details</h3>
            <ol>
                <li>From the Dashboard, click <strong>"View Appointment"</strong>.</li>
                <li>Enter the <strong>Appointment Number</strong> (e.g., APP-20260901-001).</li>
                <li>Click <strong>"Search"</strong>.</li>
                <li>The system will display:
                    <ul>
                        <li>Patient details (name, contact, address, email)</li>
                        <li>Assigned dentist and treatment</li>
                        <li>Appointment date and time</li>
                        <li>Status of the appointment</li>
                        <li>Cost details (if bill has been generated)</li>
                    </ul>
                </li>
            </ol>
            <div class="tip">
                <strong>💡 Tip:</strong> If the appointment is not found, you will see an error message. Double-check the appointment number.
            </div>
        </div>
        
        <div id="bill" class="help-section">
            <h3><span class="icon">💰</span> 4. Calculate and Print Bill</h3>
            <ol>
                <li>From the Dashboard, click <strong>"Generate Bill"</strong>.</li>
                <li>Enter the <strong>Appointment Number</strong> for which you want to generate a bill.</li>
                <li>Click <strong>"Generate Bill"</strong>.</li>
                <li>The system will calculate:
                    <ul>
                        <li><strong>Consultation Fee</strong> - Based on the assigned dentist</li>
                        <li><strong>Treatment Cost</strong> - Based on the selected treatment</li>
                        <li><strong>Total Amount</strong> - Sum of both</li>
                    </ul>
                </li>
                <li>Click <strong>"Print Receipt"</strong> to view and print a formatted receipt.</li>
            </ol>
            <div class="tip">
                <strong>⚠️ Note:</strong> A bill can only be generated once per appointment. If a bill already exists, the system will notify you.
            </div>
        </div>
        
        <div id="reports" class="help-section">
            <h3><span class="icon">📊</span> 5. Reports</h3>
            <ol>
                <li>From the Dashboard, click <strong>"Reports"</strong>.</li>
                <li>You will see three report options:
                    <ul>
                        <li><strong>All Appointments</strong> - Shows all registered appointments</li>
                        <li><strong>All Bills</strong> - Shows all generated bills</li>
                        <li><strong>Daily Report</strong> - Enter a date to see appointments for that specific day</li>
                    </ul>
                </li>
                <li>Click on any button to view the report.</li>
                <li>Reports are presented in a table format for easy viewing.</li>
            </ol>
            <div class="tip">
                <strong>💡 Tip:</strong> Use the "Daily Report" to track appointments and revenue for specific days.
            </div>
        </div>
        
        <div class="help-section" style="border-left-color: #e74c3c;">
            <h3><span class="icon">🚪</span> 6. Exit System</h3>
            <ol>
                <li>Click the <strong>"Logout"</strong> button in the top-right corner of the Dashboard.</li>
                <li>You will be redirected to the Login page.</li>
                <li>For security, always logout after completing your session.</li>
            </ol>
            <div class="tip" style="border-left-color: #e74c3c;">
                <strong>🔒 Security Tip:</strong> Always log out when leaving your workstation to protect patient data.
            </div>
        </div>
        
        <div class="help-section" style="border-left-color: #f39c12;">
            <h3><span class="icon">📞</span> Support</h3>
            <p>If you encounter any issues, please contact:</p>
            <ul>
                <li><strong>IT Support:</strong> +94 11 234 5678</li>
                <li><strong>Email:</strong> support@sunrisedental.com</li>
                <li><strong>System Administrator:</strong> admin@sunrisedental.com</li>
            </ul>
        </div>
        
        <a href="${pageContext.request.contextPath}/dashboard" class="btn-back">← Back to Dashboard</a>
    </div>
</body>
</html>