<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
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
    <title>Register Appointment - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .form-container {
            max-width: 700px;
            margin: 30px auto;
            background: white;
            padding: 35px 40px;
            border-radius: 12px;
            box-shadow: 0 2px 20px rgba(0,0,0,0.1);
        }
        .form-container h2 {
            color: #2c3e50;
            border-bottom: 2px solid #667eea;
            padding-bottom: 15px;
            margin-bottom: 25px;
        }
        .form-group {
            margin-bottom: 18px;
        }
        .form-group label {
            display: block;
            font-weight: 600;
            color: #34495e;
            margin-bottom: 5px;
            font-size: 14px;
        }
        .form-group input, .form-group select, .form-group textarea {
            width: 100%;
            padding: 10px 14px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 14px;
            font-family: 'Times New Roman', serif;
            box-sizing: border-box;
            transition: border-color 0.3s;
        }
        .form-group input:focus, .form-group select:focus, .form-group textarea:focus {
            border-color: #667eea;
            outline: none;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        .form-group textarea {
            resize: vertical;
            min-height: 60px;
        }
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }
        .btn-submit {
            background: linear-gradient(135deg, #27ae60 0%, #2ecc71 100%);
            color: white;
            padding: 14px 30px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: transform 0.2s;
            width: 100%;
            margin-top: 10px;
        }
        .btn-submit:hover {
            transform: translateY(-2px);
        }
        .btn-back {
            background: #95a5a6;
            color: white;
            padding: 10px 25px;
            border: none;
            border-radius: 8px;
            text-decoration: none;
            display: inline-block;
            margin-top: 15px;
        }
        .btn-back:hover {
            background: #7f8c8d;
        }
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
        .field-error {
            color: #e74c3c;
            font-size: 12px;
            margin-top: 3px;
        }
        @media (max-width: 600px) {
            .form-row {
                grid-template-columns: 1fr;
            }
            .form-container {
                padding: 20px;
            }
        }
    </style>
    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
</head>
<body>
    <div class="form-container">
        <h2>📝 Register New Appointment</h2>
        
        <% if (request.getAttribute("success") != null) { %>
            <div class="success-msg">✅ <%= request.getAttribute("success") %></div>
            <% if (request.getAttribute("appointment") != null) { 
                com.dental.model.Appointment app = (com.dental.model.Appointment) request.getAttribute("appointment");
            %>
                <div style="background: #ebf5fb; padding: 15px; border-radius: 8px; margin-bottom: 20px;">
                    <strong>Appointment Number:</strong> <%= app.getAppointmentNo() %><br>
                    <strong>Patient:</strong> <%= app.getPatientName() %><br>
                    <strong>Date:</strong> <%= app.getAppointmentDate() %>
                </div>
            <% } %>
        <% } %>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="error-msg">⚠️ <%= request.getAttribute("error") %></div>
        <% } %>
        
        <form action="${pageContext.request.contextPath}/appointment" method="post" onsubmit="return validateAppointmentForm()">
            <input type="hidden" name="action" value="register">
            
            <div class="form-group">
                <label>Patient Full Name *</label>
                <input type="text" name="patientName" id="patientName" placeholder="e.g., Mr. Saman Kumara" value="<%= request.getAttribute("patientName") != null ? request.getAttribute("patientName") : "" %>" required>
                <div id="nameError" class="field-error"><%= request.getAttribute("nameError") != null ? request.getAttribute("nameError") : "" %></div>
            </div>
            
            <div class="form-group">
                <label>Address *</label>
                <textarea name="address" id="address" rows="2" placeholder="Enter full address" required><%= request.getAttribute("address") != null ? request.getAttribute("address") : "" %></textarea>
                <div id="addressError" class="field-error"><%= request.getAttribute("addressError") != null ? request.getAttribute("addressError") : "" %></div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label>Contact Number *</label>
                    <input type="tel" name="contactNo" id="contactNo" placeholder="e.g., +94771234567" value="<%= request.getAttribute("contactNo") != null ? request.getAttribute("contactNo") : "" %>" required>
                    <div id="phoneError" class="field-error"><%= request.getAttribute("phoneError") != null ? request.getAttribute("phoneError") : "" %></div>
                </div>
                <div class="form-group">
                    <label>Email Address</label>
                    <input type="email" name="email" id="email" placeholder="patient@example.com" value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>">
                    <div id="emailError" class="field-error"><%= request.getAttribute("emailError") != null ? request.getAttribute("emailError") : "" %></div>
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label>Select Dentist *</label>
                    <select name="dentistId" id="dentistId" required>
                        <option value="">-- Select Dentist --</option>
                        <% 
                            List<Object[]> dentists = (List<Object[]>) request.getAttribute("dentists");
                            if (dentists != null) {
                                Integer selectedDentist = (Integer) request.getAttribute("dentistId");
                                for (Object[] d : dentists) {
                                    int id = (Integer) d[0];
                                    String name = (String) d[1];
                                    double fee = (Double) d[2];
                                    String selected = (selectedDentist != null && selectedDentist == id) ? "selected" : "";
                        %>
                            <option value="<%= id %>" <%= selected %>><%= name %> (LKR <%= String.format("%.2f", fee) %>)</option>
                        <%      }
                            }
                        %>
                    </select>
                </div>
                <div class="form-group">
                    <label>Treatment Type *</label>
                    <select name="treatmentId" id="treatmentId" required>
                        <option value="">-- Select Treatment --</option>
                        <% 
                            List<Object[]> treatments = (List<Object[]>) request.getAttribute("treatments");
                            if (treatments != null) {
                                Integer selectedTreatment = (Integer) request.getAttribute("treatmentId");
                                for (Object[] t : treatments) {
                                    int id = (Integer) t[0];
                                    String name = (String) t[1];
                                    double cost = (Double) t[2];
                                    String selected = (selectedTreatment != null && selectedTreatment == id) ? "selected" : "";
                        %>
                            <option value="<%= id %>" <%= selected %>><%= name %> (LKR <%= String.format("%.2f", cost) %>)</option>
                        <%      }
                            }
                        %>
                    </select>
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label>Appointment Date *</label>
                    <input type="date" name="appointmentDate" id="appointmentDate" value="<%= request.getAttribute("appointmentDate") != null ? request.getAttribute("appointmentDate") : "" %>" required>
                    <div id="dateError" class="field-error"><%= request.getAttribute("dateError") != null ? request.getAttribute("dateError") : "" %></div>
                </div>
                <div class="form-group">
                    <label>Appointment Time *</label>
                    <input type="time" name="appointmentTime" id="appointmentTime" value="<%= request.getAttribute("appointmentTime") != null ? request.getAttribute("appointmentTime") : "09:00" %>" required>
                </div>
            </div>
            
            <button type="submit" class="btn-submit">✅ Register Appointment</button>
        </form>
        
        <a href="${pageContext.request.contextPath}/dashboard" class="btn-back">← Back to Dashboard</a>
    </div>
</body>
</html>