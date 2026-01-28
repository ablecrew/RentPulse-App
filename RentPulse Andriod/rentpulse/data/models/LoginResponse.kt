package com.rentpulse.data.models

data class LoginResponse(
    val status: String,
    val message: String?,
    val user_id: Int?,
    val role: String?
) {
    val success: Boolean
        get() = status.equals("success", ignoreCase = true)
}
