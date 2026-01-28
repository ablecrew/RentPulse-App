package com.rentpulse.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.R
import com.rentpulse.navigation.Screens

@Composable
fun TermsOfServiceScreen(navController: NavController) {
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
        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Terms of Service",
                fontFamily = montserrat,
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        // Scrollable Terms
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            TermsSection(
                montserrat, "Welcome to our platform! By using our services, you agree to these terms. " +
                        "Please read them carefully. To prevent any inconveniences."
            )
            TermsSection(
                montserrat, "1. Acceptance of Terms",
                "By accessing or using our services, you agree to be bound by these terms and all applicable laws and regulations. " +
                        "If you do not agree with any of these terms, you are prohibited from using or accessing this site."
            )
            TermsSection(
                montserrat, "2. Description of Service",
                "Our platform provides tools for landlords to manage tenant payments, including rent collection, payment tracking, and communication features, and also help tenants meet their living standard goals."
            )
            TermsSection(
                montserrat, "3. User Accounts",
                "To use certain features, you must create an account. You are responsible for maintaining the confidentiality of your account information and for all activities that occur under your account."
            )
            TermsSection(
                montserrat, "4. Payments and Fees",
                "We may charge fees for certain services. You agree to pay all applicable fees in a timely manner. Payments are processed securely through our payment partners."
            )
            TermsSection(
                montserrat, "5. Privacy Policy",
                "Your privacy is important to us. Please review our Privacy Policy to understand how we collect, use, and protect your information."
            )
            TermsSection(
                montserrat, "6. Termination",
                "We may terminate or suspend your account at any time, with or without cause, without prior notice."
            )
            TermsSection(
                montserrat, "7. Changes to Terms",
                "We reserve the right to modify these terms at any time. Your continued use of the service after any changes constitutes your acceptance of the new terms."
            )
            TermsSection(
                montserrat, "8. Contact Us",
                "If you have any questions about these terms, please contact us at support@rentpulse.com."
            )
            Spacer(modifier = Modifier.height(80.dp)) // Space for bottom nav
        }

        // Bottom Navigation
        NavigationBar(containerColor = Color.White) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home", fontFamily = montserrat) },
                selected = false,
                onClick = { /* navController.navigate("home") */ }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.SupportAgent, contentDescription = "Support") },
                label = { Text("Support", fontFamily = montserrat) },
                selected = false,
                onClick = { navController.navigate(Screens.HelpCenterScreen.route) }
            )
        }
    }
}

@Composable
fun TermsSection(fontFamily: FontFamily, title: String, body: String? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color.White)
            .padding(12.dp)
    ) {
        Text(
            text = title,
            fontFamily = fontFamily,
            fontWeight = if (body != null) FontWeight.Bold else FontWeight.Normal,
            fontSize = if (body != null) 16.sp else 14.sp,
            textAlign = TextAlign.Start
        )
        if (body != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = body,
                fontFamily = fontFamily,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTermsOfService() {
    val navController = rememberNavController()
    TermsOfServiceScreen(navController)
}


