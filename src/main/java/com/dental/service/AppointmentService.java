package com.dental.service;

import com.dental.dao.AppointmentDAO;
import com.dental.model.Appointment;
import com.dental.util.ValidationUtil;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class AppointmentService {
    private AppointmentDAO appointmentDAO;
    private ValidationUtil validator;
    
    public AppointmentService() {
        this.appointmentDAO = new AppointmentDAO();
        this.validator = new ValidationUtil();
    }
    
    public boolean registerAppointment(Appointment appointment) {
        // Validate input
        if (appointment.getContactNo() == null || !validator.isValidPhoneNumber(appointment.getContactNo())) {
            System.err.println("❌ Invalid phone number: " + appointment.getContactNo());
            return false;
        }
        if (appointment.getEmail() != null && !appointment.getEmail().isEmpty() && !validator.isValidEmail(appointment.getEmail())) {
            System.err.println("❌ Invalid email: " + appointment.getEmail());
            return false;
        }
        if (!validator.isValidDate(appointment.getAppointmentDate())) {
            System.err.println("❌ Invalid date: " + appointment.getAppointmentDate());
            return false;
        }
        if (!validator.isValidName(appointment.getPatientName())) {
            System.err.println("❌ Invalid patient name: " + appointment.getPatientName());
            return false;
        }
        
        // Generate unique appointment number
        String appNo = generateAppointmentNumber();
        appointment.setAppointmentNo(appNo);
        System.out.println("✅ Generated appointment number: " + appNo);
        
        return appointmentDAO.saveAppointment(appointment);
    }
    
    private String generateAppointmentNumber() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new java.util.Date());
        int random = (int)(Math.random() * 9000) + 1000;
        String appNo = "APP-" + date + "-" + random;
        System.out.println("🔄 Generated: " + appNo);
        return appNo;
    }
    
    public Appointment getAppointmentDetails(String appointmentNo) {
        if (appointmentNo == null || appointmentNo.trim().isEmpty()) {
            System.err.println("❌ Appointment number is null or empty");
            return null;
        }
        
        String cleanAppNo = appointmentNo.trim();
        System.out.println("🔍 AppointmentService - Searching for: " + cleanAppNo);
        
        Appointment app = appointmentDAO.getAppointmentByNo(cleanAppNo);
        if (app == null) {
            System.err.println("❌ AppointmentService - Not found: " + cleanAppNo);
        } else {
            System.out.println("✅ AppointmentService - Found: " + cleanAppNo + " - Patient: " + app.getPatientName());
        }
        return app;
    }
    
    public List<Appointment> getAllAppointments() {
        return appointmentDAO.getAllAppointments();
    }
    
    public List<Appointment> getAppointmentsByDateRange(Date startDate, Date endDate) {
        return appointmentDAO.getAppointmentsByDate(startDate, endDate);
    }
    
    public List<Object[]> getDentists() {
        return appointmentDAO.getDentists();
    }
    
    public List<Object[]> getTreatments() {
        return appointmentDAO.getTreatments();
    }
}