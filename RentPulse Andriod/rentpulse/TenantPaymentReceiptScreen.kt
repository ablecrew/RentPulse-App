package com.rentpulse

import android.widget.Toast
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.navigation.Screens
import com.rentpulse.ui.screens.TDBottomNavigationBar
import com.rentpulse.ui.theme.Montserrat


@Composable
fun TenantPaymentReceiptScreen(
    navController: NavController,
    propertyName: String = "123 Main St Apt 4B",
    propertyImage: Int = R.drawable.apartment_ic, // replace with tenant property image
    amount: String = "Ksh.155,000",
    paymentMethod: String = "M-pesa",
    transactionId: String = "TXN1234567890",
    date: String = "Aug 1, 2025"
) {
    val gradientBackground = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF007BFF),
            0.5f to Color(0xFF007BFF),
            0.55f to Color(0xFFBFDFFF),
            1.0f to Color(0xFFF5F5F5)
        )
    )

    Scaffold(
        bottomBar = { TDBottomNavigationBar(
            navController = navController,
            currentScreen = Screens.TenantPaymentDashboardScreen.route
        ) },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(28.dp)) // move title+back arrow down

            // Back Arrow + Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 16.dp)
                    .clickable { navController.popBackStack() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.backarrow_ic),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp) // bigger back arrow
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Payment Receipt",
                    style = TextStyle(
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Card Container
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Payment Successful",
                        style = TextStyle(
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            color = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = propertyImage),
                            contentDescription = "Property Image",
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = amount,
                                style = TextStyle(
                                    fontFamily = Montserrat,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = propertyName,
                                style = TextStyle(
                                    fontFamily = Montserrat,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // Date & Payment Method
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text("Date", fontWeight = FontWeight.Bold, fontFamily = Montserrat)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(date, fontFamily = Montserrat, color = Color.Gray)
                        }
                        Column {
                            Text("Payment Method", fontWeight = FontWeight.Bold, fontFamily = Montserrat)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(paymentMethod, fontFamily = Montserrat, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(38.dp))

                    Text("Transaction ID", fontWeight = FontWeight.Bold, fontFamily = Montserrat)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(transactionId, fontFamily = Montserrat, color = Color.Gray)

                    Spacer(modifier = Modifier.height(42.dp))

                    // Download & Share Buttons
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                Toast.makeText(
                                    navController.context,
                                    "Downloading receipt...",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Download", fontFamily = Montserrat, color = Color.Black)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Button(
                            onClick = {
                                Toast.makeText(
                                    navController.context,
                                    "Sharing receipt...",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Share", fontFamily = Montserrat, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PaymentReceiptPreview() {
    val navController = rememberNavController()
    TenantPaymentReceiptScreen(navController)
}
