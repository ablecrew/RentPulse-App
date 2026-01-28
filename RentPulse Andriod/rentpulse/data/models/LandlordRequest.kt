package com.rentpulse.data.models

import com.google.gson.annotations.SerializedName

data class LandlordRequest(
    @SerializedName("user_id") val user_id: Int,            // link landlord to user
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("id_number") val id_number: String,
    @SerializedName("profile_image") val profile_image: String?,
    @SerializedName("allow_updates") val allow_updates: Boolean,
    @SerializedName("email") val email: String? = null
)
