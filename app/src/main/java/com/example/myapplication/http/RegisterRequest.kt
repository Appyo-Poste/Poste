package com.example.myapplication.http

data class RegisterRequest(
    val email: String,
    val first_name: String,
    val last_name: String,
    val password: String
)
