package com.rentpulse.data.models.payments

data class PaymentRequest(
    val landlord_id: Int? = null,
    val method: String,
    val account_details: String?,
    val multiple_accounts: Boolean = false,
    val tenant_fees: Double = 0.0,
    val allow_updates: Boolean = false
)
