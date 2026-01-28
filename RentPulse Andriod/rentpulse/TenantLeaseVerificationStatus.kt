package com.rentpulse

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.navigation.Screens
import com.rentpulse.ui.screens.TDBottomNavigationBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



// ---------------------- VIEWMODEL ----------------------
class LeaseVerificationViewModel : ViewModel() {

    private val _leaseStatus = MutableStateFlow("Pending")
    val leaseStatus: StateFlow<String> = _leaseStatus

    init {
        simulateStatusUpdates()
    }

    private fun simulateStatusUpdates() {
        // This simulates your backend verification progress
        viewModelScope.launch {
            delay(2000)
            _leaseStatus.value = "In Progress"
            delay(3000)
            _leaseStatus.value = "Verified"
        }
    }
}

// ---------------------- SCREEN ----------------------
@Composable
fun TenantLeaseVerificationStatusScreen(
    navController: NavController,
    tenantName: String = "",
    tenantAddress: String = "",
    profileImage: Int = R.drawable.profile_icon,
    viewModel: LeaseVerificationViewModel = viewModel()
) {
    val leaseStatus by viewModel.leaseStatus.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
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
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
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
                    text = "Lease Verification Status",
                    color = Color.White,
                    fontFamily = MontserratFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            // Main Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Tenant Info
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = profileImage),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Tenant: $tenantName",
                                fontFamily = MontserratFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = tenantAddress,
                                fontFamily = MontserratFont,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Status Section
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Status\n${leaseStatus.uppercase()}",
                            fontFamily = MontserratFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    Text(
                        text = "We're currently verifying $tenantName's lease agreement. This process usually takes 1-2 business days. You'll receive a notification once the verification is complete.",
                        fontFamily = MontserratFont,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LeaseVerificationStatusPreview() {
    val navController = rememberNavController()
    TenantLeaseVerificationStatusScreen(
        navController = navController,
        tenantName = "Alex Johnson",
        tenantAddress = "123 Main St, Apt 4B",
        profileImage = R.drawable.profile_icon
    )
}

