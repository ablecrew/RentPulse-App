package com.rentpulse

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.navigation.Screens
import com.rentpulse.data.models.payments.PaymentRequest
import kotlinx.coroutines.launch

@Composable
fun LandlordPaymentMethodScreen(navController: NavController) {

    val gradientBrush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF007BFF),
            0.5f to Color(0xFF007BFF),
            0.55f to Color(0xFFBFDFFF),
            1.0f to Color(0xFFF5F5F5)
        )
    )

    val context = LocalContext.current
    var allowProfileUpdates by remember { mutableStateOf(true) }
    var selectedMethod by remember { mutableStateOf("") }
    var tenantFees by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
            .padding(16.dp)
    ) {
        // Back Arrow
        Icon(
            painter = painterResource(id = R.drawable.backarrow_ic),
            contentDescription = "Back",
            tint = Color.White,
            modifier = Modifier
                .size(28.dp)
                .clickable { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Title
        Text(
            text = "Payment Method",
            style = TextStyle(
                fontFamily = MontserratFont,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.White
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Card for payment methods
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Account Details",
                    fontFamily = MontserratFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Payment options
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PaymentOption(R.drawable.paypal_ic) {
                        selectedMethod = "PayPal"
                        navController.navigate(Screens.PayPalDetailsScreen.route)
                    }
                    PaymentOption(R.drawable.mpesa_ic) {
                        selectedMethod = "M-Pesa"
                        navController.navigate(Screens.MpesaDetailsScreen.route)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PaymentOption(R.drawable.mastercard_ic) {
                        selectedMethod = "MasterCard"
                        navController.navigate(Screens.MastercardDetailsScreen.route)
                    }
                    PaymentOption(R.drawable.bank_ic) {
                        selectedMethod = "Bank"
                        navController.navigate(Screens.BankDetailsScreen.route)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Switch
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Allow future profile updates",
                        fontFamily = MontserratFont,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = allowProfileUpdates,
                        onCheckedChange = { allowProfileUpdates = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF007BFF),
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.Gray
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ✅ Save button (clean API call)
                Button(
                    onClick = {
                        scope.launch {
                            try {
                                val api = RetrofitClient.api
                                val request = PaymentRequest(
                                    method = selectedMethod,
                                    account_details = "",
                                    multiple_accounts = false,
                                    tenant_fees = tenantFees.toDoubleOrNull() ?: 0.0,
                                    allow_updates = allowProfileUpdates
                                )

                                val response = api.savePaymentInfo(request)

                                if (response.success) {
                                    navController.navigate(Screens.PaymentProcessingFeesScreen.route)
                                } else {
                                    // Handle error if save fails
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Save",
                        color = Color.White,
                        fontFamily = MontserratFont,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Cancel
                Text(
                    text = "Cancel",
                    color = Color(0xFF007BFF),
                    fontFamily = MontserratFont,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Help Center
        Text(
            text = "Help Center",
            color = Color(0xFF007BFF),
            fontFamily = MontserratFont,
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { navController.navigate(Screens.HelpCenterScreen.route) }
        )
    }
}

@Composable
fun PaymentOption(iconRes: Int, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .size(80.dp)
            .clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LandlordPaymentMethodScreenPreview() {
    val navController = rememberNavController()
    LandlordPaymentMethodScreen(navController)
}
