package com.rentpulse

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.data.models.payments.PaymentRequest
import com.rentpulse.navigation.Screens
import kotlinx.coroutines.launch

@Composable
fun PaymentProcessingFeesScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var tenantFees by remember { mutableStateOf("") }
    var allowProfileUpdates by remember { mutableStateOf(true) }
    var selectedMethod by remember { mutableStateOf("processing_fee") } // default tag for backend
    var isLoading by remember { mutableStateOf(false) }

    val gradientBrush = Brush.verticalGradient(
        listOf(
            Color(0xFF007BFF),
            Color(0xFF007BFF),
            Color(0xFFBFDFFF),
            Color(0xFFF5F5F5)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind { drawRect(brush = gradientBrush) }
    ) {
        // --- Back Arrow ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        // --- White Card in Center ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Payment Processing Fees",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF007BFF)
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    OutlinedTextField(
                        value = tenantFees,
                        onValueChange = { tenantFees = it },
                        label = { Text("Enter processing fee (KSh)", color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(color = Color.Black),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF007BFF),
                            unfocusedBorderColor = Color.Gray,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(35.dp))

                    Button(
                        onClick = {
                            Log.d("PaymentProcessing", "Save button clicked ✅")

                            scope.launch {
                                isLoading = true

                                val request = PaymentRequest(
                                    method = selectedMethod,
                                    account_details = "N/A",
                                    multiple_accounts = false,
                                    tenant_fees = tenantFees.toDoubleOrNull() ?: 0.0,
                                    allow_updates = allowProfileUpdates
                                    // landlord_id intentionally omitted during registration
                                )

                                try {
                                    val response = RetrofitClient.api.savePaymentInfo(request)

                                    if (response.success == true) {
                                        Toast.makeText(context, "Saved successfully ✅", Toast.LENGTH_SHORT).show()

                                        // Navigate to dashboard or next screen
                                        navController.navigate(Screens.LandlordDashboardd.route) {
                                            popUpTo(Screens.PaymentProcessingFeesScreen.route) { inclusive = true }
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            response.message ?: "Failed to save",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: Exception) {
                                    Log.e("PaymentProcessing", "Error: ${e.message}", e)
                                    Toast.makeText(context, "Network or server error", Toast.LENGTH_SHORT).show()
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Saving...", color = Color.White, fontWeight = FontWeight.Bold)
                        } else {
                            Text("Save", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentProcessingFeesScreenPreview() {
    val navController = rememberNavController()
    PaymentProcessingFeesScreen(navController)
}
