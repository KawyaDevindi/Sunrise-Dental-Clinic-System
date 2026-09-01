<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dental.model.Bill" %>
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
    <title>Generate Bill - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .bill-container {
            max-width: 800px;
            margin: 30px auto;
            background: white;
            padding: 35px 40px;
            border-radius: 12px;
            box-shadow: 0 2px 20px rgba(0,0,0,0.1);
        }
        .search-box {
            display: flex;
            gap: 15px;
            margin-bottom: 25px;
        }
        .search-box input {
            flex: 1;
            padding: 12px 16px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 14px;
        }
        .search-box input:focus {
            border-color: #667eea;
            outline: none;
        }
        .search-box button {
            padding: 12px 30px;
            background: #27ae60;
            color: white;
            border: none;
            border-radius: 8px;
            font-weight: bold;
            cursor: pointer;
            transition: background 0.3s;
        }
        .search-box button:hover {
            background: #219a52;
        }
        .bill-card {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 25px;
            margin-top: 20px;
            border-left: 5px solid #27ae60;
        }
        .bill-card .header {
            border-bottom: 2px solid #e0e0e0;
            padding-bottom: 15px;
            margin-bottom: 15px;
        }
        .bill-card .header h3 {
            margin: 0;
            color: #2c3e50;
        }
        .bill-details {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 10px;
        }
        .bill-details .item {
            display: flex;
            flex-direction: column;
        }
        .bill-details .item .label {
            font-weight: 600;
            color: #6b7280;
            font-size: 12px;
            text-transform: uppercase;
        }
        .bill-details .item .value {
            font-size: 16px;
            color: #1f2937;
        }
        .total-row {
            grid-column: 1/-1;
            border-top: 2px solid #27ae60;
            padding-top: 15px;
            margin-top: 10px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .total-row .amount {
            font-size: 28px;
            font-weight: bold;
            color: #27ae60;
        }
        .btn-print {
            background: #3498db;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 8px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: background 0.3s;
        }
        .btn-print:hover {
            background: #2980b9;
        }
        .btn-back {
            background: #95a5a6;
            color: white;
            padding: 10px 25px;
            border: none;
            border-radius: 8px;
            text-decoration: none;
            display: inline-block;
            margin-top: 20px;
        }
        .btn-back:hover { background: #7f8c8d; }
        .success-msg {
            background: #d5f5e3;
            color: #27ae60;
            padding: 15px;
            border-radius: 8px;
            border-left: 4px solid #27ae60;
            margin-bottom: 20px;
        }
        .error-msg {
            background: #fdebd0;
            color: #e67e22;
            padding: 15px;
            border-radius: 8px;
            border-left: 4px solid #e67e22;
            margin-bottom: 20px;
        }
        .btn-group {
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
            margin-top: 20px;
        }
        @media (max-width: 600px) {
            .bill-details {
                grid-template-columns: 1fr;
            }
            .search-box {
                flex-direction: column;
            }
            .bill-container {
                padding: 20px;
            }
        }
    </style>
</head>
<body>
    <div class="bill-container">
        <h2>💰 Generate Patient Bill</h2>
        
        <form action="${pageContext.request.contextPath}/bill" method="get" class="search-box">
            <input type="hidden" name="action" value="generate">
            <input type="text" name="appointmentNo" placeholder="Enter Appointment Number" required>
            <button type="submit">Generate Bill</button>
        </form>
        
        <% if (request.getAttribute("success") != null) { %>
            <div class="success-msg">✅ <%= request.getAttribute("success") %></div>
        <% } %>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="error-msg">⚠️ <%= request.getAttribute("error") %></div>
        <% } %>
        
        <% 
            Bill bill = (Bill) request.getAttribute("bill");
            if (bill != null) {
        %>
            <div class="bill-card">
                <div class="header">
                    <h3>📄 Bill #<%= bill.getBillId() %></h3>
                    <p style="color: #6b7280; font-size: 13px;">Generated: <%= bill.getGeneratedDate() %></p>
                </div>
                <div class="bill-details">
                    <div class="item">
                        <span class="label">Appointment No</span>
                        <span class="value"><%= bill.getAppointmentNo() %></span>
                    </div>
                    <div class="item">
                        <span class="label">Patient Name</span>
                        <span class="value"><%= bill.getPatientName() %></span>
                    </div>
                    <div class="item">
                        <span class="label">Consultation Fee</span>
                        <span class="value">LKR <%= String.format("%.2f", bill.getConsultationFee()) %></span>
                    </div>
                    <div class="item">
                        <span class="label">Treatment Cost</span>
                        <span class="value">LKR <%= String.format("%.2f", bill.getTreatmentCost()) %></span>
                    </div>
                    <% if (bill.getDiscount() > 0) { %>
                    <div class="item">
                        <span class="label">Discount</span>
                        <span class="value">- LKR <%= String.format("%.2f", bill.getDiscount()) %></span>
                    </div>
                    <% } %>
                    <div class="total-row">
                        <span style="font-size: 18px; font-weight: bold;">Total Amount</span>
                        <span class="amount">LKR <%= String.format("%.2f", bill.getTotalAmount()) %></span>
                    </div>
                </div>
                <div class="btn-group">
                    <a href="${pageContext.request.contextPath}/bill?action=print&appointmentNo=<%= bill.getAppointmentNo() %>" target="_blank" class="btn-print">🖨️ Print Receipt</a>
                </div>
            </div>
        <% } %>
        
        <a href="${pageContext.request.contextPath}/dashboard" class="btn-back">← Back to Dashboard</a>
    </div>
</body>
</html>