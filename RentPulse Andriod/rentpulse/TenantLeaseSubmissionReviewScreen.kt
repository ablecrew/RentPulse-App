package com.rentpulse

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rentpulse.navigation.Screens
import com.rentpulse.ui.screens.TDBottomNavigationBar


@Composable
fun TenantLeaseSubmissionReviewScreen(
    navController: NavController,
    apartmentName: String = "",
    leaseStartDate: String = "",
    leaseDocument: String = ""
) {
    Scaffold(
        bottomBar = { TDBottomNavigationBar(
            navController = navController,
            currentScreen = Screens.TenantLeaseDetailsScreen.route
        ) }
    ) { innerPadding ->
        Column(
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
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Back Arrow
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = "Lease Submission Review",
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = MontserratFont,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Review the details of your lease submission for",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontFamily = MontserratFont,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = apartmentName,
                        color = Color(0xFF007BFF),
                        fontSize = 16.sp,
                        fontFamily = MontserratFont,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Property Details
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.document_ic),
                            contentDescription = "Document Icon",
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Property",
                                fontWeight = FontWeight.Bold,
                                fontFamily = MontserratFont,
                                color = Color.Black
                            )
                            Text(
                                text = apartmentName,
                                fontFamily = MontserratFont,
                                color = Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Lease Start Date",
                        fontWeight = FontWeight.Bold,
                        fontFamily = MontserratFont,
                        color = Color.Black
                    )
                    Text(
                        text = leaseStartDate,
                        fontFamily = MontserratFont,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Document",
                        fontWeight = FontWeight.Bold,
                        fontFamily = MontserratFont,
                        color = Color.Black
                    )
                    Text(
                        text = leaseDocument,
                        fontFamily = MontserratFont,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { navController.navigate(Screens.TenantLeaseVerificationStatusScreen.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "Submit for Verification",
                            color = Color.White,
                            fontFamily = MontserratFont
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LeaseSubmissionReviewPreview() {
    val navController = rememberNavController()
    TenantLeaseSubmissionReviewScreen(
        navController,
        apartmentName = "Apartment name!",
        leaseStartDate = "Aug, 1, 2025",
        leaseDocument = "lease_agreement.pdf"
    )
}
