package com.rentpulse

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.navigation.Screens
import com.rentpulse.ui.theme.Montserrat


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandlordSupportScreen(navController: NavHostController) {
    var selectedLanguage by remember { mutableStateOf("English") }
    var selectedTheme by remember { mutableStateOf("Light") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Support",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp, // larger title
                        color = Color.White,
                        modifier = Modifier.padding(top = 6.dp) // push down a bit
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(top = 6.dp) // push arrow down a bit
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp) // slightly bigger arrow
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                modifier = Modifier.height(64.dp) // taller top bar
            )
        },
        bottomBar = {
            com.rentpulse.BottomNavigationBar(
                navController = navController,
                selectedItem = "support",
                onHomeClick = { navController.navigate(Screens.LandlordDashboardd.route) },
                onPropertiesClick = { navController.navigate(Screens.LandlordPropertiesScreen.route) },
                onPaymentsClick = { navController.navigate(Screens.LandlordPaymentInfoScreen.route) },
                onSupportClick = { navController.navigate(Screens.HelpCenterScreen.route) }
            )
        }
    ) { paddingValues ->
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
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SupportItem("Help Centre") { navController.navigate(Screens.HelpCenterScreen.route) }
                    Divider()
                    SupportItem("Contact Us") { /* Navigate Contact Us */ }
                    Divider()
                    SupportItem("Terms of Service") { navController.navigate(Screens.TermsOfServiceScreen.route) }
                    Divider()
                    SupportItem("Security") { /* Navigate Security */ }

                    Spacer(Modifier.height(20.dp))

                    Text(
                        "Preferences",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    PreferenceItem(
                        label = "Language",
                        selected = selectedLanguage,
                        options = listOf("English", "French", "Spanish", "Swahili"),
                        onSelect = { selectedLanguage = it }
                    )

                    Spacer(Modifier.height(16.dp))

                    PreferenceItem(
                        label = "Theme",
                        selected = selectedTheme,
                        options = listOf("Light", "Dark"),
                        onSelect = { selectedTheme = it }
                    )
                }
            }
        }
    }
}

@Composable
fun SupportItem(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontFamily = Montserrat, fontSize = 18.sp, color = Color.Black) // bigger text
        Icon(Icons.Outlined.KeyboardArrowRight, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(24.dp))
    }
}

@Composable
fun PreferenceItem(
    label: String,
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            fontFamily = Montserrat,
            fontSize = 18.sp,
            color = Color.Black
        )
        DropdownSelector(selected, options, onSelect)
    }
}

@Composable
fun DropdownSelector(selected: String, options: List<String>, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(selected, fontFamily = Montserrat, fontSize = 16.sp) // bigger text for clarity
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach {
                DropdownMenuItem(
                    text = {
                        Text(it, fontFamily = Montserrat, fontSize = 16.sp)
                    },
                    onClick = {
                        onSelect(it)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewLandlordSupportScreen() {
    val navController = rememberNavController()
    LandlordSupportScreen(navController)
}
