package com.rentpulse

import RetrofitClient
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.rentpulse.navigation.Screens
import com.rentpulse.utils.AuthPreferences
import com.rentpulse.utils.LandlordPreferences


@Composable
fun LandlordDashboarddScreen(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                selectedItem = "home",
                onHomeClick = { navController.navigate(Screens.LandlordDashboardd.route) },
                onPropertiesClick = { navController.navigate(Screens.LandlordPropertiesScreen.route) },
                onPaymentsClick = { navController.navigate(Screens.LandlordPaymentInfoScreen.route) },
                onSupportClick = { navController.navigate(Screens.HelpCenterScreen.route) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF007BFF),
                            Color(0xFF007BFF),
                            Color(0xFFBFDFFF),
                            Color(0xFFF5F5F5)
                        )
                    )
                )
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            // ===== TOP BAR =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // --- Helper function to get the logged-in user's ID ---
                fun getUserId(context: Context): Int? {
                    val sharedPreferences = context.getSharedPreferences("RentPulsePrefs", Context.MODE_PRIVATE)
                    return if (sharedPreferences.contains("userId")) {
                        sharedPreferences.getInt("userId", -1).takeIf { it != -1 }
                    } else null
                }

// --- Profile Icon (shows user's image if available) ---
                val context = LocalContext.current
                var profileImageUrl by remember { mutableStateOf<String?>(null) }

                LaunchedEffect(Unit) {
                    val sharedPreferences = context.getSharedPreferences("RentPulsePrefs", Context.MODE_PRIVATE)
                    val savedImage = sharedPreferences.getString("profileImageUrl", null)

                    if (savedImage != null) {
                        // ✅ Use cached image
                        profileImageUrl = savedImage
                        return@LaunchedEffect
                    }

                    try {
                        // 🟢 Fetch landlord profile from API
                        val userId = getUserId(context) ?: return@LaunchedEffect   // <-- safe unwrap

                        // Optional: also treat 0 as invalid id
                        if (userId == 0) return@LaunchedEffect

                        val response = RetrofitClient.api.getLandlordProfile(userId) // userId is non-null Int here

                        if (response.success) {
                            // adjust according to your response model
                            val imagePath = response.data.landlord?.profile_image

                            if (!imagePath.isNullOrEmpty()) {
                                // ✅ Build full image URL dynamically (ensure BASE_URL is public or use a getter)
                                val baseUrl = RetrofitClient.BASE_URL
                                val fullUrl = baseUrl.removeSuffix("/") + "/" + imagePath

                                profileImageUrl = fullUrl

                                // 💾 Cache the image URL locally
                                sharedPreferences.edit()
                                    .putString("profileImageUrl", fullUrl)
                                    .apply()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.3f))
                        .clickable {
                            navController.navigate(Screens.LandlordPersonalInfoScreen.route)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (!profileImageUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = profileImageUrl,
                            contentDescription = "User Profile",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Personal Info",
                            tint = Color.White,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }

                // Right Icons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { /* TODO: Notifications */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
                    }

                    Box {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Settings", fontFamily = montserrat, color = Color.Black) },
                                onClick = { navController.navigate(Screens.LandlordSettingsScreen.route) }
                            )
                            DropdownMenuItem(
                                text = { Text("Invites", fontFamily = montserrat, color = Color.Black) },
                                onClick = { navController.navigate(Screens.LandlordInvitesScreen.route) }
                            )
                            DropdownMenuItem(
                                text = { Text("Logout", fontFamily = montserrat, color = Color.Red) },
                                onClick = {
                                    expanded = false
                                    // Clear all landlord-related stored data
                                    LandlordPreferences.clearLandlordDetails(context)
                                    AuthPreferences.clearAuthToken(context)
                                    // Navigate back to the Welcome screen and clear backstack
                                    navController.navigate(Screens.WelcomeScreen.route) {
                                        popUpTo(0)
                                    }
                                }
                            )

                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ===== WELCOME MESSAGE =====
            Column {
                Text(
                    "Welcome Landlord Name!",
                    color = Color.White,
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    "Your dashboard is ready.",
                    color = Color.White.copy(alpha = 0.9f),
                    fontFamily = montserrat,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== MAIN DASHBOARD CARD =====
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {

                // --- Properties & Tenants ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DashboardCard(
                        title = "12 Properties",
                        subtitle = "83% Occupancy",
                        icon = Icons.Default.Home,
                        font = montserrat,

                    )
                    DashboardCard(
                        title = "Active Tenants",
                        subtitle = "Coming soon",
                        icon = Icons.Default.People,
                        font = montserrat
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // --- Payments Overview ---
                PaymentsOverviewCard(font = montserrat)

                Spacer(modifier = Modifier.height(12.dp))

                // --- Maintenance ---
                SectionTitle("Maintenance", font = montserrat)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatusCard("New Request", Icons.Default.Build, montserrat)
                    StatusCard("In Progress", Icons.Default.HourglassBottom, montserrat)
                    StatusCard("Pending", Icons.Default.Pending, montserrat)
                    StatusCard("Completed", Icons.Default.CheckCircle, montserrat)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- Lease Overview ---
                SectionTitle("Lease Overview", font = montserrat)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatusCard("New Request", Icons.Default.Description, montserrat)
                    StatusCard("Accepted", Icons.Default.Check, montserrat)
                    StatusCard("Upcoming", Icons.Default.Event, montserrat)
                    StatusCard("Declined", Icons.Default.Close, montserrat)
                }

                Spacer(modifier = Modifier.height(16.dp))



                    // --- Messages ---
                    SectionTitle("Messages", font = montserrat)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatusCard("All", Icons.Default.Message, montserrat)

                        Box(contentAlignment = Alignment.TopEnd) {
                            StatusCard("Unread", Icons.Default.MarkEmailUnread, montserrat)
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                                    .align(Alignment.TopEnd)
                            )
                        }

                        StatusCard("Calls", Icons.Default.Call, montserrat)
                    }

                }

            }
        }
    }


