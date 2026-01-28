package com.rentpulse.data.models

data class TenantData(
    val id: Int = 0,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val id_number: String? = null,
    val house_number: String? = null,
    val profile_image: String? = null,
    val rent_status: String? = null,
    val water_status: String? = null,
    val electricity_status: String? = null,
    val allow_updates: Boolean? = false
)
