package com.dental.service;

import com.dental.dao.AppointmentDAO;
import com.dental.model.Appointment;
import com.dental.util.ValidationUtil;
import java.sql.Date;
import java.sql.Time;
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
            return false;
        }
        if (appointment.getEmail() != null && !appointment.getEmail().isEmpty() && !validator.isValidEmail(appointment.getEmail())) {
            return false;
        }
        if (!validator.isValidDate(appointment.getAppointmentDate())) {
            return false;
        }
        if (!validator.isValidName(appointment.getPatientName())) {
            return false;
        }
        
        // Generate unique appointment number
        String appNo = generateAppointmentNumber();
        appointment.setAppointmentNo(appNo);
        
        return appointmentDAO.saveAppointment(appointment);
    }
    
    private String generateAppointmentNumber() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new java.util.Date());
        int random = (int)(Math.random() * 9000) + 1000;
        return "APP-" + date + "-" + random;
    }
    
    public Appointment getAppointmentDetails(String appointmentNo) {
        if (appointmentNo == null || appointmentNo.trim().isEmpty()) {
            return null;
        }
        return appointmentDAO.getAppointmentByNo(appointmentNo);
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
