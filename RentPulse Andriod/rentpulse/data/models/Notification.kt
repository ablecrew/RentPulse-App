package com.rentpulse.data.models

data class Notification(
    val id: Int,
    val title: String,
    val message: String,
    val created_at: String,
    val is_read: Int // 0 = unread, 1 = read
)
