package com.rentpulse.data.models

data class PropertyRequest(
    val property_name: String,
    val location: String,
    val property_type: String,
    val units_total: Int,
    val amenities: String = "",   // List of amenities
    val rent_amount: String = "",                // Rent amount as string (with currency if needed)
    val media_urls: List<String> = emptyList()   // URLs of uploaded images/videos
)
