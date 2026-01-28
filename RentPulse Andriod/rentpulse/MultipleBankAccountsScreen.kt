package com.rentpulse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.launch

@Composable
fun MultipleBankAccountsScreen(navController: NavController) {
    val context = LocalContext.current
    val gradientBrush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF007BFF),
            0.5f to Color(0xFF007BFF),
            0.55f to Color(0xFFBFDFFF),
            1.0f to Color(0xFFF5F5F5)
        )
    )

    var accounts by remember { mutableStateOf("") }
    var tenantFees by remember { mutableStateOf("") }
    var allowProfileUpdates by remember { mutableStateOf(true) }
    var selectedMethod by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Prefill existing data (auto-handled by backend)
    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.api.getPaymentInfo()
            if (response.success == true && response.data != null) {
                accounts = if (response.data.multiple_accounts?.lowercase() == "true") "true" else "false"
                allowProfileUpdates = response.data.allow_updates ?: true
            }
        } catch (_: Exception) { }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Multiple Bank Accounts",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = accounts,
            onValueChange = { accounts = it },
            label = { Text("Enter multiple accounts (comma-separated)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(
                fontFamily = com.rentpulse.ui.theme.Montserrat,
                color = Color.Black
            ),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF007BFF),
                unfocusedBorderColor = Color(0xFFBDBDBD),
                focusedLabelColor = Color(0xFF007BFF),
                unfocusedLabelColor = Color.Gray,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color(0xFF007BFF),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
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

        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    val request = PaymentRequest(
                        method = selectedMethod,
                        account_details = null,
                        multiple_accounts = accounts.lowercase() == "true",
                        tenant_fees = tenantFees.toDoubleOrNull() ?: 0.0,
                        allow_updates = allowProfileUpdates
                    )
                    try {
                        val response = RetrofitClient.api.savePaymentInfo(request)
                        if (response.success == true) {
                            navController.popBackStack()
                        }
                    } catch (_: Exception) { }
                    isLoading = false
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isLoading) "Saving..." else "Save",
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MultipleBankAccountsScreenPreview() {
    val navController = rememberNavController()
    MultipleBankAccountsScreen(navController)
}
