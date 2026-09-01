package com.dental.util;

import java.sql.Date;
import java.util.regex.Pattern;

public class ValidationUtil {
    
    // Phone number validation - Sri Lankan format
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(\\+94|0)[0-9]{9,10}$");
    
    // Email validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    // Name validation - letters, spaces, and some special characters
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z\\s.']+$");
    
    public boolean isValidPhoneNumber(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }
    
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return true; // Email is optional
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    public boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && NAME_PATTERN.matcher(name.trim()).matches();
    }
    
    public boolean isValidDate(Date date) {
        return date != null && date.after(new Date(System.currentTimeMillis()));
    }
    
    public boolean isValidAppointmentNumber(String appNo) {
        return appNo != null && appNo.matches("^APP-\\d{8}-\\d{4}$");
    }
    
    public boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty() && address.trim().length() >= 5;
    }
}
