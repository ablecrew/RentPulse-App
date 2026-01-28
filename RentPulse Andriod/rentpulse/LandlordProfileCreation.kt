package com.rentpulse

import androidx.compose.foundation.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.navigation.Screens


val montserratFontFamily = FontFamily.Default

@Composable
fun LandlordProfileCreationScreen(
    navController: NavController,
    onPersonalInfoClick: () -> Unit = {},
    onPropertyInfoClick: () -> Unit = {},
    onPaymentInfoClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onHelpCenterClick: () -> Unit = {},
    onNavigateToDashboard: () -> Unit = {}
) {
    var allowUpdates by remember { mutableStateOf(true) }
    var termsAccepted by remember { mutableStateOf(false) }

    Box(
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            // Status bar space
            Spacer(modifier = Modifier.height(60.dp))

            // Title Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Set Up Your\nLandlord Profile",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = montserratFontFamily,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 38.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Share your properties and payment info\nto start collecting rent",
                    fontSize = 16.sp,
                    fontFamily = montserratFontFamily,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // White Container with form elements
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Property icon
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.property_icon),
                            contentDescription = "Property Icon",
                            modifier = Modifier.size(56.dp),
                            colorFilter = ColorFilter.tint(Color.Black)
                        )
                    }

                    // Personal Information Button
                    ProfileSetupButton(
                        text = "Personal Information",
                        onClick = {
                            navController.navigate(Screens.LandlordPersonalInfoScreen.route) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Property Information Button
                    ProfileSetupButton(
                        text = "Property Information",
                        onClick = {
                            navController.navigate(Screens.LandlordPropertyInfoScreen.route) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Payment Information Button
                    ProfileSetupButton(
                        text = "Payment Information",
                        onClick = {  navController.navigate(Screens.LandlordPaymentInfoScreen.route) }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Allow future profile updates toggle (outside white container)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Allow future profile updates",
                    fontSize = 14.sp,
                    fontFamily = montserratFontFamily,
                    color = Color.Black.copy(alpha = 0.7f),
                    modifier = Modifier.weight(1f)
                )

                Switch(
                    checked = allowUpdates,
                    onCheckedChange = { allowUpdates = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF007BFF),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.Gray.copy(alpha = 0.3f)
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom section with Terms and Help Center (outside white container)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Terms & Policy with checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        termsAccepted = !termsAccepted
                        if (termsAccepted) {
                            onNavigateToDashboard()
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (termsAccepted) Icons.Default.CheckCircle else Icons.Default.CheckCircle,
                        contentDescription = "Terms checkbox",
                        tint = if (termsAccepted) Color(0xFF007BFF) else Color.Gray.copy(alpha = 0.3f),
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Terms & Policy",
                        fontSize = 14.sp,
                        fontFamily = montserratFontFamily,
                        color = Color(0xFF007BFF),
                        modifier = Modifier.clickable { navController.navigate(Screens.TermsOfServiceScreen.route) }
                    )
                }

                // Help Center
                Text(
                    text = "Help Center",
                    fontSize = 14.sp,
                    fontFamily = montserratFontFamily,
                    color = Color.Blue.copy(alpha = 0.7f),
                    modifier = Modifier.clickable { navController.navigate(Screens.HelpCenterScreen.route) }
                )
            }



            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun ProfileSetupButton(
    text: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LandlordProfileCreationScreenPreview() {
    val navController = rememberNavController()
    LandlordProfileCreationScreen(
        navController,
        onPersonalInfoClick = {  navController.navigate(Screens.LandlordPersonalInfoScreen.route) },
        onPropertyInfoClick = { navController.navigate(Screens.LandlordPropertyInfoScreen.route) },
        onPaymentInfoClick = { navController.navigate(Screens.LandlordPaymentInfoScreen.route)},
        onTermsClick = { navController.navigate(Screens.TermsOfServiceScreen.route) },
        onHelpCenterClick = { navController.navigate(Screens.HelpCenterScreen.route) },
        onNavigateToDashboard = { navController.navigate(Screens.LandlordDashboardd.route) }
    )
}
