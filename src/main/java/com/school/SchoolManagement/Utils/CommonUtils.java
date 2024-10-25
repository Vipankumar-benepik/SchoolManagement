package com.school.SchoolManagement.Utils;

import org.springframework.stereotype.Component;

@Component
public class CommonUtils {
    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);
    }
}
