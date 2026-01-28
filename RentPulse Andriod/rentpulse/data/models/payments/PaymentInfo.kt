data class PaymentInfo(
    val method: String?,
    val account_details: String?,
    val multiple_accounts: String?,
    val tenant_fees: String?,
    val allow_updates: Boolean?,
    val landlord_id: String? = null,
    val success: Boolean? = null,
    val data: PaymentInfo? = null
)
