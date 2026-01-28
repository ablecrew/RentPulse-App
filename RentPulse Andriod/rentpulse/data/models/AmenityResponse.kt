package com.rentpulse.data.models

data class AmenityResponse(
    val success: Boolean,
    val message: String,
    val data: List<String>, // List of amenities
    val amenities: List<String> = emptyList(),
)
