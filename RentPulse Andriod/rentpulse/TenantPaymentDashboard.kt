package com.rentpulse.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.R
import com.rentpulse.navigation.Screens

val montserratFontFamily = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_bold, FontWeight.Bold)
)

@Composable
fun TenantPaymentDashboardScreen(navController: NavController) {
    val gradient = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF007BFF),
            0.5f to Color(0xFF007BFF),
            0.55f to Color(0xFFBFDFFF),
            1.0f to Color(0xFFF5F5F5)
        )
    )

    // These would normally come from ViewModel
    val totalPaid by remember { mutableStateOf("Ksh102,000") }
    val outstanding by remember { mutableStateOf("Ksh.26,000") }
    val nextPayment by remember { mutableStateOf("Ksh.13,000") }

    Scaffold(
        bottomBar = {
            // Pass the current screen route here so the tab highlights correctly
            TDBottomNavigationBar(
                navController = navController,
                currentScreen = Screens.TenantPaymentDashboardScreen.route
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(padding)
        ) {
            Column {
                // Top Row: Back + Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Payment Status",
                        fontFamily = montserratFontFamily,
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Main White Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Row with Total Paid + Outstanding Balance
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            PaymentInfoCard("Total Paid", totalPaid, "+10%", Color(0xFF4CAF50))
                            PaymentInfoCard("Outstanding Balance", outstanding, "-5%", Color(0xFFF44336))
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        PaymentInfoCard("Next Payment Due", nextPayment, "+2%", Color(0xFF4CAF50))

                        Spacer(modifier = Modifier.height(20.dp))

                        PaymentActionButton("Payment History") { navController.navigate(Screens.TenantPaymentHistoryScreen.route) }
                        PaymentActionButton("Make Payment") { navController.navigate(Screens.TenantPaymentScreen.route) }
                        PaymentActionButton("Payment Receipt") { navController.navigate(Screens.TenantPaymentReceiptScreen.route) }
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentInfoCard(title: String, amount: String, percent: String, percentColor: Color, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontFamily = montserratFontFamily, color = Color.Gray, fontSize = 14.sp)
            Text(text = amount, fontFamily = montserratFontFamily, fontSize = 20.sp, color = Color.Black)
            Text(text = percent, fontFamily = montserratFontFamily, fontSize = 12.sp, color = percentColor)
        }
    }
}

@Composable
fun PaymentActionButton(text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        border = ButtonDefaults.outlinedButtonBorder
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = text, fontFamily = montserratFontFamily, color = Color.Black)
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
}

@Composable
fun navColor(isSelected: Boolean): Color {
    val targetColor = if (isSelected) Color(0xFF007BFF) else Color.Black
    val animatedColor by animateColorAsState(targetValue = targetColor)
    return animatedColor
}

@Composable
fun TDBottomNavigationBar(navController: NavController, currentScreen: String) {
    NavigationBar(containerColor = Color.White) {

        // Home
        val isHomeSelected = currentScreen == Screens.TenantDashboardScreen.route
        NavigationBarItem(
            selected = isHomeSelected,
            onClick = {
                navController.navigate(Screens.TenantDashboardScreen.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.home_ic),
                    contentDescription = "Home",
                    modifier = Modifier.size(28.dp),
                    tint = navColor(isHomeSelected)
                )
            },
            label = {
                Text(
                    text = "Home",
                    fontFamily = montserratFontFamily,
                    color = navColor(isHomeSelected)
                )
            }
        )

        // Settings
        val isSettingsSelected = currentScreen == Screens.TenantSettingsScreen.route
        NavigationBarItem(
            selected = isSettingsSelected,
            onClick = {
                navController.navigate(Screens.TenantSettingsScreen.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.settings_ic),
                    contentDescription = "Settings",
                    modifier = Modifier.size(24.dp),
                    tint = navColor(isSettingsSelected)
                )
            },
            label = {
                Text(
                    text = "Settings",
                    fontFamily = montserratFontFamily,
                    color = navColor(isSettingsSelected)
                )
            }
        )

        // Lease
        val isLeaseSelected = currentScreen == Screens.TenantLeaseDetailsScreen.route
        NavigationBarItem(
            selected = isLeaseSelected,
            onClick = {
                navController.navigate(Screens.TenantLeaseDetailsScreen.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.lease_ic),
                    contentDescription = "Lease",
                    modifier = Modifier.size(26.dp),
                    tint = navColor(isLeaseSelected)
                )
            },
            label = {
                Text(
                    text = "Lease",
                    fontFamily = montserratFontFamily,
                    color = navColor(isLeaseSelected)
                )
            }
        )

        // Payments
        val isPaymentsSelected = currentScreen == Screens.TenantPaymentDashboardScreen.route
        NavigationBarItem(
            selected = isPaymentsSelected,
            onClick = {
                navController.navigate(Screens.TenantPaymentDashboardScreen.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.payment_ic),
                    contentDescription = "Payments",
                    modifier = Modifier.size(28.dp),
                    tint = navColor(isPaymentsSelected)
                )
            },
            label = {
                Text(
                    text = "Payments",
                    fontFamily = montserratFontFamily,
                    color = navColor(isPaymentsSelected)
                )
            }
        )
    }
}

data class BottomNavItem(val title: String, val icon: Int, val route: String)

@Preview(showSystemUi = true)
@Composable
fun PreviewTenantPaymentDashboardScreen() {
    val navController = rememberNavController()
    TenantPaymentDashboardScreen(navController)
}
