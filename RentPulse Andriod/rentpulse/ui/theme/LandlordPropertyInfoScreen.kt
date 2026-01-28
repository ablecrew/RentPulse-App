package com.rentpulse.ui.theme

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.data.models.PropertyRequest
import com.rentpulse.data.models.ApiResponse
import com.rentpulse.navigation.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandlordPropertyInfoScreen(
    navController: NavController,
    onBack: () -> Unit = {},
    onCancel: () -> Unit = {},
    onHelpCenter: () -> Unit = {}
) {

    var propertyName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Select Location") }
    var propertyType by remember { mutableStateOf("Select Property Type") }
    var unitsTotal by remember { mutableStateOf("1") }
    var isLoading by remember { mutableStateOf(false) }

    var locationExpanded by remember { mutableStateOf(false) }
    var propertyTypeExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val locationOptions = listOf("Nairobi", "Mombasa", "Kisumu", "Nakuru", "Eldoret")
    val propertyTypeOptions = listOf("Apartment", "Bungalow", "Bedsitter", "Maisonette")

    // API call to add property
    fun saveProperty() {
        if (propertyName.isBlank() || location == "Select Location" || propertyType == "Select Property Type") {
            Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        isLoading = true
        val request = PropertyRequest(
            property_name = propertyName,
            location = location,
            property_type = propertyType,
            units_total = unitsTotal.toIntOrNull() ?: 1
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: ApiResponse = RetrofitClient.api.addProperty(request)
                isLoading = false
                if (response.success) {
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "Property saved successfully!", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screens.LandlordPaymentInfoScreen.route)
                    }
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "Error: ${response.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                isLoading = false
                e.printStackTrace()
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Network error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Box(
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
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Back Arrow
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onBack() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Property Information",
                fontFamily = Montserrat,
                fontSize = 20.sp,
                fontWeight = Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            // White container
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    OutlinedTextField(
                        value = propertyName,
                        onValueChange = { propertyName = it },
                        label = { Text("Property Name", fontFamily = Montserrat, color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(fontFamily = Montserrat, color = Color.Black),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Location dropdown
                    ExposedDropdownMenuBox(
                        expanded = locationExpanded,
                        onExpandedChange = { locationExpanded = !locationExpanded }
                    ) {
                        OutlinedTextField(
                            value = location,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Location", fontFamily = Montserrat, color = Color.Black) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = locationExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = TextStyle(fontFamily = Montserrat, color = Color.Black)
                        )
                        ExposedDropdownMenu(
                            expanded = locationExpanded,
                            onDismissRequest = { locationExpanded = false }
                        ) {
                            locationOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, fontFamily = Montserrat) },
                                    onClick = {
                                        location = option
                                        locationExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Property Type dropdown
                    ExposedDropdownMenuBox(
                        expanded = propertyTypeExpanded,
                        onExpandedChange = { propertyTypeExpanded = !propertyTypeExpanded }
                    ) {
                        OutlinedTextField(
                            value = propertyType,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Property Type", fontFamily = Montserrat, color = Color.Black) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = propertyTypeExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = TextStyle(fontFamily = Montserrat, color = Color.Black)
                        )
                        ExposedDropdownMenu(
                            expanded = propertyTypeExpanded,
                            onDismissRequest = { propertyTypeExpanded = false }
                        ) {
                            propertyTypeOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, fontFamily = Montserrat) },
                                    onClick = {
                                        propertyType = option
                                        propertyTypeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = unitsTotal,
                        onValueChange = { unitsTotal = it },
                        label = { Text("Total Units", fontFamily = Montserrat, color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(fontFamily = Montserrat, color = Color.Black),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { saveProperty() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
                    ) {
                        Text(if (isLoading) "Saving..." else "Save", color = Color.White, fontFamily = Montserrat)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Cancel",
                        fontFamily = Montserrat,
                        color = Color(0xFF007BFF),
                        modifier = Modifier.clickable { onCancel() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Help Center",
                fontFamily = Montserrat,
                color = Color(0xFF007BFF),
                modifier = Modifier.clickable { onHelpCenter() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LandlordPropertyInfoPreview() {
    val navController = rememberNavController()
    LandlordPropertyInfoScreen(
        navController,
        onBack = { navController.navigate(Screens.LandlordPersonalInfoScreen.route) },
        onCancel = { navController.navigate(Screens.WelcomeScreen.route) },
        onHelpCenter = { navController.navigate(Screens.HelpCenterScreen.route) }
    )
}
