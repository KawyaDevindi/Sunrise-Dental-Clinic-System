<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.dental.model.Appointment, com.dental.model.Bill" %>
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
    <title>Reports - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .report-container {
            max-width: 1100px;
            margin: 30px auto;
            background: white;
            padding: 35px 40px;
            border-radius: 12px;
            box-shadow: 0 2px 20px rgba(0,0,0,0.1);
        }
        .report-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 2px solid #e0e0e0;
            padding-bottom: 15px;
            margin-bottom: 25px;
            flex-wrap: wrap;
            gap: 15px;
        }
        .report-header h2 {
            color: #2c3e50;
        }
        .report-actions {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
        }
        .report-actions a {
            padding: 10px 20px;
            background: #667eea;
            color: white;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 600;
            transition: background 0.3s;
        }
        .report-actions a:hover {
            background: #5a67d8;
        }
        .report-actions a.green {
            background: #27ae60;
        }
        .report-actions a.green:hover {
            background: #219a52;
        }
        .report-actions a.orange {
            background: #f39c12;
        }
        .report-actions a.orange:hover {
            background: #e67e22;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
            font-size: 14px;
        }
        table th {
            background: #2c3e50;
            color: white;
            padding: 12px 15px;
            text-align: left;
            font-weight: 600;
        }
        table td {
            padding: 10px 15px;
            border-bottom: 1px solid #e5e7eb;
        }
        table tr:hover {
            background: #f9fafb;
        }
        .status-badge {
            padding: 3px 12px;
            border-radius: 20px;
            font-size: 11px;
            font-weight: 600;
        }
        .status-Scheduled { background: #dbeafe; color: #2563eb; }
        .status-Completed { background: #d1fae5; color: #065f46; }
        .status-Billed { background: #fef3c7; color: #92400e; }
        .status-Cancelled { background: #fee2e2; color: #991b1b; }
        .empty-state {
            text-align: center;
            padding: 40px 20px;
            color: #6b7280;
        }
        .empty-state .icon {
            font-size: 48px;
            display: block;
            margin-bottom: 10px;
        }
        .summary-stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        .stat-card {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            text-align: center;
        }
        .stat-card .number {
            font-size: 32px;
            font-weight: bold;
            color: #2c3e50;
        }
        .stat-card .label {
            color: #6b7280;
            font-size: 13px;
            margin-top: 5px;
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
        .date-filter {
            display: flex;
            gap: 15px;
            align-items: center;
            margin-bottom: 20px;
            flex-wrap: wrap;
        }
        .date-filter input {
            padding: 10px 14px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
        }
        .date-filter button {
            padding: 10px 25px;
            background: #667eea;
            color: white;
            border: none;
            border-radius: 8px;
            font-weight: bold;
            cursor: pointer;
        }
        @media (max-width: 600px) {
            .report-container { padding: 20px; }
            .report-header { flex-direction: column; align-items: stretch; }
            table { font-size: 12px; }
            table th, table td { padding: 8px 10px; }
            .summary-stats { grid-template-columns: 1fr 1fr; }
        }
    </style>
</head>
<body>
    <div class="report-container">
        <div class="report-header">
            <h2>📊 Clinic Reports</h2>
            <div class="report-actions">
                <a href="${pageContext.request.contextPath}/report?action=allAppointments">📋 All Appointments</a>
                <a href="${pageContext.request.contextPath}/report?action=allBills" class="green">💰 All Bills</a>
                <a href="${pageContext.request.contextPath}/report" class="orange">📈 Summary</a>
            </div>
        </div>
        
        <% 
            String reportTitle = (String) request.getAttribute("reportTitle");
            if (reportTitle != null) {
        %>
            <h3 style="color: #34495e; margin-bottom: 15px;"><%= reportTitle %></h3>
        <% } %>
        
        <!-- Date Filter for Daily Report -->
        <form action="${pageContext.request.contextPath}/report" method="get" class="date-filter">
            <input type="hidden" name="action" value="daily">
            <label>Select Date:</label>
            <input type="date" name="reportDate" value="<%= request.getAttribute("reportDate") != null ? request.getAttribute("reportDate") : "" %>">
            <button type="submit">View Daily Report</button>
        </form>
        
        <!-- Summary Stats -->
        <% if (request.getAttribute("totalAppointments") != null) { %>
            <div class="summary-stats">
                <div class="stat-card">
                    <div class="number"><%= request.getAttribute("totalAppointments") %></div>
                    <div class="label">Total Appointments</div>
                </div>
                <div class="stat-card">
                    <div class="number"><%= request.getAttribute("totalBills") %></div>
                    <div class="label">Total Bills Generated</div>
                </div>
                <div class="stat-card" style="background: #d5f5e3;">
                    <div class="number" style="color: #27ae60;">
                        <% 
                            Double revenue = (Double) request.getAttribute("totalRevenue");
                            if (revenue == null) revenue = 0.0;
                            out.print("LKR " + String.format("%.2f", revenue));
                        %>
                    </div>
                    <div class="label">Total Revenue</div>
                </div>
            </div>
        <% } %>
        
        <!-- Appointments Table -->
        <%
            List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
            if (appointments != null && !appointments.isEmpty()) {
        %>
            <div style="overflow-x: auto;">
                <table>
                    <thead>
                        <tr>
                            <th>Appointment No</th>
                            <th>Patient</th>
                            <th>Dentist</th>
                            <th>Treatment</th>
                            <th>Date</th>
                            <th>Time</th>
                            <th>Status</th>
                            <th>Cost</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Appointment app : appointments) { %>
                            <tr>
                                <td><strong><%= app.getAppointmentNo() %></strong></td>
                                <td><%= app.getPatientName() %></td>
                                <td><%= app.getDentistName() != null ? app.getDentistName() : "N/A" %></td>
                                <td><%= app.getTreatmentName() != null ? app.getTreatmentName() : "N/A" %></td>
                                <td><%= app.getAppointmentDate() %></td>
                                <td><%= app.getAppointmentTime() %></td>
                                <td><span class="status-badge status-<%= app.getStatus() %>"><%= app.getStatus() %></span></td>
                                <td><%= app.getTotalCost() > 0 ? "LKR " + String.format("%.2f", app.getTotalCost()) : "-" %></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        <%
            }
        %>
        
        <!-- Bills Table -->
        <%
            List<Bill> bills = (List<Bill>) request.getAttribute("bills");
            if (bills != null && !bills.isEmpty()) {
        %>
            <div style="overflow-x: auto; margin-top: 30px;">
                <h4 style="color: #34495e; margin-bottom: 10px;">Bills</h4>
                <table>
                    <thead>
                        <tr>
                            <th>Bill ID</th>
                            <th>Appointment No</th>
                            <th>Patient</th>
                            <th>Consultation</th>
                            <th>Treatment</th>
                            <th>Total</th>
                            <th>Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Bill bill : bills) { %>
                            <tr>
                                <td>#<%= bill.getBillId() %></td>
                                <td><%= bill.getAppointmentNo() %></td>
                                <td><%= bill.getPatientName() %></td>
                                <td>LKR <%= String.format("%.2f", bill.getConsultationFee()) %></td>
                                <td>LKR <%= String.format("%.2f", bill.getTreatmentCost()) %></td>
                                <td><strong>LKR <%= String.format("%.2f", bill.getTotalAmount()) %></strong></td>
                                <td><%= bill.getGeneratedDate() %></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        <%
            }
        %>
        
        <!-- Empty State -->
        <%
            if (appointments == null && bills == null && request.getAttribute("totalAppointments") == null) {
        %>
            <div class="empty-state">
                <span class="icon">📋</span>
                <p>Select a report type from the buttons above to view data.</p>
            </div>
        <%
            } else if ((appointments != null && appointments.isEmpty()) && (bills == null || bills.isEmpty())) {
        %>
            <div class="empty-state">
                <span class="icon">📭</span>
                <p>No records found for the selected report.</p>
            </div>
        <%
            }
        %>
        
        <a href="${pageContext.request.contextPath}/dashboard" class="btn-back">← Back to Dashboard</a>
    </div>
</body>
</html>