package com.rentpulse.data.models

data class UploadResponse(
    val success: Boolean,
    val message: String,
    val fileUrls: List<String>? = null, // List of uploaded file URLs
    val url: String = "" // URL of the uploaded media
)
