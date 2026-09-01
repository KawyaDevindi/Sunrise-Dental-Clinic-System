package com.dental.dao;

import com.dental.model.Appointment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    
    // Save new appointment
    public boolean saveAppointment(Appointment appointment) {
        String sql = "INSERT INTO appointments (appointment_no, patient_name, address, contact_no, email, dentist_id, treatment_id, appointment_date, appointment_time, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, appointment.getAppointmentNo());
            pstmt.setString(2, appointment.getPatientName());
            pstmt.setString(3, appointment.getAddress());
            pstmt.setString(4, appointment.getContactNo());
            pstmt.setString(5, appointment.getEmail());
            pstmt.setInt(6, appointment.getDentistId());
            pstmt.setInt(7, appointment.getTreatmentId());
            pstmt.setDate(8, appointment.getAppointmentDate());
            pstmt.setTime(9, appointment.getAppointmentTime());
            pstmt.setString(10, "Scheduled");
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get appointment by number using stored procedure
    public Appointment getAppointmentByNo(String appointmentNo) {
        String sql = "{CALL GetAppointmentDetails(?)}";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setString(1, appointmentNo);
            ResultSet rs = cstmt.executeQuery();
            
            if (rs.next()) {
                Appointment app = new Appointment();
                app.setAppointmentNo(rs.getString("appointment_no"));
                app.setPatientName(rs.getString("patient_name"));
                app.setAddress(rs.getString("address"));
                app.setContactNo(rs.getString("contact_no"));
                app.setEmail(rs.getString("email"));
                app.setDentistId(rs.getInt("dentist_id"));
                app.setTreatmentId(rs.getInt("treatment_id"));
                app.setAppointmentDate(rs.getDate("appointment_date"));
                app.setAppointmentTime(rs.getTime("appointment_time"));
                app.setStatus(rs.getString("status"));
                app.setTotalCost(rs.getDouble("total_cost"));
                
                // Joined fields
                app.setDentistName(rs.getString("dentist_name"));
                app.setTreatmentName(rs.getString("treatment_name"));
                app.setTreatmentCost(rs.getDouble("treatment_cost"));
                app.setConsultationFee(rs.getDouble("consultation_fee"));
                
                return app;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Get all appointments
    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, d.dentist_name, t.treatment_name, t.treatment_cost FROM appointments a LEFT JOIN dentists d ON a.dentist_id = d.dentist_id LEFT JOIN treatments t ON a.treatment_id = t.treatment_id ORDER BY a.appointment_date DESC, a.appointment_time ASC";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Appointment app = new Appointment();
                app.setAppointmentNo(rs.getString("appointment_no"));
                app.setPatientName(rs.getString("patient_name"));
                app.setAddress(rs.getString("address"));
                app.setContactNo(rs.getString("contact_no"));
                app.setEmail(rs.getString("email"));
                app.setDentistId(rs.getInt("dentist_id"));
                app.setTreatmentId(rs.getInt("treatment_id"));
                app.setAppointmentDate(rs.getDate("appointment_date"));
                app.setAppointmentTime(rs.getTime("appointment_time"));
                app.setStatus(rs.getString("status"));
                app.setTotalCost(rs.getDouble("total_cost"));
                app.setDentistName(rs.getString("dentist_name"));
                app.setTreatmentName(rs.getString("treatment_name"));
                app.setTreatmentCost(rs.getDouble("treatment_cost"));
                list.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Get appointments by date range
    public List<Appointment> getAppointmentsByDate(Date startDate, Date endDate) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, d.dentist_name, t.treatment_name FROM appointments a LEFT JOIN dentists d ON a.dentist_id = d.dentist_id LEFT JOIN treatments t ON a.treatment_id = t.treatment_id WHERE a.appointment_date BETWEEN ? AND ? ORDER BY a.appointment_date ASC";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Appointment app = new Appointment();
                app.setAppointmentNo(rs.getString("appointment_no"));
                app.setPatientName(rs.getString("patient_name"));
                app.setAddress(rs.getString("address"));
                app.setContactNo(rs.getString("contact_no"));
                app.setEmail(rs.getString("email"));
                app.setAppointmentDate(rs.getDate("appointment_date"));
                app.setAppointmentTime(rs.getTime("appointment_time"));
                app.setStatus(rs.getString("status"));
                app.setTotalCost(rs.getDouble("total_cost"));
                app.setDentistName(rs.getString("dentist_name"));
                app.setTreatmentName(rs.getString("treatment_name"));
                list.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Get dentist list
    public List<Object[]> getDentists() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT dentist_id, dentist_name, consultation_fee FROM dentists ORDER BY dentist_name";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new Object[]{rs.getInt("dentist_id"), rs.getString("dentist_name"), rs.getDouble("consultation_fee")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Get treatment list
    public List<Object[]> getTreatments() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT treatment_id, treatment_name, treatment_cost FROM treatments ORDER BY treatment_name";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new Object[]{rs.getInt("treatment_id"), rs.getString("treatment_name"), rs.getDouble("treatment_cost")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