// ====== REUSABLE COMPONENTS ======

@Composable
fun DashboardCard(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, font: FontFamily) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = Modifier
            .width(150.dp)
            .height(70.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF007BFF))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(title, fontFamily = font, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color. Black)
                Text(subtitle, fontFamily = font, color = Color.Black, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun PaymentsOverviewCard(font: FontFamily) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Payment, contentDescription = null, tint = Color(0xFF007BFF))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Payments Overview", fontFamily = font, fontWeight = FontWeight.Bold, color = Color.Black)
                }
                Text("This Month", fontFamily = font, color = Color.Gray, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text("Ksh. 2,389,788", fontFamily = font, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Text("of Ksh. 2,738,568", fontFamily = font, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = 0.85f,
                color = Color(0xFF007BFF),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "2 overdue payments",
                fontFamily = font,
                color = Color.Red,
                fontSize = 12.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun SectionTitle(title: String, font: FontFamily) {
    Text(
        title,
        fontFamily = font,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = Color.Black,
        modifier = Modifier.padding(vertical = 6.dp)
    )
}

@Composable
fun StatusCard(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, font: FontFamily) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F0FE)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF007BFF))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontFamily = font, fontSize = 12.sp, textAlign = TextAlign.Center, color = Color.Black)
    }
}

// ===== BOTTOM NAVIGATION =====
@Composable
fun BottomNavigationBar(
    navController: NavController,
    selectedItem: String = "home",
    onHomeClick: () -> Unit,
    onPropertiesClick: () -> Unit,
    onPaymentsClick: () -> Unit,
    onSupportClick: () -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        @Composable
        fun navItem(iconRes: Int, label: String, itemKey: String, onClick: () -> Unit) {
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = label,
                        modifier = Modifier.size(24.dp),
                        tint = if (selectedItem == itemKey) Color(0xFF007BFF) else Color.Black
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 13.sp,
                        color = if (selectedItem == itemKey) Color(0xFF007BFF) else Color.Black
                    )
                },
                selected = selectedItem == itemKey,
                onClick = onClick
            )
        }

        navItem(R.drawable.home_ic, "Home", "home", { navController.navigate(Screens.LandlordDashboardd.route) })
        navItem(R.drawable.properties_ic, "Properties", "properties", { navController.navigate(Screens.LandlordPropertiesScreen.route) })
        navItem(R.drawable.payments_ic, "Payments", "payments", { navController.navigate(Screens.LandlordPaymentInfoScreen.route) })
        navItem(R.drawable.support_ic, "Support", "support", { navController.navigate(Screens.HelpCenterScreen.route) })
    }
}

// ===== PREVIEW =====
@Preview(showBackground = true)
@Composable
fun PreviewLandlordDashboard() {
    val navController = rememberNavController()
    LandlordDashboarddScreen(navController)
}
