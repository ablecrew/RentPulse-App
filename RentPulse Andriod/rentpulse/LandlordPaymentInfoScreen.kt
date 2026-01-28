package com.rentpulse

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.navigation.Screens

@Composable
fun LandlordPaymentInfoScreen(
    navController: NavController,
    onSave: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    Box(
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
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Top Bar with Back Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
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
            }

            Spacer(modifier = Modifier.height(50.dp))

            // Title
            Text(
                text = "Payment Information",
                fontFamily = MontserratFont,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(60.dp))

            // White card container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(vertical = 16.dp, horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PaymentInfoButton("Preferred Payment Method") {
                    navController.navigate(Screens.LandlordPaymentMethodScreen.route)
                }
                PaymentInfoButton("Payment Account Details") {
                    navController.navigate("payment_account")
                }
                PaymentInfoButton("Multiple Bank Accounts") {
                    navController.navigate(Screens.MultipleBankAccountsScreen.route)
                }
                PaymentInfoButton("Payment Processing Fees") {
                    navController.navigate(Screens.PaymentProcessingFeesScreen.route)
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Terms clickable
                Text(
                    text = "Your payment info is encrypted and visible only to tenants you approve. Terms",
                    fontFamily = MontserratFont,
                    fontSize = 12.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable {
                        navController.navigate("terms_screen")
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save button
            Button(
                onClick = { navController.navigate(Screens.LandlordDashboardd.route) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
            ) {
                Text("Save", color = Color.White, fontFamily = MontserratFont)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Cancel
            Text(
                text = "Cancel",
                fontFamily = MontserratFont,
                color = Color(0xFF007BFF),
                modifier = Modifier.clickable { navController.navigate(Screens.WelcomeScreen.route) }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Help Center link
            Text(
                text = "Help Center",
                fontFamily = MontserratFont,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Color(0xFF007BFF),
                modifier = Modifier
                    .clickable { navController.navigate(Screens.HelpCenterScreen.route) }
                    .padding(bottom = 12.dp)
            )
        }
    }
}


@Composable
fun PaymentInfoButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                fontFamily = MontserratFont,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Arrow",
                tint = Color.Gray
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewLandlordPaymentInfoScreen() {
    val navController = rememberNavController()
    LandlordPaymentInfoScreen(navController)
}


