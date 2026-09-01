package com.dental.servlet;

import com.dental.model.Appointment;
import com.dental.model.Bill;
import com.dental.service.AppointmentService;
import com.dental.service.BillService;
import com.dental.util.SessionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/report")
public class ReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AppointmentService appointmentService;
    private BillService billService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        appointmentService = new AppointmentService();
        billService = new BillService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        if (!SessionManager.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("allAppointments".equals(action)) {
            List<Appointment> appointments = appointmentService.getAllAppointments();
            request.setAttribute("appointments", appointments);
            request.setAttribute("reportTitle", "All Appointments");
            request.getRequestDispatcher("/jsp/reports.jsp").forward(request, response);
        } else if ("allBills".equals(action)) {
            List<Bill> bills = billService.getAllBills();
            request.setAttribute("bills", bills);
            request.setAttribute("reportTitle", "All Bills");
            request.getRequestDispatcher("/jsp/reports.jsp").forward(request, response);
        } else if ("daily".equals(action)) {
            String dateParam = request.getParameter("reportDate");
            if (dateParam != null && !dateParam.isEmpty()) {
                Date reportDate = Date.valueOf(dateParam);
                List<Appointment> appointments = appointmentService.getAppointmentsByDateRange(reportDate, reportDate);
                request.setAttribute("appointments", appointments);
                request.setAttribute("reportTitle", "Daily Report - " + dateParam);
                request.setAttribute("reportDate", dateParam);
            } else {
                request.setAttribute("error", "Please select a date.");
            }
            request.getRequestDispatcher("/jsp/reports.jsp").forward(request, response);
        } else {
            // Default - show summary
            List<Appointment> allAppointments = appointmentService.getAllAppointments();
            List<Bill> allBills = billService.getAllBills();
            request.setAttribute("totalAppointments", allAppointments != null ? allAppointments.size() : 0);
            request.setAttribute("totalBills", allBills != null ? allBills.size() : 0);
            request.getRequestDispatcher("/jsp/reports.jsp").forward(request, response);
        }
    }
}
