package com.rentpulse.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.R

val Montserrat = FontFamily(Font(R.font.montserrat_regular))

@Composable
fun AboutRentPulseScreen(
    navController: NavController,
    onNavigateHome: () -> Unit = {},
    onNavigateSupport: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
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
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBackClick() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "About RentPulse",
                fontFamily = Montserrat,
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // White Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Our Mission",
                fontFamily = Montserrat,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "RentPulse is dedicated to simplifying rent management for landlords. We aim to provide a seamless, efficient, and transparent platform that empowers landlords to focus on their properties, not paperwork.\n We also help the tenants find their desired living styles and habits and the conducive environment that meet their expectations.",
                fontFamily = Montserrat,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Key Features",
                fontFamily = Montserrat,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            FeatureItem("Automated Rent Collection")
            FeatureItem("Real-time Payment Tracking")
            FeatureItem("Tenant Communication Tools")
            FeatureItem("Secure and Reliable")

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Contact Us",
                fontFamily = Montserrat,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                buildAnnotatedString {
                    append("For any questions or support, please reach out to us at ")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF007BFF),
                            textDecoration = TextDecoration.Underline
                        )
                    ) { append("support@rentpulse.com") }
                    append(" or call us at ")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF007BFF),
                            textDecoration = TextDecoration.Underline
                        )
                    ) { append("(254) 707-528-980") }
                    append(".")
                },
                fontFamily = Montserrat,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Navigation
        NavigationBar(containerColor = Color.White) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home", fontFamily = Montserrat) },
                selected = false,
                onClick = { onNavigateHome() }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.SupportAgent, contentDescription = "Support") },
                label = { Text("Support", fontFamily = Montserrat) },
                selected = false,
                onClick = { onNavigateSupport() }
            )
        }
    }
}

@Composable
fun FeatureItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(Color(0xFFF5F5F5), RoundedCornerShape(6.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontFamily = Montserrat, fontSize = 14.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun AboutRentPulseScreenPreview() {
    val navController = rememberNavController()
    AboutRentPulseScreen(navController)
}

