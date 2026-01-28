package com.rentpulse.data.models

data class ApiResponse(
    val success: Boolean,
    val message: String,
    val otp: String? = null,
    val userId: String? = null,
    val token: String? = null,
    val role: String? = null,
    val error: String? = null
) {
    /** Member property to check if API call was successful */
    val isSuccessful: Boolean
        get() = success

    /** Member function to return the body of the response */
    fun body(): ApiResponse = this
}
