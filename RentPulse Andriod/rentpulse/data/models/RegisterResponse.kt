package com.rentpulse.data.models

data class RegisterResponse(
    val status: String,
    val message: String,
    val role: String? = null,
    val userId: Int? = null   // 🔹 Added userId property
)