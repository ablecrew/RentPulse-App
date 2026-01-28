package com.rentpulse.data.models

data class TokenRequest(
    val user_id: Int,   // pass the logged-in user's ID
    val fcm_token: String
)