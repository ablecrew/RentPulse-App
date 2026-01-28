package com.rentpulse.navigation

sealed class Screens(
    val route: String,
) {
    object SplashScreen : Screens("Splash_screen")

    object WelcomeScreen : Screens("Welcome_screen")

    object AboutRentPulseScreen : Screens("AboutRentPulse_screen")

    object CreateAccountScreen : Screens("CreateAccount_screen")

    object AccountVerificationScreen : Screens("AccountVerification_screen/{email}") {
        fun createRoute(email: String) = "AccountVerification_screen/$email"
        const val routeWithArg = "AccountVerification_screen/{email}"
    }

    object SignInScreen : Screens("SignIn_screen")

    object PasswordResetScreen : Screens("PasswordReset_Screen")

    object TenantProfileCreationScreen : Screens("TenantProfileCreation_screen")

    object TenantDashboardScreen {
        const val route = "tenant_dashboard/{fullName}/{phone}/{idNumber}/{profileImage}/{allowUpdates}"

        fun createRoute(
            fullName: String,
            phone: String,
            idNumber: String,
            profileImage: String,
            allowUpdates: Boolean
        ): String {
            return "tenant_dashboard/$fullName/$phone/$idNumber/$profileImage/$allowUpdates"
        }
    }


    object RolesScreen : Screens("Roles_screen")

    object PasswordResetSuccessScreen : Screens("PasswordResetSuccess_screen")

    object LandlordProfileCreationScreen : Screens("LandlordProfileCreation_screen")

    object LandlordPersonalInfoScreen : Screens("LandlordPersonalInfo_screen")

    object LandlordPropertyInfoScreen : Screens("LandlordPropertyInfo_screen")

    object LandlordPaymentInfoScreen : Screens("LandlordPaymentInfo_screen")

    object HelpCenterScreen : Screens("HelpCenter_screen")

    object TermsOfServiceScreen : Screens("TermsOfService_screen")

    object PrivacyPolicyScreen : Screens("PrivacyPolicy_screen")

    object LandlordDashboardd : Screens("LandlordDashboardd_screen")

    object LandlordPaymentMethodScreen : Screens("LandlordPaymentMethod_screen")

    object TenantLeaseDetailsScreen : Screens("TenantLeaseDetails_screen")

    object TenantLeaseSubmissionReviewScreen : Screens("TenantLeaseSubmissionReview_screen")

    object TenantPaymentDashboardScreen : Screens("TenantPaymentDashboard_screen")

    object TenantSettingsScreen : Screens("TenantSettings_screen")

    object TenantPaymentHistoryScreen : Screens("tenant_payment_history_screen")

    object TenantLeaseVerificationScreen : Screens("TenantLeaseVerification_screen")

    object TenantLeaseVerificationStatusScreen : Screens("TenantLeaseVerificationStatus_screen")

    object TenantPaymentScreen : Screens("TenantPayment_screen")

    object TenantPaymentReceiptScreen : Screens("TenantPaymentReceipt_screen")

    object LandlordPropertiesScreen : Screens("LandlordProperties_screen")

    object LandlordAddPropertyScreen : Screens("LandlordAddProperty_screen")

    object LandlordSettingsScreen : Screens("LandlordSettings_screen")

    object LandlordSupportScreen : Screens("LandlordSupport_screen")

    object NotificationScreen : Screens ("Notification_screen")

    object TenantFindHomeScreen : Screens ("TenantFindHome_screen")

    object MultipleBankAccountsScreen : Screens("MultipleBankAccounts_screen")

    object PaymentProcessingFeesScreen : Screens("PaymentProcessingFees_screen")

    object MpesaDetailsScreen : Screens("MpesaDetails_screen")

    object MastercardDetailsScreen : Screens("MastercardDetails_screen")

    object PayPalDetailsScreen : Screens("PayPalDetails_screen")

    object BankDetailsScreen : Screens("BankDetails_screen")

    object LandlordInvitesScreen : Screens("LandlordInvites_screen")
}
