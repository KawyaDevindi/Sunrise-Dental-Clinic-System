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
            
            int result = pstmt.executeUpdate();
            System.out.println("✅ Appointment saved: " + appointment.getAppointmentNo());
            return result > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error saving appointment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Get appointment by number - FIXED VERSION
    public Appointment getAppointmentByNo(String appointmentNo) {
        if (appointmentNo == null || appointmentNo.trim().isEmpty()) {
            System.err.println("❌ Appointment number is null or empty");
            return null;
        }
        
        String cleanAppNo = appointmentNo.trim();
        System.out.println("🔍 Searching for appointment: " + cleanAppNo);
        
        // Try direct SQL query first (more reliable)
        String sql = "SELECT a.*, d.dentist_name, d.consultation_fee, t.treatment_name, t.treatment_cost " +
                     "FROM appointments a " +
                     "LEFT JOIN dentists d ON a.dentist_id = d.dentist_id " +
                     "LEFT JOIN treatments t ON a.treatment_id = t.treatment_id " +
                     "WHERE a.appointment_no = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cleanAppNo);
            ResultSet rs = pstmt.executeQuery();
            
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
                
                // Joined fields - using correct column names
                app.setDentistName(rs.getString("dentist_name"));
                app.setTreatmentName(rs.getString("treatment_name"));
                app.setTreatmentCost(rs.getDouble("treatment_cost"));
                app.setConsultationFee(rs.getDouble("consultation_fee"));
                
                System.out.println("✅ Found appointment: " + cleanAppNo);
                return app;
            } else {
                System.out.println("❌ No appointment found for: " + cleanAppNo);
            }
        } catch (SQLException e) {
            System.err.println("❌ SQL Error in getAppointmentByNo: " + e.getMessage());
            e.printStackTrace();
        }
        
        // If direct query fails, try stored procedure as fallback
        System.out.println("🔄 Trying stored procedure fallback...");
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             CallableStatement cstmt = conn.prepareCall("{CALL GetAppointmentDetails(?)}")) {
            
            cstmt.setString(1, cleanAppNo);
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
                app.setDentistName(rs.getString("dentist_name"));
                app.setTreatmentName(rs.getString("treatment_name"));
                app.setTreatmentCost(rs.getDouble("treatment_cost"));
                app.setConsultationFee(rs.getDouble("consultation_fee"));
                
                System.out.println("✅ Found via stored procedure: " + cleanAppNo);
                return app;
            }
        } catch (SQLException e) {
            System.err.println("❌ Stored procedure error: " + e.getMessage());
        }
        
        return null;
    }
    
    // Get all appointments
    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, d.dentist_name, d.consultation_fee, t.treatment_name, t.treatment_cost FROM appointments a LEFT JOIN dentists d ON a.dentist_id = d.dentist_id LEFT JOIN treatments t ON a.treatment_id = t.treatment_id ORDER BY a.appointment_date DESC, a.appointment_time ASC";
        
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
                app.setConsultationFee(rs.getDouble("consultation_fee"));
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
        String sql = "SELECT a.*, d.dentist_name, d.consultation_fee, t.treatment_name FROM appointments a LEFT JOIN dentists d ON a.dentist_id = d.dentist_id LEFT JOIN treatments t ON a.treatment_id = t.treatment_id WHERE a.appointment_date BETWEEN ? AND ? ORDER BY a.appointment_date ASC";
        
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
                app.setConsultationFee(rs.getDouble("consultation_fee"));
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