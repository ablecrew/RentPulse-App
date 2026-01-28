package com.rentpulse

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.navigation.Screens
import com.rentpulse.ui.screens.TDBottomNavigationBar


@Composable
fun TenantSettingsScreen(navController: NavController) {
    val gradientBrush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF007BFF),
            0.5f to Color(0xFF007BFF),
            0.55f to Color(0xFFBFDFFF),
            1.0f to Color(0xFFF5F5F5)
        )
    )

    Scaffold(
        bottomBar = {
            TDBottomNavigationBar(
                navController = navController,
                currentScreen = Screens.TenantSettingsScreen.route
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradientBrush)
                .padding(padding)
        ) {
            // Top Bar with Back Arrow & Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Settings",
                    fontFamily = MontserratFont,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // White Card Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color.White, shape = MaterialTheme.shapes.medium)
                    .padding(16.dp)
            ) {
                // Profile Section
                SettingsItem(
                    icon = { Image(painterResource(id = R.drawable.profile_icon), contentDescription = "Profile") },
                    title = "Profile",
                    subtitle = "Edit your profile information"
                ) {
                    navController.navigate("tenantProfile")
                }

                Divider()

                // Payments
                SettingsItem(
                    icon = { Icon(Icons.Default.Payment, contentDescription = "Payment", tint = Color.Gray) },
                    title = "Payment Methods",
                    subtitle = "Manage your payment methods"
                ) {
                    navController.navigate("paymentMethods")
                }

                Divider()

                // Notifications
                SettingsItem(
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.Gray) },
                    title = "Notifications",
                    subtitle = "Manage your notification settings"
                ) {
                    navController.navigate("notifications")
                }

                Divider()

                // Language
                SettingsItem(
                    icon = { Icon(Icons.Default.Language, contentDescription = "Language", tint = Color.Gray) },
                    title = "Language",
                    subtitle = "Choose your preferred language"
                ) {
                    navController.navigate("language")
                }

                Divider()

                // Help
                SettingsItem(
                    icon = { Icon(Icons.Default.Help, contentDescription = "Help", tint = Color.Gray) },
                    title = "Help",
                    subtitle = "Get help and support"
                ) {
                    navController.navigate(Screens.HelpCenterScreen.route)
                }

                Divider()

                // Contact Us
                SettingsItem(
                    icon = { Icon(Icons.Default.ContactMail, contentDescription = "Contact Us", tint = Color.Gray) },
                    title = "Contact Us",
                    subtitle = "Contact support"
                ) {
                    navController.navigate("contact")
                }

                Divider()

                // Change Password
                SettingsItem(
                    icon = { Icon(Icons.Default.Lock, contentDescription = "Change Password", tint = Color.Gray) },
                    title = "Change Password",
                    subtitle = "Change your password"
                ) {
                    navController.navigate("changePassword")
                }
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFF0F0F0), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, fontFamily = MontserratFont, fontSize = 16.sp, color = Color.Black)
            Text(text = subtitle, fontFamily = MontserratFont, fontSize = 12.sp, color = Color.Gray)
        }
    }
}


@Composable
@Preview(showBackground = true)
fun TenantSettingsScreenPreview() {
    val navController = rememberNavController()
    TenantSettingsScreen(navController)
}
