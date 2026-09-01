<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dental.model.Appointment" %>
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
    <title>View Appointment - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .view-container {
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
            background: #667eea;
            color: white;
            border: none;
            border-radius: 8px;
            font-weight: bold;
            cursor: pointer;
            transition: background 0.3s;
        }
        .search-box button:hover {
            background: #5a67d8;
        }
        .appointment-card {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 25px;
            margin-top: 20px;
            border-left: 5px solid #667eea;
        }
        .appointment-card .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 2px solid #e0e0e0;
            padding-bottom: 15px;
            margin-bottom: 15px;
        }
        .appointment-card .header .status {
            padding: 5px 15px;
            border-radius: 20px;
            font-weight: bold;
            font-size: 12px;
        }
        .status-scheduled { background: #dbeafe; color: #2563eb; }
        .status-completed { background: #d1fae5; color: #065f46; }
        .status-billed { background: #fef3c7; color: #92400e; }
        .status-cancelled { background: #fee2e2; color: #991b1b; }
        .appointment-card .details {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 12px;
        }
        .appointment-card .details .item {
            display: flex;
            flex-direction: column;
        }
        .appointment-card .details .item .label {
            font-weight: 600;
            color: #6b7280;
            font-size: 12px;
            text-transform: uppercase;
        }
        .appointment-card .details .item .value {
            font-size: 16px;
            color: #1f2937;
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
        .not-found {
            background: #fef2f2;
            color: #991b1b;
            padding: 20px;
            border-radius: 8px;
            text-align: center;
            margin-top: 20px;
        }
        @media (max-width: 600px) {
            .appointment-card .details {
                grid-template-columns: 1fr;
            }
            .search-box {
                flex-direction: column;
            }
            .view-container {
                padding: 20px;
            }
        }
    </style>
</head>
<body>
    <div class="view-container">
        <h2>🔍 View Appointment Details</h2>
        
        <form action="${pageContext.request.contextPath}/appointment" method="get" class="search-box">
            <input type="hidden" name="action" value="search">
            <input type="text" name="appointmentNo" placeholder="Enter Appointment Number (e.g., APP-20260901-001)" required>
            <button type="submit">Search</button>
        </form>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="not-found">⚠️ <%= request.getAttribute("error") %></div>
        <% } %>
        
        <% 
            Appointment app = (Appointment) request.getAttribute("appointment");
            if (app != null) {
                String statusClass = "status-" + app.getStatus().toLowerCase();
        %>
            <div class="appointment-card">
                <div class="header">
                    <h3>Appointment: <strong><%= app.getAppointmentNo() %></strong></h3>
                    <span class="status <%= statusClass %>"><%= app.getStatus() %></span>
                </div>
                <div class="details">
                    <div class="item">
                        <span class="label">Patient Name</span>
                        <span class="value"><%= app.getPatientName() %></span>
                    </div>
                    <div class="item">
                        <span class="label">Contact Number</span>
                        <span class="value"><%= app.getContactNo() %></span>
                    </div>
                    <div class="item">
                        <span class="label">Email</span>
                        <span class="value"><%= app.getEmail() != null ? app.getEmail() : "N/A" %></span>
                    </div>
                    <div class="item">
                        <span class="label">Address</span>
                        <span class="value"><%= app.getAddress() != null ? app.getAddress() : "N/A" %></span>
                    </div>
                    <div class="item">
                        <span class="label">Dentist</span>
                        <span class="value"><%= app.getDentistName() != null ? app.getDentistName() : "Not Assigned" %></span>
                    </div>
                    <div class="item">
                        <span class="label">Treatment</span>
                        <span class="value"><%= app.getTreatmentName() != null ? app.getTreatmentName() : "Not Assigned" %></span>
                    </div>
                    <div class="item">
                        <span class="label">Date</span>
                        <span class="value"><%= app.getAppointmentDate() %></span>
                    </div>
                    <div class="item">
                        <span class="label">Time</span>
                        <span class="value"><%= app.getAppointmentTime() %></span>
                    </div>
                    <div class="item">
                        <span class="label">Treatment Cost</span>
                        <span class="value">LKR <%= String.format("%.2f", app.getTreatmentCost()) %></span>
                    </div>
                    <div class="item">
                        <span class="label">Consultation Fee</span>
                        <span class="value">LKR <%= String.format("%.2f", app.getConsultationFee()) %></span>
                    </div>
                    <% if (app.getTotalCost() > 0) { %>
                    <div class="item" style="grid-column: 1/-1; border-top: 2px solid #e0e0e0; padding-top: 10px; margin-top: 5px;">
                        <span class="label" style="font-size: 16px; color: #2c3e50;">Total Cost</span>
                        <span class="value" style="font-size: 20px; font-weight: bold; color: #27ae60;">LKR <%= String.format("%.2f", app.getTotalCost()) %></span>
                    </div>
                    <% } %>
                </div>
            </div>
        <% } %>
        
        <a href="${pageContext.request.contextPath}/dashboard" class="btn-back">← Back to Dashboard</a>
    </div>
</body>
</html>