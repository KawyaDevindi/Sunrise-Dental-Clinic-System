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
            return null;
        }
        return billDAO.generateBill(appointmentNo);
    }
    
    public Bill getBillByAppointmentNo(String appointmentNo) {
        return billDAO.getBillByAppointmentNo(appointmentNo);
    }
    
    public List<Bill> getAllBills() {
        return billDAO.getAllBills();
    }
    
    public String formatCurrency(double amount) {
        return String.format("LKR %.2f", amount);
    }
}
