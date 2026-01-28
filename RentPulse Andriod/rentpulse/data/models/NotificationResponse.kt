package com.rentpulse.data.models


data class NotificationResponse(
    val success: Boolean,
    val unread_count: Int,
    val message: String,
    val data: List<com.rentpulse.data.models.Notification> // row JSON array
)