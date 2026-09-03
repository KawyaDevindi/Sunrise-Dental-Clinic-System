package com.dental.dao;

import com.dental.model.Bill;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {
    
    // Generate bill using stored procedure - FIXED VERSION
    public Bill generateBill(String appointmentNo) {
        if (appointmentNo == null || appointmentNo.trim().isEmpty()) {
            System.err.println("❌ BillDAO: Appointment number is null or empty");
            return null;
        }
        
        String cleanAppNo = appointmentNo.trim();
        System.out.println("🔍 BillDAO - Generating bill for: " + cleanAppNo);
        
        String sql = "{CALL GenerateBill(?)}";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setString(1, cleanAppNo);
            System.out.println("🔄 Executing stored procedure for: " + cleanAppNo);
            
            boolean hasResults = cstmt.execute();
            System.out.println("📊 Stored procedure executed, hasResults: " + hasResults);
            
            if (hasResults) {
                ResultSet rs = cstmt.getResultSet();
                System.out.println("📊 ResultSet available: " + (rs != null));
                
                if (rs != null && rs.next()) {
                    Bill bill = new Bill();
                    
                    // Try to get all columns safely
                    try {
                        bill.setBillId(rs.getInt("bill_id"));
                    } catch (SQLException e) {
                        System.out.println("⚠️ Column 'bill_id' not found");
                    }
                    
                    try {
                        bill.setAppointmentNo(rs.getString("appointment_no"));
                    } catch (SQLException e) {
                        System.out.println("⚠️ Column 'appointment_no' not found");
                    }
                    
                    try {
                        bill.setPatientName(rs.getString("patient_name"));
                    } catch (SQLException e) {
                        System.out.println("⚠️ Column 'patient_name' not found");
                    }
                    
                    try {
                        bill.setConsultationFee(rs.getDouble("consultation_fee"));
                    } catch (SQLException e) {
                        System.out.println("⚠️ Column 'consultation_fee' not found");
                    }
                    
                    try {
                        bill.setTreatmentCost(rs.getDouble("treatment_cost"));
                    } catch (SQLException e) {
                        System.out.println("⚠️ Column 'treatment_cost' not found");
                    }
                    
                    try {
                        bill.setDiscount(rs.getDouble("discount"));
                    } catch (SQLException e) {
                        System.out.println("⚠️ Column 'discount' not found");
                    }
                    
                    try {
                        bill.setTotalAmount(rs.getDouble("total_amount"));
                    } catch (SQLException e) {
                        System.out.println("⚠️ Column 'total_amount' not found");
                    }
                    
                    try {
                        bill.setGeneratedDate(rs.getString("generated_date"));
                    } catch (SQLException e) {
                        System.out.println("⚠️ Column 'generated_date' not found");
                    }
                    
                    System.out.println("✅ BillDAO - Bill generated successfully for: " + cleanAppNo);
                    System.out.println("   Bill ID: " + bill.getBillId());
                    System.out.println("   Total: " + bill.getTotalAmount());
                    
                    return bill;
                } else {
                    System.out.println("❌ BillDAO - No data returned from stored procedure");
                }
            } else {
                System.out.println("❌ BillDAO - Stored procedure returned no results");
                // Check for output parameters or messages
                int updateCount = cstmt.getUpdateCount();
                System.out.println("📊 Update count: " + updateCount);
            }
        } catch (SQLException e) {
            System.err.println("❌ BillDAO - SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Alternative method - Direct SQL (Fallback)
    public Bill generateBillDirectSQL(String appointmentNo) {
        if (appointmentNo == null || appointmentNo.trim().isEmpty()) {
            return null;
        }
        
        String cleanAppNo = appointmentNo.trim();
        System.out.println("🔍 BillDAO - Using direct SQL for: " + cleanAppNo);
        
        // First check if bill already exists
        String checkSql = "SELECT b.*, a.patient_name FROM bills b JOIN appointments a ON b.appointment_no = a.appointment_no WHERE b.appointment_no = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
            
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
                System.out.println("✅ BillDAO - Found existing bill: " + cleanAppNo);
                return bill;
            }
        } catch (SQLException e) {
            System.err.println("❌ BillDAO - Check bill error: " + e.getMessage());
        }
        
        // If no bill exists, create one using direct SQL
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            // Get costs
            String costSql = "SELECT IFNULL(d.consultation_fee, 500) AS consult_fee, IFNULL(t.treatment_cost, 0) AS treat_cost " +
                             "FROM appointments a " +
                             "LEFT JOIN dentists d ON a.dentist_id = d.dentist_id " +
                             "LEFT JOIN treatments t ON a.treatment_id = t.treatment_id " +
                             "WHERE a.appointment_no = ?";
            
            double consultFee = 500;
            double treatCost = 0;
            
            try (PreparedStatement pstmt = conn.prepareStatement(costSql)) {
                pstmt.setString(1, cleanAppNo);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    consultFee = rs.getDouble("consult_fee");
                    treatCost = rs.getDouble("treat_cost");
                }
            }
            
            double total = consultFee + treatCost;
            
            // Insert bill
            String insertSql = "INSERT INTO bills (appointment_no, consultation_fee, treatment_cost, total_amount) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, cleanAppNo);
                pstmt.setDouble(2, consultFee);
                pstmt.setDouble(3, treatCost);
                pstmt.setDouble(4, total);
                
                int affected = pstmt.executeUpdate();
                System.out.println("📊 Bill insert affected rows: " + affected);
                
                if (affected > 0) {
                    // Update appointment total cost
                    String updateSql = "UPDATE appointments SET total_cost = ? WHERE appointment_no = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setDouble(1, total);
                        updateStmt.setString(2, cleanAppNo);
                        updateStmt.executeUpdate();
                    }
                    
                    // Get the new bill
                    return getBillByAppointmentNo(cleanAppNo);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ BillDAO - Direct SQL error: " + e.getMessage());
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