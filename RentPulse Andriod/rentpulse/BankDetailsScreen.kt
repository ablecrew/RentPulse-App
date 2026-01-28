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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.data.models.payments.PaymentRequest
import com.rentpulse.navigation.Screens
import kotlinx.coroutines.launch

@Composable
fun BankDetailsScreen(navController: NavController) {
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
    var allowProfileUpdates by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    var tenantFees by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

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

        // Header
        Text(
            text = "Link your bank account for smooth and secure payments 💳",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Bank details input
        OutlinedTextField(
            value = details,
            onValueChange = { details = it },
            label = { Text("Bank A/C Number") },
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

        // Toggle
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
                        landlord_id = null, // Not used for now
                        method = "Bank",
                        account_details = details,
                        multiple_accounts = false,
                        tenant_fees = tenantFees.toDoubleOrNull() ?: 0.0,
                        allow_updates = allowProfileUpdates
                    )
                    try {
                        RetrofitClient.api.savePaymentInfo(request)
                        navController.navigate(Screens.PaymentProcessingFeesScreen.route)
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
fun BankDetailsScreenPreview() {
    val navController = rememberNavController()
    BankDetailsScreen(navController)
}
