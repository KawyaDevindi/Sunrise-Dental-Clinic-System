package com.dental.servlet;

import com.dental.model.Bill;
import com.dental.model.Appointment;
import com.dental.service.BillService;
import com.dental.service.AppointmentService;
import com.dental.util.SessionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/bill")
public class BillServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BillService billService;
    private AppointmentService appointmentService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        billService = new BillService();
        appointmentService = new AppointmentService();
        System.out.println("✅ BillServlet initialized");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        if (!SessionManager.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        System.out.println("🔍 BillServlet - Action: " + action);
        
        if ("generate".equals(action)) {
            String appNo = request.getParameter("appointmentNo");
            System.out.println("🔍 BillServlet - Generating bill for: " + appNo);
            
            if (appNo != null && !appNo.trim().isEmpty()) {
                String cleanAppNo = appNo.trim();
                
                // Check if appointment exists
                System.out.println("🔍 BillServlet - Checking if appointment exists: " + cleanAppNo);
                Appointment app = appointmentService.getAppointmentDetails(cleanAppNo);
                
                if (app == null) {
                    System.err.println("❌ BillServlet - Appointment not found: " + cleanAppNo);
                    request.setAttribute("error", "Appointment not found with number: " + cleanAppNo);
                    request.getRequestDispatcher("/jsp/generateBill.jsp").forward(request, response);
                    return;
                }
                
                System.out.println("✅ BillServlet - Found appointment: " + cleanAppNo);
                System.out.println("   Patient: " + app.getPatientName());
                System.out.println("   Status: " + app.getStatus());
                
                // Check if bill already exists
                Bill existingBill = billService.getBillByAppointmentNo(cleanAppNo);
                if (existingBill != null) {
                    System.out.println("📊 BillServlet - Bill already exists for: " + cleanAppNo);
                    request.setAttribute("bill", existingBill);
                    request.setAttribute("success", "Bill already exists!");
                    request.getRequestDispatcher("/jsp/generateBill.jsp").forward(request, response);
                    return;
                }
                
                // Generate bill
                System.out.println("🔄 BillServlet - Calling billService.generateBill for: " + cleanAppNo);
                Bill bill = billService.generateBill(cleanAppNo);
                
                if (bill != null) {
                    System.out.println("✅ BillServlet - Bill generated successfully for: " + cleanAppNo);
                    System.out.println("   Bill ID: " + bill.getBillId());
                    System.out.println("   Total: " + bill.getTotalAmount());
                    
                    request.setAttribute("bill", bill);
                    request.setAttribute("success", "Bill generated successfully!");
                } else {
                    System.err.println("❌ BillServlet - Failed to generate bill for: " + cleanAppNo);
                    request.setAttribute("error", "Failed to generate bill. Please try again.");
                }
            } else {
                request.setAttribute("error", "Please enter an appointment number.");
            }
            request.getRequestDispatcher("/jsp/generateBill.jsp").forward(request, response);
        } else if ("print".equals(action)) {
            String appNo = request.getParameter("appointmentNo");
            if (appNo != null && !appNo.trim().isEmpty()) {
                Bill bill = billService.getBillByAppointmentNo(appNo.trim());
                if (bill != null) {
                    generateReceipt(response, bill);
                } else {
                    response.setContentType("text/html");
                    response.getWriter().println("<h3>Bill not found!</h3>");
                }
            }
        } else {
            request.getRequestDispatcher("/jsp/generateBill.jsp").forward(request, response);
        }
    }
    
    private void generateReceipt(HttpServletResponse response, Bill bill) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Receipt - Sunrise Dental Clinic</title>");
        out.println("<style>");
        out.println("body { font-family: 'Times New Roman', serif; margin: 50px; background: #f5f5f5; }");
        out.println(".receipt { width: 450px; margin: 0 auto; background: white; border: 2px solid #333; padding: 30px; border-radius: 10px; box-shadow: 0 5px 20px rgba(0,0,0,0.2); }");
        out.println(".header { text-align: center; border-bottom: 2px solid #333; padding-bottom: 15px; }");
        out.println(".header h1 { margin: 0; color: #2c3e50; font-size: 24px; }");
        out.println(".header p { margin: 3px 0; color: #666; font-size: 13px; }");
        out.println(".details { margin: 20px 0; }");
        out.println(".details table { width: 100%; border-collapse: collapse; }");
        out.println(".details td { padding: 8px 5px; }");
        out.println(".details .label { font-weight: bold; color: #34495e; }");
        out.println(".total { border-top: 2px solid #333; padding-top: 15px; margin-top: 15px; }");
        out.println(".total .amount { font-size: 26px; font-weight: bold; color: #27ae60; }");
        out.println(".footer { text-align: center; margin-top: 25px; font-size: 12px; color: #888; border-top: 1px solid #ddd; padding-top: 15px; }");
        out.println(".btn-print { padding: 10px 25px; background: #2c3e50; color: white; border: none; border-radius: 5px; cursor: pointer; font-size: 14px; margin-top: 10px; }");
        out.println(".btn-print:hover { background: #1a252f; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='receipt'>");
        out.println("<div class='header'>");
        out.println("<h1>🦷 Sunrise Dental Clinic</h1>");
        out.println("<p>No. 123, Galle Road, Colombo 03</p>");
        out.println("<p>Tel: +94 11 234 5678 | Email: info@sunrisedental.com</p>");
        out.println("<h3 style='margin-top:10px; color:#2c3e50;'>PATIENT RECEIPT</h3>");
        out.println("</div>");
        out.println("<div class='details'>");
        out.println("<table>");
        out.println("<tr><td class='label'>Bill No:</td><td>#" + bill.getBillId() + "</td></tr>");
        out.println("<tr><td class='label'>Appointment No:</td><td><strong>" + bill.getAppointmentNo() + "</strong></td></tr>");
        out.println("<tr><td class='label'>Patient Name:</td><td>" + bill.getPatientName() + "</td></tr>");
        out.println("<tr><td class='label'>Date:</td><td>" + bill.getGeneratedDate() + "</td></tr>");
        out.println("<tr><td colspan='2' style='padding-top:12px;'><hr></td></tr>");
        out.println("<tr><td class='label'>Consultation Fee:</td><td align='right'>LKR " + String.format("%.2f", bill.getConsultationFee()) + "</td></tr>");
        out.println("<tr><td class='label'>Treatment Cost:</td><td align='right'>LKR " + String.format("%.2f", bill.getTreatmentCost()) + "</td></tr>");
        if (bill.getDiscount() > 0) {
            out.println("<tr><td class='label'>Discount:</td><td align='right'>- LKR " + String.format("%.2f", bill.getDiscount()) + "</td></tr>");
        }
        out.println("</table>");
        out.println("</div>");
        out.println("<div class='total'>");
        out.println("<table>");
        out.println("<tr><td class='label' style='font-size:18px;'>TOTAL AMOUNT:</td>");
        out.println("<td align='right' class='amount'>LKR " + String.format("%.2f", bill.getTotalAmount()) + "</td></tr>");
        out.println("</table>");
        out.println("</div>");
        out.println("<div class='footer'>");
        out.println("<p>Thank you for choosing Sunrise Dental Clinic</p>");
        out.println("<p>Please keep this receipt for your records</p>");
        out.println("<button class='btn-print' onclick='window.print()'>🖨️ Print Receipt</button><br><br>");
        out.println("<a href='javascript:window.close()' style='color:#3498db; text-decoration:none;'>Close Window</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}