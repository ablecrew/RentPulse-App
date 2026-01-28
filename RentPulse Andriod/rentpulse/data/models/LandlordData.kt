package com.rentpulse.data.models

data class LandlordData(
    val user_id: Int,
    val name: String,
    val phone: String,
    val email: String?,
    val id_number: String,
    val profile_image: String?,
    val properties_count: Int = 0,          // Total properties owned
    val tenants_count: Int = 0,             // Total tenants under landlord
    val total_payments: Double = 0.0,       // Sum of all payments received
    val pending_payments: Double = 0.0,     // Unpaid or overdue amounts
    val maintenance_requests: Int = 0,      // Active maintenance requests
    val upcoming_rent_due: Int = 0,         // Tenants with rent due soon
    val properties: List<String>? = null,   // List of property names or IDs
    val tenants: List<String>? = null,      // List of tenant names or IDs
    val total_earnings: Double = 0.0,        // Overall landlord earnings
    val allow_updates: Boolean? = false
)
