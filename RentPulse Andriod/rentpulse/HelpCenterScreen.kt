package com.rentpulse.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.R
import com.rentpulse.navigation.Screens

val montserrat = FontFamily(Font(R.font.montserrat_regular))

@Composable
fun HelpCenterScreen(navController: NavController) {
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            com.rentpulse.BottomNavigationBar(
                navController = navController,
                selectedItem = "support",
                onHomeClick = { navController.navigate(Screens.LandlordDashboardd.route) },
                onPropertiesClick = { navController.navigate(Screens.LandlordPropertiesScreen.route) },
                onPaymentsClick = { navController.navigate(Screens.LandlordPaymentInfoScreen.route) },
                onSupportClick = { navController.navigate(Screens.HelpCenterScreen.route) }
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
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
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    text = "Help Center",
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // FAQ Section
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Frequently Asked Questions",
                        fontWeight = FontWeight.Bold,
                        fontFamily = montserrat,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    FAQItem(
                        question = "How do I add a new tenant?",
                        answer = "Go to the Tenant Directory, click 'Add Tenant', then fill in the tenant's details and save."
                    )
                    FAQItem(
                        question = "How do I set up recurring payments?",
                        answer = "Navigate to Payments, choose a tenant, and enable recurring payments with your preferred method."
                    )
                    FAQItem(
                        question = "What payment methods are accepted?",
                        answer = "We support M-Pesa, credit/debit cards, and bank transfers."
                    )
                    FAQItem(
                        question = "How do I upload lease documents?",
                        answer = "Head to the Lease section, select a property, and upload your PDF or image lease files."
                    )
                    FAQItem(
                        question = "Can tenants see their payment history?",
                        answer = "Yes. Tenants can log in and view all past and upcoming payments in their dashboard."
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Contact Support Section
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Contact Support",
                        fontWeight = FontWeight.Bold,
                        fontFamily = montserrat,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ContactItem(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = "support@rentpulse.com"
                    ) {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:support@rentpulse.com")
                        }
                        context.startActivity(intent)
                    }

                    ContactItem(
                        icon = Icons.Default.Phone,
                        label = "Phone",
                        value = "+254707528980"
                    ) {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:+254707528980")
                        }
                        context.startActivity(intent)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Resources Section
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Resources",
                        fontWeight = FontWeight.Bold,
                        fontFamily = montserrat,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ResourceItem(
                        icon = Icons.Default.MenuBook,
                        label = "Getting Started Guide"
                    ) {
                        navController.navigate("gettingStarted")
                    }

                    ResourceItem(
                        icon = Icons.Default.VideoLibrary,
                        label = "Video Tutorials"
                    ) {
                        navController.navigate("videoTutorials")
                    }
                }
            }
        }
    }
}

@Composable
fun FAQItem(question: String, answer: String) {
    var expanded = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded.value = !expanded.value }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = question,
                    fontFamily = montserrat,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expand",
                    tint = Color.Black
                )
            }
            if (expanded.value) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = answer,
                    fontFamily = montserrat,
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun ContactItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF007BFF), //
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontFamily = montserrat,
                color = Color.Black
            )
            Text(
                text = value,
                color = Color.DarkGray,
                fontFamily = montserrat
            )
        }
    }
}

@Composable
fun ResourceItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF007BFF),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            fontFamily = montserrat,
            color = Color.Black
        )
    }
}


@Preview(showBackground = true)
@Composable
fun HelpCenterPreview() {
    val navController = rememberNavController()
    HelpCenterScreen(navController)
}
