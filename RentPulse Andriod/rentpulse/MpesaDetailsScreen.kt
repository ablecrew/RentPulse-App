package com.rentpulse

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.data.models.payments.PaymentRequest
import com.rentpulse.navigation.Screens
import com.rentpulse.ui.theme.Montserrat
import com.rentpulse.utils.AuthPreferences
import kotlinx.coroutines.*

@Composable
fun MpesaDetailsScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var details by remember { mutableStateOf(TextFieldValue("")) }
    var tenantFees by remember { mutableStateOf("") }
    var allowProfileUpdates by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Animation for card slide-up
    var startAnimation by remember { mutableStateOf(false) }
    val cardOffset by animateDpAsState(
        targetValue = if (startAnimation) 0.dp else 80.dp,
        animationSpec = tween(durationMillis = 700)
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

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
            .padding(16.dp)
    ) {
        // --- Top Bar ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "M-Pesa Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // --- Main Card ---
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.Center)
                .offset(y = cardOffset)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                OutlinedTextField(
                    value = details,
                    onValueChange = { details = it },
                    label = { Text("Enter your M-Pesa Business/Till Number", color = Color.Black) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = TextStyle(fontFamily = Montserrat, color = Color.Black),
                    singleLine = true
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Allow future updates to this profile",
                        fontSize = 16.sp
                    )
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

                // --- Save Button ---
                Button(
                    onClick = {
                        if (details.text.isBlank()) {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        scope.launch(Dispatchers.IO) {
                            isLoading = true
                            try {
                                val request = PaymentRequest(
                                    method = "M-Pesa",
                                    account_details = details.text,
                                    multiple_accounts = false,
                                    tenant_fees = tenantFees.toDoubleOrNull() ?: 0.0,
                                    allow_updates = allowProfileUpdates
                                )

                                val response = RetrofitClient.api.savePaymentInfo(request)
                                Log.d("PaymentResponse", "Success=${response.success}, Message=${response.message}")

                                if (response.success == true) {
                                    AuthPreferences.saveUserRole(context, "landlord")

                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Landlord profile saved!", Toast.LENGTH_SHORT).show()
                                        delay(400)
                                        navController.navigate(Screens.LandlordDashboardd.route) {
                                            popUpTo(Screens.MpesaDetailsScreen.route) { inclusive = true }
                                            launchSingleTop = true
                                        }
                                    }
                                } else {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Error: ${response.message}", Toast.LENGTH_LONG).show()
                                    }
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                                }
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    enabled = !isLoading && details.text.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF007BFF),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = if (isLoading) "Saving..." else "Finish",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }

                Text(
                    text = "You're almost done! RentPulse ensures every payment detail stays secure and seamless 💪",
                    color = Color(0xFF007BFF),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MpesaDetailsScreenPreview() {
    val navController = rememberNavController()
    MpesaDetailsScreen(navController)
}
