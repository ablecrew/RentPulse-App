package com.rentpulse.data.models

data class LocationResponse(
    val success: Boolean,
    val message: String,
    val data: Map<String, List<String>> = emptyMap(), // County → List of towns/estates
    val locations: List<String> = emptyList()
)

