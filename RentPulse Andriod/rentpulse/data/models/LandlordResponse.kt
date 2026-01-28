package com.rentpulse.data.models

data class LandlordResponse(
    val success: Boolean,
    val message: String?,
    val data: LandlordDataWrapper
)
