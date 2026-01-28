package com.rentpulse.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

@Composable
fun PrivacyPolicyScreen(
    navController: NavController,
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onSupportClick: () -> Unit = {}
) {
    val montserrat = FontFamily(Font(R.font.montserrat_regular))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to Color(0xFF007BFF),
                        0.5f to Color(0xFF007BFF),
                        0.55f to Color(0xFFBFDFFF),
                        1.0f to Color(0xFFF5F5F5)
                    )
                )
            )
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Privacy Policy",
                fontFamily = montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White
            )
        }

        // Content Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                PolicySection(
                    title = "Our Commitment to Your Privacy",
                    body = "At RentWise, we value your privacy and are committed to protecting your personal information. This Privacy Policy explains how we collect, use, and share your information when you use our app. By using RentWise, you agree to the terms of this policy.",
                    montserrat
                )
                PolicySection(
                    title = "Information We Collect",
                    body = "We collect information you provide directly, such as your name, contact details, and payment information. We also collect data automatically, including your device information and app usage patterns. This helps us improve our services and provide a better user experience.",
                    montserrat
                )
                PolicySection(
                    title = "How We Use Your Information",
                    body = "Your information is used to facilitate payments, communicate with you, and enhance our app's functionality. We may also use your data for analytics and marketing purposes, always ensuring your privacy and data security.",
                    montserrat
                )
                PolicySection(
                    title = "Sharing Your Information",
                    body = "We may share your information with service providers who assist us in operating the app, processing payments, and providing customer support. We do not sell your personal information to third parties. All data sharing is done under strict confidentiality agreements.",
                    montserrat
                )
                PolicySection(
                    title = "Your Rights",
                    body = "You have the right to access, correct, or delete your personal information. You can also object to certain data processing activities. To exercise these rights, please contact us at the address below.",
                    montserrat
                )
                PolicySection(
                    title = "Contact Us",
                    body = "If you have any questions or concerns about this Privacy Policy, please contact us at:\n\nRentPulse Support\n453 5th Ngong Avenue\n\nEmail: support@rentpulse.com",
                    montserrat
                )
            }
        }

        // Bottom Navigation
        NavigationBar(containerColor = Color.White) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home", fontFamily = montserrat) },
                selected = false,
                onClick = { onHomeClick() }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Info, contentDescription = "Support") },
                label = { Text("Support", fontFamily = montserrat) },
                selected = false,
                onClick = { navController.navigate(Screens.HelpCenterScreen.route) }
            )
        }
    }
}

@Composable
fun PolicySection(title: String, body: String, fontFamily: FontFamily) {
    Text(
        text = title,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = Color.Black
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = body,
        fontFamily = fontFamily,
        fontSize = 14.sp,
        color = Color.Black
    )
    Spacer(modifier = Modifier.height(12.dp))
}

@Preview(showBackground = true)
@Composable
fun PrivacyPolicyScreenPreview() {
    val navController = rememberNavController()
    PrivacyPolicyScreen(
        navController,
        onBackClick = {},
        onHomeClick = {},
        onSupportClick = {navController.navigate(Screens.HelpCenterScreen.route)}
    )
}


