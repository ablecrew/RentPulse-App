package com.rentpulse.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.R
import com.rentpulse.navigation.Screens



@Composable
fun TenantLeaseDetailsScreen(
    navController: NavController,
    propertyImageRes: Int = R.drawable.apartment_ic,
    propertyName: String = "Apartment 2B",
    propertyAddress: String = "123 Main St",
    leaseStart: String = "July 31, 2025",
    leaseEnd: String = "",
    monthlyRent: String = "Ksh.193000",
    securityDeposit: String = "Ksh.64000"
) {
    val montserrat = FontFamily(Font(R.font.montserrat_regular))

    var noPetsAllowed by remember { mutableStateOf(false) }
    var rentDueOn1st by remember { mutableStateOf(false) }
    var tenantResponsible by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            TDBottomNavigationBar(
                navController = navController,
                currentScreen = Screens.TenantLeaseDetailsScreen.route
            )
        }
    ) { paddingValues ->
        Column(
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
                .padding(paddingValues)
        ) {
            // Top bar with back arrow
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
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
                    text = "Property Details",
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }

            // White card content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = propertyImageRes),
                        contentDescription = "Property Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color.LightGray, RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = propertyName,
                            fontFamily = montserrat,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text(
                            text = propertyAddress,
                            fontFamily = montserrat,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Lease Terms",
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                LeaseTermRow("Lease Start Date", leaseStart, montserrat,)
                LeaseTermRow("Lease End Date", leaseEnd, montserrat)
                LeaseTermRow("Monthly Rent", monthlyRent, montserrat)
                LeaseTermRow("Security Deposit", securityDeposit, montserrat)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Key Conditions",
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                CheckboxItem("No pets allowed", noPetsAllowed, { noPetsAllowed = it }, montserrat)
                CheckboxItem("Rent due on the 1st of each month", rentDueOn1st, { rentDueOn1st = it }, montserrat)
                CheckboxItem("Tenant responsible for utilities", tenantResponsible, { tenantResponsible = it }, montserrat)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navController.navigate(Screens.TenantLeaseSubmissionReviewScreen.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Proceed",
                        fontFamily = montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun LeaseTermRow(label: String, value: String, font: FontFamily) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontFamily = font, fontSize = 14.sp, color = Color.Black)
        Text(text = value, fontFamily = font, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
    }
}

@Composable
fun CheckboxItem(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit, font: FontFamily) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF007BFF))
        )
        Text(text = label, fontFamily = font, fontSize = 14.sp, color = Color.Black)
    }
}


@Preview(showBackground = true)
@Composable
fun TenantLeaseDetailsPreview() {
    val navController = rememberNavController()
    TenantLeaseDetailsScreen(
        navController = navController,
        propertyImageRes = R.drawable.apartment_ic,
        propertyName = "Apartment 2B",
        propertyAddress = "123 Main St, Apt 2B",
        leaseStart = "August 1, 2025",
        leaseEnd = "July 31, 2026",
        monthlyRent = "$ 1500",
        securityDeposit = "$ 500"
    )
}
