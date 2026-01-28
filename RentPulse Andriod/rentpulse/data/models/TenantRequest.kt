package com.rentpulse.data.models

import com.google.gson.annotations.SerializedName

data class TenantRequest(
    @SerializedName("user_id") val user_id: Int? = null,           // optional now
    @SerializedName("full_name") val full_name: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("id_number") val id_number: String,
    @SerializedName("profile_image") val profile_image: String = "",
    @SerializedName("allow_updates") val allow_updates: Boolean = true
)
