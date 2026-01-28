package com.rentpulse

import com.rentpulse.navigation.Screens
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.ui.screens.TDBottomNavigationBar


@Composable
fun TenantPaymentScreen(navController: NavController) {
    var amount by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf("M-pesa") }

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
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Back Button
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = "Make Payment",
                fontFamily = MontserratFont,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // White Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Amount Input
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Amount", fontFamily = MontserratFont, color = Color.Black) },
                        singleLine = true,
                        textStyle = TextStyle(
                            fontFamily = MontserratFont,
                            color = Color.Black),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedIndicatorColor = Color(0xFF007BFF),
                            unfocusedIndicatorColor = Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Payment Method
                    Text(
                        text = "Payment Method",
                        fontFamily = MontserratFont,
                        fontSize = 16.sp,
                        color = Color.Black
                    )

                    val methods = listOf("M-pesa", "Bank Transfer", "PayPal", "Credit Card")
                    methods.forEach { method ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedMethod = method
                                }
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = method,
                                fontFamily = MontserratFont,
                                color = Color.Black,
                                modifier = Modifier.weight(1f)
                            )
                            RadioButton(
                                selected = selectedMethod == method,
                                onClick = {
                                    selectedMethod = method
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF007BFF),
                                    unselectedColor = Color.Gray
                                )
                            )
                        }
                        Divider(color = Color(0xFFE0E0E0))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Button
            Button(
                onClick = { navController.navigate("paymentDetails/$selectedMethod") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Confirm", fontFamily = MontserratFont, color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Navigation
            TDBottomNavigationBar(
                navController = navController,
                currentScreen = Screens.TenantPaymentDashboardScreen.route
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TenantPaymentScreenPreview() {
    TenantPaymentScreen(navController = rememberNavController())
}
