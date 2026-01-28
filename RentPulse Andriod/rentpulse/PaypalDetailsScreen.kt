package com.rentpulse

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.data.models.payments.PaymentRequest
import com.rentpulse.navigation.Screens
import kotlinx.coroutines.launch

@Composable
fun PayPalDetailsScreen(navController: NavController) {
    val context = LocalContext.current
    val gradientBrush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF007BFF),
            0.5f to Color(0xFF007BFF),
            0.55f to Color(0xFFBFDFFF),
            1.0f to Color(0xFFF5F5F5)
        )
    )

    var details by remember { mutableStateOf("") }
    var tenantFees by remember { mutableStateOf("") }
    var allowProfileUpdates by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Prefill existing PayPal data (for latest landlord auto-handled by backend)
    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.api.getPaymentInfo()
            if (response.success == true && response.data != null && response.data.method == "PayPal") {
                details = response.data.account_details ?: ""
                allowProfileUpdates = response.data.allow_updates ?: true
            }
        } catch (_: Exception) { }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Back arrow
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.backarrow_ic),
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { navController.popBackStack() }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Receive payments safely! Add your PayPal account ✨",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = details,
            onValueChange = { details = it },
            label = { Text("PayPal Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Toggle: Allow future profile updates
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Allow future profile updates",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.weight(1f)
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

        Spacer(modifier = Modifier.height(20.dp))

        // Save button
        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    val request = PaymentRequest(
                        method = "PayPal",
                        account_details = details,
                        multiple_accounts = false,
                        tenant_fees = tenantFees.toDoubleOrNull() ?: 0.0,
                        allow_updates = allowProfileUpdates
                    )
                    try {
                        val response = RetrofitClient.api.savePaymentInfo(request)
                        if (response.success) {
                            navController.navigate(Screens.PaymentProcessingFeesScreen.route)
                        }
                    } catch (_: Exception) { }
                    isLoading = false
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFFFF)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isLoading) "Saving..." else "Save",
                color = Color.Blue,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PayPalDetailsScreenPreview() {
    val navController = rememberNavController()
    PayPalDetailsScreen(navController)
}
