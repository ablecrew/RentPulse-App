package com.rentpulse.data.models

data class NotificationData(
    val id: Int,
    val title: String,
    val message: String,
    val is_read: Int,
    val created_at: String
)
