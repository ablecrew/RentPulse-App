package com.rentpulse

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.navigation.Screens
import com.rentpulse.ui.theme.RentPulseTheme

@Composable
fun WelcomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to Color(0xFF007BFF),
                        0.40f to Color(0xFF007BFF),
                        0.50f to Color(0xFF80BFFF),
                        0.55f to Color(0xFFBFDFFF),
                        0.60f to Color.White,
                        1.0f to Color.White
                    )
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Logo & Title
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(40.dp))
                Image(
                    painter = painterResource(id = R.drawable.icon_rp),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Welcome to RentPulse", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    "Your Gateway to Smart property living",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    "Track Rent. Build Trust.\nSimplify Living.",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

            // Feature List
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                FeatureItem(Icons.Default.CheckCircle, "View payment status instantly", Color(0xFF2ECC71))
                FeatureItem(Icons.Default.Description, "Access lease details anytime")
                FeatureItem(Icons.Default.Build, "Track maintenance progress easily")
            }

            // Buttons & Footer
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = { navController.navigate(Screens.SignInScreen.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        modifier = Modifier
                            .weight(1f)
                            .shadow(4.dp, shape = RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Sign In", color = Color(0xFF007BFF), fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { navController.navigate(Screens.CreateAccountScreen.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFF007BFF)),
                        modifier = Modifier
                            .weight(1f)
                            .shadow(4.dp, shape = RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Create Account", color = Color(0xFF007BFF), fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Footer with clickable items and dropdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Privacy Policy clickable
                    Text(
                        "Privacy Policy",
                        fontSize = 12.sp,
                        color = Color.Blue,
                        modifier = Modifier.clickable {
                            navController.navigate(Screens.PrivacyPolicyScreen.route)
                        }
                    )

                    // Language dropdown
                    var expanded by remember { mutableStateOf(false) }
                    var selectedLanguage by remember { mutableStateOf("English") }
                    Box {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { expanded = true }
                        ) {
                            Text(selectedLanguage, fontSize = 12.sp)
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            listOf("English", "French", "Spanish", "Swahili").forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text(lang) },
                                    onClick = {
                                        selectedLanguage = lang
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    // RentPulse clickable
                    Text(
                        "RentPulse",
                        fontSize = 12.sp,
                        color = Color.Blue,
                        modifier = Modifier.clickable {
                            navController.navigate(Screens.AboutRentPulseScreen.route)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FeatureItem(icon: ImageVector, text: String, iconTint: Color = Color.Black) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 14.sp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    RentPulseTheme {
        val navController = rememberNavController()
        WelcomeScreen(navController)
    }
}
