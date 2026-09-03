package com.dental.dao;

import com.dental.model.Bill;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {
    
    // Generate bill using stored procedure
    public Bill generateBill(String appointmentNo) {
        if (appointmentNo == null || appointmentNo.trim().isEmpty()) {
            System.err.println("❌ Appointment number is null or empty");
            return null;
        }
        
        String cleanAppNo = appointmentNo.trim();
        System.out.println("🔍 Generating bill for: " + cleanAppNo);
        
        String sql = "{CALL GenerateBill(?)}";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setString(1, cleanAppNo);
            ResultSet rs = cstmt.executeQuery();
            
            if (rs.next()) {
                Bill bill = new Bill();
                bill.setBillId(rs.getInt("bill_id"));
                bill.setAppointmentNo(rs.getString("appointment_no"));
                bill.setPatientName(rs.getString("patient_name"));
                bill.setConsultationFee(rs.getDouble("consultation_fee"));
                bill.setTreatmentCost(rs.getDouble("treatment_cost"));
                bill.setDiscount(rs.getDouble("discount"));
                bill.setTotalAmount(rs.getDouble("total_amount"));
                bill.setGeneratedDate(rs.getString("generated_date"));
                System.out.println("✅ Bill generated for: " + cleanAppNo);
                return bill;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error generating bill: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    // Get bill by appointment number
    public Bill getBillByAppointmentNo(String appointmentNo) {
        if (appointmentNo == null || appointmentNo.trim().isEmpty()) {
            return null;
        }
        
        String cleanAppNo = appointmentNo.trim();
        String sql = "SELECT b.*, a.patient_name FROM bills b JOIN appointments a ON b.appointment_no = a.appointment_no WHERE b.appointment_no = ? ORDER BY b.generated_date DESC LIMIT 1";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cleanAppNo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Bill bill = new Bill();
                bill.setBillId(rs.getInt("bill_id"));
                bill.setAppointmentNo(rs.getString("appointment_no"));
                bill.setPatientName(rs.getString("patient_name"));
                bill.setConsultationFee(rs.getDouble("consultation_fee"));
                bill.setTreatmentCost(rs.getDouble("treatment_cost"));
                bill.setDiscount(rs.getDouble("discount"));
                bill.setTotalAmount(rs.getDouble("total_amount"));
                bill.setGeneratedDate(rs.getString("generated_date"));
                return bill;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Get all bills
    public List<Bill> getAllBills() {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT b.*, a.patient_name FROM bills b JOIN appointments a ON b.appointment_no = a.appointment_no ORDER BY b.generated_date DESC";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Bill bill = new Bill();
                bill.setBillId(rs.getInt("bill_id"));
                bill.setAppointmentNo(rs.getString("appointment_no"));
                bill.setPatientName(rs.getString("patient_name"));
                bill.setConsultationFee(rs.getDouble("consultation_fee"));
                bill.setTreatmentCost(rs.getDouble("treatment_cost"));
                bill.setDiscount(rs.getDouble("discount"));
                bill.setTotalAmount(rs.getDouble("total_amount"));
                bill.setGeneratedDate(rs.getString("generated_date"));
                list.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}