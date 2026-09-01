package com.dental.servlet;

import com.dental.model.Appointment;
import com.dental.service.AppointmentService;
import com.dental.util.SessionManager;
import com.dental.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@WebServlet("/appointment")
public class AppointmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AppointmentService appointmentService;
    private ValidationUtil validator;
    
    @Override
    public void init() throws ServletException {
        super.init();
        appointmentService = new AppointmentService();
        validator = new ValidationUtil();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check session
        if (!SessionManager.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("search".equals(action)) {
            String appNo = request.getParameter("appointmentNo");
            if (appNo != null && !appNo.trim().isEmpty()) {
                Appointment app = appointmentService.getAppointmentDetails(appNo.trim());
                request.setAttribute("appointment", app);
                if (app == null) {
                    request.setAttribute("error", "Appointment not found with number: " + appNo);
                }
            } else {
                request.setAttribute("error", "Please enter an appointment number.");
            }
            request.getRequestDispatcher("/jsp/viewAppointment.jsp").forward(request, response);
        } else {
            // Load form with dentists and treatments
            loadFormData(request);
            request.getRequestDispatcher("/jsp/registerAppointment.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check session
        if (!SessionManager.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("register".equals(action)) {
            registerAppointment(request, response);
        }
    }
    
    private void registerAppointment(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Get form data
            String patientName = request.getParameter("patientName");
            String address = request.getParameter("address");
            String contactNo = request.getParameter("contactNo");
            String email = request.getParameter("email");
            int dentistId = Integer.parseInt(request.getParameter("dentistId"));
            int treatmentId = Integer.parseInt(request.getParameter("treatmentId"));
            Date appointmentDate = Date.valueOf(request.getParameter("appointmentDate"));
            Time appointmentTime = Time.valueOf(request.getParameter("appointmentTime") + ":00");
            
            // Validate
            boolean isValid = true;
            
            if (!validator.isValidName(patientName)) {
                request.setAttribute("nameError", "Please enter a valid patient name.");
                isValid = false;
            }
            
            if (!validator.isValidPhoneNumber(contactNo)) {
                request.setAttribute("phoneError", "Please enter a valid Sri Lankan phone number (e.g., +94771234567 or 0771234567).");
                isValid = false;
            }
            
            if (!validator.isValidEmail(email)) {
                request.setAttribute("emailError", "Please enter a valid email address.");
                isValid = false;
            }
            
            if (!validator.isValidAddress(address)) {
                request.setAttribute("addressError", "Please enter a valid address (minimum 5 characters).");
                isValid = false;
            }
            
            if (!validator.isValidDate(appointmentDate)) {
                request.setAttribute("dateError", "Appointment date must be in the future.");
                isValid = false;
            }
            
            if (!isValid) {
                loadFormData(request);
                request.setAttribute("patientName", patientName);
                request.setAttribute("address", address);
                request.setAttribute("contactNo", contactNo);
                request.setAttribute("email", email);
                request.setAttribute("dentistId", dentistId);
                request.setAttribute("treatmentId", treatmentId);
                request.setAttribute("appointmentDate", appointmentDate);
                request.setAttribute("appointmentTime", appointmentTime);
                request.getRequestDispatcher("/jsp/registerAppointment.jsp").forward(request, response);
                return;
            }
            
            // Create appointment object
            Appointment appointment = new Appointment();
            appointment.setPatientName(patientName);
            appointment.setAddress(address);
            appointment.setContactNo(contactNo);
            appointment.setEmail(email);
            appointment.setDentistId(dentistId);
            appointment.setTreatmentId(treatmentId);
            appointment.setAppointmentDate(appointmentDate);
            appointment.setAppointmentTime(appointmentTime);
            
            // Register appointment
            boolean success = appointmentService.registerAppointment(appointment);
            
            if (success) {
                request.setAttribute("success", "Appointment registered successfully!");
                request.setAttribute("appointment", appointment);
            } else {
                request.setAttribute("error", "Failed to register appointment. Please try again.");
            }
            
            loadFormData(request);
            request.getRequestDispatcher("/jsp/registerAppointment.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            loadFormData(request);
            request.getRequestDispatcher("/jsp/registerAppointment.jsp").forward(request, response);
        }
    }
    
    private void loadFormData(HttpServletRequest request) {
        request.setAttribute("dentists", appointmentService.getDentists());
        request.setAttribute("treatments", appointmentService.getTreatments());
    }
}
