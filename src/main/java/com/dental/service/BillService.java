package com.dental.service;

import com.dental.dao.BillDAO;
import com.dental.model.Bill;
import java.util.List;

public class BillService {
    private BillDAO billDAO;
    
    public BillService() {
        this.billDAO = new BillDAO();
    }
    
    public Bill generateBill(String appointmentNo) {
        if (appointmentNo == null || appointmentNo.trim().isEmpty()) {
            System.err.println("❌ BillService: Appointment number is null or empty");
            return null;
        }
        
        String cleanAppNo = appointmentNo.trim();
        System.out.println("🔍 BillService - Generating bill for: " + cleanAppNo);
        
        // Try stored procedure first
        Bill bill = billDAO.generateBill(cleanAppNo);
        
        // If stored procedure fails, try direct SQL
        if (bill == null) {
            System.out.println("🔄 BillService - Stored procedure failed, trying direct SQL...");
            bill = billDAO.generateBillDirectSQL(cleanAppNo);
        }
        
        if (bill == null) {
            System.err.println("❌ BillService - Failed to generate bill for: " + cleanAppNo);
        } else {
            System.out.println("✅ BillService - Bill generated: " + cleanAppNo);
            System.out.println("   Bill ID: " + bill.getBillId());
            System.out.println("   Total: " + bill.getTotalAmount());
        }
        
        return bill;
    }
    
    public Bill getBillByAppointmentNo(String appointmentNo) {
        if (appointmentNo == null || appointmentNo.trim().isEmpty()) {
            return null;
        }
        return billDAO.getBillByAppointmentNo(appointmentNo.trim());
    }
    
    public List<Bill> getAllBills() {
        return billDAO.getAllBills();
    }
    
    public String formatCurrency(double amount) {
        return String.format("LKR %.2f", amount);
    }
}