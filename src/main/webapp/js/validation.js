/**
 * SUNRISE DENTAL CLINIC - Client Side Validation
 */

// Validate Appointment Registration Form
function validateAppointmentForm() {
    var isValid = true;
    
    // Patient Name
    var name = document.getElementById('patientName').value.trim();
    var nameError = document.getElementById('nameError');
    if (name.length < 2) {
        nameError.textContent = 'Please enter a valid patient name (minimum 2 characters).';
        isValid = false;
    } else if (!/^[A-Za-z\s.']+$/.test(name)) {
        nameError.textContent = 'Name should only contain letters and spaces.';
        isValid = false;
    } else {
        nameError.textContent = '';
    }
    
    // Contact Number (Sri Lankan format)
    var phone = document.getElementById('contactNo').value.trim();
    var phoneError = document.getElementById('phoneError');
    var phonePattern = /^(\+94|0)[0-9]{9,10}$/;
    if (!phonePattern.test(phone)) {
        phoneError.textContent = 'Enter a valid Sri Lankan number (e.g., +94771234567 or 0771234567).';
        isValid = false;
    } else {
        phoneError.textContent = '';
    }
    
    // Email (optional but validate if provided)
    var email = document.getElementById('email').value.trim();
    var emailError = document.getElementById('emailError');
    if (email.length > 0) {
        var emailPattern = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
        if (!emailPattern.test(email)) {
            emailError.textContent = 'Please enter a valid email address.';
            isValid = false;
        } else {
            emailError.textContent = '';
        }
    } else {
        emailError.textContent = '';
    }
    
    // Address
    var address = document.getElementById('address').value.trim();
    var addressError = document.getElementById('addressError');
    if (address.length < 5) {
        addressError.textContent = 'Please enter a complete address (minimum 5 characters).';
        isValid = false;
    } else {
        addressError.textContent = '';
    }
    
    // Date (must be today or future)
    var date = document.getElementById('appointmentDate').value;
    var dateError = document.getElementById('dateError');
    if (date) {
        var today = new Date().toISOString().split('T')[0];
        if (date < today) {
            dateError.textContent = 'Appointment date must be today or in the future.';
            isValid = false;
        } else {
            dateError.textContent = '';
        }
    }
    
    return isValid;
}

// Validate Login Form
function validateLoginForm() {
    var username = document.getElementById('username').value.trim();
    var password = document.getElementById('password').value.trim();
    
    if (username.length < 2) {
        alert('Please enter your username.');
        return false;
    }
    if (password.length < 3) {
        alert('Please enter your password.');
        return false;
    }
    return true;
}

// Auto-hide messages after 5 seconds
document.addEventListener('DOMContentLoaded', function() {
    var messages = document.querySelectorAll('.success-msg, .error-msg');
    messages.forEach(function(msg) {
        setTimeout(function() {
            msg.style.transition = 'opacity 1s';
            msg.style.opacity = '0';
            setTimeout(function() {
                msg.style.display = 'none';
            }, 1000);
        }, 5000);
    });
});