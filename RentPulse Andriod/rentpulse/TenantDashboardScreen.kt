package com.rentpulse

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.ui.theme.components.ProfileImage
import com.rentpulse.data.models.TenantData
import com.rentpulse.navigation.Screens
import com.rentpulse.ui.screens.TDBottomNavigationBar
import com.rentpulse.ui.theme.RentPulseTheme
import com.rentpulse.utils.AuthPreferences
import com.rentpulse.utils.TenantPreferences
import kotlinx.coroutines.launch

@Composable
fun TenantDashboardScreen(
    navController: NavController,
) {
    var tenantData by remember { mutableStateOf<TenantData?>(null) }
    var loading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Notification badge count
    var notificationsCount by remember { mutableStateOf(0) }

    // Load locally saved tenant details
    LaunchedEffect(Unit) {
        tenantData = TenantPreferences.readTenantDetails(context)
        loading = tenantData == null
    }

    // Fetch tenant data and unread notifications
    LaunchedEffect(Unit) {
        val token = AuthPreferences.readAuthToken(context)
        if (!token.isNullOrEmpty()) {
            coroutineScope.launch {
                try {
                    // Tenant info
                    val tenantResponse = RetrofitClient.api.getTenant("Bearer $token")
                    if (tenantResponse.success && tenantResponse.data != null) {
                        tenantData = tenantResponse.data.data
                        TenantPreferences.saveTenantDetails(
                            context,
                            fullName = tenantData?.name ?: "",
                            phone = tenantData?.phone ?: "",
                            idNumber = tenantData?.id_number ?: "",
                            profileImage = tenantData?.profile_image ?: "",
                            allowUpdates = tenantData?.allow_updates ?: true
                        )
                    } else {
                        Log.e("TenantDashboard", "Failed to fetch tenant data: $tenantResponse")
                    }

                    // Unread notifications
                    val notifResponse = RetrofitClient.api.getNotifications("Bearer $token")
                    if (notifResponse.success) {
                        notificationsCount = notifResponse.unread_count
                    } else {
                        Log.e("TenantDashboard", "Failed to fetch notifications: ${notifResponse.message}")
                    }

                } catch (e: Exception) {
                    Log.e("TenantDashboard", "API call error", e)
                } finally {
                    loading = false
                }
            }
        } else {
            loading = false
        }
    }

    val gradientBackground = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF007BFF),
            0.5f to Color(0xFF007BFF),
            0.55f to Color(0xFFBFDFFF),
            1.0f to Color(0xFFF5F5F5)
        )
    )

    Scaffold(
        bottomBar = { TDBottomNavigationBar(
            navController = navController,
            currentScreen = Screens.TenantDashboardScreen.route
        ) },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(padding)
        ) {
            if (loading && tenantData == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                tenantData?.let { tenant ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        // HEADER ROW: Profile + Notifications + Menu
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Left: Profile Image
                            ProfileImage(
                                base64String = tenant.profile_image,
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                            )

                            // Right: Notifications + Menu
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Notification with badge
                                BadgedBox(
                                    badge = {
                                        if (notificationsCount > 0) {
                                            Badge(
                                                containerColor = Color.Red,
                                                contentColor = Color.White
                                            ) {
                                                Text(
                                                    text = notificationsCount.toString(),
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Notifications,
                                        contentDescription = "Notifications",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clickable {
                                                navController.navigate(Screens.NotificationScreen.route)
                                            }
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                // Menu with dropdown
                                var expanded by remember { mutableStateOf(false) }
                                IconButton(onClick = { expanded = true }) {
                                    Icon(
                                        imageVector = Icons.Filled.Menu,
                                        contentDescription = "Menu",
                                        tint = Color.White
                                    )
                                }
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Find Home") },
                                        onClick = {
                                            expanded = false
                                            navController.navigate(Screens.TenantFindHomeScreen.route)
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Settings") },
                                        onClick = {
                                            expanded = false
                                            navController.navigate(Screens.TenantSettingsScreen.route)
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Logout") },
                                        onClick = {
                                            expanded = false
                                            TenantPreferences.clearTenantDetails(context)
                                            AuthPreferences.clearAuthToken(context)
                                            navController.navigate(Screens.WelcomeScreen.route) {
                                                popUpTo(0)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        // END HEADER

                        // WELCOME CARD
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = "Welcome to RentPulse",
                                    fontFamily = montserrat,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = tenant.name ?: "Tenant",
                                    fontFamily = montserrat,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF007BFF)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Your dashboard is ready. Start by verifying your leasing and payment info",
                                    fontFamily = montserrat,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // PROPERTY / PAYMENT STATUS CARDS
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Property(s) Info",
                                    fontFamily = montserrat,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    modifier = Modifier.align(Alignment.Start)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        DashboardStatusCard(
                                            title = "No payment history",
                                            subtitle = "Link lease & start tracking",
                                            icon = Icons.Filled.CheckCircle,
                                            modifier = Modifier.fillMaxWidth(0.5f),
                                            onClick = { navController.navigate(Screens.TenantPaymentHistoryScreen.route) }
                                        )
                                        DashboardStatusCard(
                                            title = "Lease Info",
                                            subtitle = "Upload lease doc to verify your contract",
                                            icon = Icons.Outlined.Description,
                                            modifier = Modifier.weight(1f),
                                            onClick = { navController.navigate(Screens.TenantLeaseVerificationScreen.route) }
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        DashboardStatusCard(
                                            title = "Maintenance Requests",
                                            subtitle = "No tickets yet",
                                            icon = Icons.Outlined.Build,
                                            modifier = Modifier.weight(1f),
                                            onClick = { navController.navigate("tickets") }
                                        )
                                        DashboardStatusCard(
                                            title = "House details",
                                            subtitle = tenant.house_number ?: "Coming soon",
                                            icon = Icons.Outlined.Home,
                                            modifier = Modifier.weight(1f),
                                            onClick = { navController.navigate(Screens.TenantLeaseDetailsScreen.route) }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Receipts & History",
                            fontFamily = montserrat,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Text(
                            text = "Payments will appear here",
                            fontFamily = montserrat,
                            fontSize = 13.sp,
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }
                } ?: Text(
                    text = "No tenant data found",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun DashboardStatusCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFB2F0E9), Color(0xFF82D0F2))
    )

    Card(
        modifier = modifier
            .height(100.dp)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(12.dp)
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        fontFamily = montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = subtitle,
                    fontFamily = montserrat,
                    fontSize = 13.sp,
                    color = Color.Black
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TenantDashboardScreenPreview() {
    val navController = rememberNavController()
    RentPulseTheme {
        TenantDashboardScreen(navController)
    }
}
