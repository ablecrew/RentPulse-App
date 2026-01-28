package com.rentpulse.navigation

import com.rentpulse.TenantLeaseSubmissionReviewScreen
import com.rentpulse.TenantPaymentReceiptScreen
import com.rentpulse.TenantPaymentScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rentpulse.LandlordAddPropertyScreen
import com.rentpulse.LandlordSettingsScreen
import com.rentpulse.AccountVerificationScreen
import com.rentpulse.BankDetailsScreen
import com.rentpulse.CreateAccountScreen
import com.rentpulse.LandlordDashboarddScreen
import com.rentpulse.LandlordPaymentMethodScreen
import com.rentpulse.LandlordPersonalInfoScreen
import com.rentpulse.LandlordProfileCreationScreen
import com.rentpulse.MastercardDetailsScreen
import com.rentpulse.MpesaDetailsScreen
import com.rentpulse.MultipleBankAccountsScreen
import com.rentpulse.NotificationScreen
import com.rentpulse.PasswordResetScreen
import com.rentpulse.PasswordResetSuccessScreen
import com.rentpulse.PayPalDetailsScreen
import com.rentpulse.PaymentProcessingFeesScreen
import com.rentpulse.RolesScreen
import com.rentpulse.SignInScreen
import com.rentpulse.SplashScreen
import com.rentpulse.TenantDashboardScreen
import com.rentpulse.TenantFindHomeScreen
import com.rentpulse.TenantProfileCreationScreen
import com.rentpulse.WelcomeScreen
import com.rentpulse.ui.screens.AboutRentPulseScreen
import com.rentpulse.ui.screens.HelpCenterScreen
import com.rentpulse.LandlordPaymentInfoScreen
import com.rentpulse.LandlordPropertiesScreen
import com.rentpulse.LandlordSupportScreen
import com.rentpulse.ui.screens.PrivacyPolicyScreen
import com.rentpulse.ui.screens.TenantLeaseDetailsScreen
import com.rentpulse.ui.screens.TenantLeaseVerificationScreen
import com.rentpulse.TenantLeaseVerificationStatusScreen
import com.rentpulse.ui.screens.TenantPaymentDashboardScreen
import com.rentpulse.ui.screens.TenantPaymentHistoryScreen
import com.rentpulse.TenantSettingsScreen
import com.rentpulse.ui.screens.LandlordInvitesScreen
import com.rentpulse.ui.screens.TermsOfServiceScreen
import com.rentpulse.ui.theme.LandlordPropertyInfoScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.SplashScreen.route,
        //modifier = Modifier.padding(innerPadding),
    ) {
        composable(Screens.SplashScreen.route) {
            SplashScreen(navController)
        }

        composable(Screens.WelcomeScreen.route) {
            WelcomeScreen(navController)
        }

        composable(Screens.AboutRentPulseScreen.route) {
            AboutRentPulseScreen(navController)
        }

        composable(Screens.CreateAccountScreen.route) {
            CreateAccountScreen(navController)
        }

        composable(Screens.SignInScreen.route) {
            SignInScreen(navController, onLoginSuccess = {})
        }

        composable(
            route = Screens.AccountVerificationScreen.routeWithArg,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            AccountVerificationScreen(navController, email)
        }

        composable(Screens.PasswordResetScreen.route) {
            PasswordResetScreen(navController)
        }

        composable(Screens.PasswordResetSuccessScreen.route) {
            PasswordResetSuccessScreen(navController)
        }

        composable(Screens.TenantDashboardScreen.route) {
            TenantDashboardScreen(navController = navController,)
        }

        composable(Screens.RolesScreen.route) {
            RolesScreen(navController)
        }

        composable(Screens.LandlordProfileCreationScreen.route) {
            LandlordProfileCreationScreen(navController)
        }

        composable(Screens.TenantProfileCreationScreen.route) {
            TenantProfileCreationScreen(navController)
        }

        composable(Screens.LandlordPersonalInfoScreen.route) {
            LandlordPersonalInfoScreen(navController)
        }

        composable(Screens.LandlordPropertyInfoScreen.route) {
            LandlordPropertyInfoScreen(navController)
        }

        composable(Screens.LandlordPaymentInfoScreen.route) {
            LandlordPaymentInfoScreen(navController)
        }

        composable(Screens.HelpCenterScreen.route) {
            HelpCenterScreen(navController)
        }

        composable(Screens.TermsOfServiceScreen.route) {
            TermsOfServiceScreen(navController)
        }

        composable(Screens.PrivacyPolicyScreen.route) {
            PrivacyPolicyScreen(navController)
        }


        composable(Screens.LandlordDashboardd.route) {
            LandlordDashboarddScreen(navController)
        }

        composable(Screens.LandlordPaymentMethodScreen.route) {
            LandlordPaymentMethodScreen(navController)
        }

        composable(Screens.TenantLeaseDetailsScreen.route) {
            TenantLeaseDetailsScreen(navController)
        }

        composable(Screens.TenantLeaseSubmissionReviewScreen.route) {
            TenantLeaseSubmissionReviewScreen(navController)
        }

        composable(Screens.TenantPaymentDashboardScreen.route) {
            TenantPaymentDashboardScreen(navController)
        }

        composable(Screens.TenantSettingsScreen.route) {
            TenantSettingsScreen(navController)
        }

        composable(Screens.TenantPaymentHistoryScreen.route) {
            TenantPaymentHistoryScreen(navController)
        }

        composable(Screens.TenantLeaseVerificationScreen.route) {
            TenantLeaseVerificationScreen(navController)
        }

        composable(Screens.TenantLeaseVerificationStatusScreen.route) {
            TenantLeaseVerificationStatusScreen(navController)
        }

        composable(Screens.TenantPaymentScreen.route) {
            TenantPaymentScreen(navController)
        }

        composable(Screens.TenantPaymentReceiptScreen.route) {
            TenantPaymentReceiptScreen(navController)
        }

        composable(Screens.LandlordPropertiesScreen.route) {
            LandlordPropertiesScreen(navController)
        }

        composable(Screens.LandlordAddPropertyScreen.route) {
            LandlordAddPropertyScreen(navController)
        }

        composable(Screens.LandlordSettingsScreen.route) {
            val isDarkTheme = remember { mutableStateOf(false) } // or true if you want dark by default
            LandlordSettingsScreen(
                navController = navController,
                isDarkTheme = isDarkTheme
            )
        }


        composable(Screens.LandlordSupportScreen.route) {
            LandlordSupportScreen(navController)
        }

        composable(Screens.TenantFindHomeScreen.route) {
            TenantFindHomeScreen(navController)
        }

        composable(Screens.NotificationScreen.route) {
            NotificationScreen(navController)
        }

        composable(Screens.MultipleBankAccountsScreen.route) {
            MultipleBankAccountsScreen(navController)
        }

        composable(Screens.PaymentProcessingFeesScreen.route) {
            PaymentProcessingFeesScreen(navController)
        }

        composable(Screens.MpesaDetailsScreen.route) {
            MpesaDetailsScreen(navController)
        }

        composable(Screens.MastercardDetailsScreen.route) {
            MastercardDetailsScreen(navController)
        }

        composable(Screens.PayPalDetailsScreen.route) {
            PayPalDetailsScreen(navController)
        }

        composable(Screens.BankDetailsScreen.route) {
            BankDetailsScreen(navController)
        }

        composable(Screens.LandlordInvitesScreen.route) {
            LandlordInvitesScreen(navController)
        }
    }
}
