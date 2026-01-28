package com.rentpulse.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.R
import com.rentpulse.navigation.Screens

@Composable
fun TenantLeaseVerificationScreen(
    navController: NavController,
    apartmentName: String = "Apartment name!"
) {
    val montserrat = FontFamily(Font(R.font.montserrat_regular))
    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Handle uploaded document URI here
        }
    }

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
        // Top App Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .clickable { navController.popBackStack() }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Text(
            text = "Lease Verification",
            fontFamily = montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Card Content
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    buildAnnotatedString {
                        append("Please upload a copy of your signed lease agreement for ")
                        withStyle(style = SpanStyle(color = Color(0xFF007BFF), fontWeight = FontWeight.Bold)) {
                            append(apartmentName)
                        }
                        append(" to verify your lease")
                    },
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Image(
                    painter = painterResource(id = R.drawable.document_ic),
                    contentDescription = "Document Icon",
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { documentPickerLauncher.launch("*/*") },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
                ) {
                    Text(
                        text = "Upload Document",
                        color = Color.White,
                        fontFamily = montserrat,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Navigation
        NavigationBar(containerColor = Color.White) {
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate(Screens.TenantDashboardScreen.route) },
                icon = { Icon(painterResource(id = R.drawable.home_ic), contentDescription = "Home") },
                label = { Text("Home", fontFamily = montserrat) }
            )
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate(Screens.TenantSettingsScreen.route) },
                icon = { Icon(painterResource(id = R.drawable.settings_ic), contentDescription = "Settings") },
                label = { Text("Settings", fontFamily = montserrat) }
            )
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate(Screens.TenantLeaseDetailsScreen.route) },
                icon = { Icon(painterResource(id = R.drawable.lease_ic), contentDescription = "Settings") },
                label = { Text("Lease", fontFamily = montserrat) }
            )
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate(Screens.TenantPaymentDashboardScreen.route) },
                icon = { Icon(painterResource(id = R.drawable.payment_ic), contentDescription = "Payments") },
                label = { Text("Payments", fontFamily = montserrat) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TenantLeaseVerificationScreenPreview() {
    val navController = rememberNavController()
    TenantLeaseVerificationScreen(navController,
        apartmentName = "Apartment name!")
}


