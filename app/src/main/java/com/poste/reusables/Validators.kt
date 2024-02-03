package com.poste.reusables

fun validateEmail(email: String): String? {
    return if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        null
    } else {
        "Invalid email"
    }
}

fun validateName(name: String): String? {
    return if (name.length >= 2) {
        null
    } else {
        "Name must be at least 2 characters"
    }
}

fun validatePassword(password: String): String? {
    return if (password.length < 8) {
        "Password must be at least 8 characters"
    } else if (password.length > 32) {
        "Password must be less than 32 characters"
    } else if (!password.contains(Regex("[0-9]"))) {
        "Password must contain a number"
    } else if (!password.contains(Regex("[A-Z]"))) {
        "Password must contain an uppercase letter"
    } else if (!password.contains(Regex("[a-z]"))) {
        "Password must contain a lowercase letter"
    } else {
        null
    }
}

fun validateConfirmPassword(password: String, confirmPassword: String): String? {
    return if (password == confirmPassword && password.isNotEmpty()) {
        null
    } else {
        "Passwords do not match"
    }
}