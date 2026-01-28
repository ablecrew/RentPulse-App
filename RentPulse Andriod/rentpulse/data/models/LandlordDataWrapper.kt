package com.rentpulse.data.models

data class LandlordDataWrapper(
    val count: Int,
    val data: List<LandlordData>,
    val landlord: LandlordData? = null
)