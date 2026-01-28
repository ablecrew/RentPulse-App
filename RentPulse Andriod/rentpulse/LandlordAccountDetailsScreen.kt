package com.rentpulse

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun LandlordAccountDetailsScreen(navController: NavController, methodName: String) {
    val gradientBrush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF007BFF),
            0.5f to Color(0xFF007BFF),
            0.55f to Color(0xFFBFDFFF),
            1.0f to Color(0xFFF5F5F5)
        )
    )

    var details by remember { mutableStateOf("") }

    // Label depends on payment method
    val labelText = when (methodName) {
        "M-Pesa" -> "Paybill / Till No."
        "PayPal" -> "Email"
        "Mastercard" -> "Visa Card No."
        "Bank" -> "Bank A/C No."
        else -> "Account Number / Till / Paybill"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
            .padding(20.dp)
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

        // Screen Content
        Text(
            text = "Enter $methodName Details",
            fontFamily = MontserratFont,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = details,
            onValueChange = { details = it },
            label = { Text(labelText) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(color = Color.Black),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedBorderColor = Color(0xFF007BFF),
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                // TODO: Save details via API
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFFFF)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Save",
                color = Color.Blue,
                fontFamily = MontserratFont,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LandlordAccountDetailsScreenPreview() {
    val navController = rememberNavController()
    LandlordAccountDetailsScreen(navController, "M-Pesa")
}
