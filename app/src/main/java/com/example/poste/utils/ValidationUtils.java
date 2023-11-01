package com.example.poste.utils;

import android.content.Context;
import android.widget.Toast;

import org.apache.commons.validator.routines.EmailValidator;

public class ValidationUtils {

    /**
     * Validate names are valid
     * Ensures first and last name are not empty and are at most 32 characters
     * @param context the activity within which this is called
     * @param firstName String variable containing the first name
     * @param lastName String variable containing the last name
     * @return boolean. True if names are valid, false otherwise
     */
    public static boolean validateNames(Context context, String firstName, String lastName) {
        if (firstName.isEmpty()) {
            Toast.makeText(context, "Please enter your first name!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (lastName.isEmpty()) {
            Toast.makeText(context, "Please enter your last name!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (firstName.length() > 32) {
            Toast.makeText(context, "First name must be at most 32 characters!", Toast.LENGTH_SHORT).show();
            return true;
        } else if (lastName.length() > 32) {
            Toast.makeText(context, "Last name must be at most 32 characters!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return true;
    }

    /**
     * Validate email is valid
     * @param context the activity within which this is called
     * @param email String variable containing the email
     * @return boolean. True if email is valid, false otherwise
     */
    public static boolean validateEmail(Context context, String email) {
        if (EmailValidator.getInstance().isValid(email)){
            return true;
        } else {
            Toast.makeText(context, "Please enter a valid email!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * Validate password matches and is valid
     * @param context the activity within which this is called
     * @param password String variable containing the password
     * @param confirmPassword String variable containing the password confirmation
     * @return boolean. True if password is valid, false otherwise
     */
    public static boolean validatePassword(Context context, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return validatePassword(context, password);
    }

    /**
     * Validation method for password
     * @param context the activity within which this is called
     * @param password String variable containing the password
     * @return boolean. True if password is valid, false otherwise
     */
    public static boolean validatePassword(Context context, String password) {
        if (password.length() < 8) {
            Toast.makeText(context, "Password must be at least 8 characters!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() > 32) {
            Toast.makeText(context, "Password must be at most 32 characters!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}

