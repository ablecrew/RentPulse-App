package com.rentpulse

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.material3.OutlinedTextField
import com.rentpulse.navigation.Screens
import com.rentpulse.ui.theme.Montserrat


@Composable
fun LandlordSettingsScreen(navController: NavController, isDarkTheme: MutableState<Boolean>) {
    val gradientBackground = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF007BFF),
            0.5f to Color(0xFF007BFF),
            0.55f to Color(0xFFBFDFFF),
            1.0f to Color(0xFFF5F5F5)
        )
    )

    var selectedLanguage by remember { mutableStateOf("English") }
    var selectedTheme by remember { mutableStateOf( "Light") }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                selectedItem = "support",
                onHomeClick = { navController.navigate(Screens.LandlordDashboardd.route) },
                onPropertiesClick = { navController.navigate(Screens.LandlordPropertiesScreen.route) },
                onPaymentsClick = { navController.navigate(Screens.LandlordPaymentInfoScreen.route) },
                onSupportClick = { navController.navigate(Screens.HelpCenterScreen.route) }
            )

        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(padding)
        ) {
            // Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { navController.popBackStack() }
                )
                Spacer(Modifier.width(12.dp))
                Text("Settings", fontFamily = Montserrat, fontSize = 22.sp, color = Color.White)
            }

            // Card
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile
                Image(
                    painter = painterResource(id = R.drawable.profile_icon),
                    contentDescription = "Landlord",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.height(8.dp))
                Text("Richardson", fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF007BFF))
                Text("Joined in 2024", fontFamily = Montserrat, fontSize = 14.sp, color = Color.Gray)

                Spacer(Modifier.height(24.dp))

                // Account Section
                Text("Account", fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black, modifier = Modifier.align(Alignment.Start))
                Spacer(Modifier.height(12.dp))

                SettingsItem("Personal Information", Icons.Default.Person) { navController.navigate(Screens.LandlordPersonalInfoScreen.route) }
                SettingsItem("Payment Methods", Icons.Default.CreditCard) { navController.navigate(Screens.LandlordPaymentMethodScreen.route) }
                SettingsItem("Notifications", Icons.Default.Notifications) { /* Navigate */ }
                SettingsItem("Security", Icons.Default.Lock) { /* Navigate */ }
                SettingsItem("Property Management", Icons.Default.Home) { navController.navigate(Screens.LandlordDashboardd.route) }

                Spacer(Modifier.height(24.dp))

                // Preferences
                Text("Preferences", fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black, modifier = Modifier.align(Alignment.Start))
                Spacer(Modifier.height(12.dp))

                // Language Dropdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Language", fontFamily = Montserrat, fontSize = 16.sp, color = Color.Black)
                    DropdownMenuBox(
                        options = listOf("English", "French", "Spanish", "Swahili"),
                        selectedOption = selectedLanguage,
                        onOptionSelected = { selectedLanguage = it }
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Theme Dropdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Theme", fontFamily = Montserrat, fontSize = 16.sp, color = Color.Black)
                    DropdownMenuBox(
                        options = listOf("Light", "Dark"),
                        selectedOption = selectedTheme,
                        onOptionSelected = {
                            selectedTheme = it
                            isDarkTheme.value = (it == "Dark") // 🔥 functional theme switch
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsItem(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = title, tint = Color(0xFF007BFF))
            Spacer(Modifier.width(12.dp))
            Text(title, fontFamily = Montserrat, fontSize = 16.sp, color = Color.Black)
        }
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next", tint = Color.Gray)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(
    options: List<String>,
    selectedOption: String,
    label: String = "",
    selected: String = "",
    onSelect: (String) -> Unit = {},
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = Montserrat,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .width(120.dp)
                .height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF007BFF),
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color(0xFF007BFF),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            option,
                            fontFamily = Montserrat,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LandlordSettingsPreview() {
    val navController = rememberNavController()
    val isDarkTheme = remember { mutableStateOf(false) }
    LandlordSettingsScreen(navController, isDarkTheme = isDarkTheme)
}
