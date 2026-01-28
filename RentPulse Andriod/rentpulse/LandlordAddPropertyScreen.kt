package com.rentpulse

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.data.models.PropertyRequest
import com.rentpulse.navigation.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandlordAddPropertyScreen(
    navController: NavController,
    onBackClick: () -> Unit = {},
    onAddProperty: (String, String, String, String, String, List<Uri>) -> Unit = { _, _, _, _, _, _ -> },
    onNavigateHome: () -> Unit = {},
    onNavigateProperties: () -> Unit = {},
    onNavigatePayments: () -> Unit = {},
    onNavigateSupport: () -> Unit = {}
) {
    val context = LocalContext.current

    var address by remember { mutableStateOf("") }
    var propertyType by remember { mutableStateOf("") }
    var units by remember { mutableStateOf("") }
    var rentAmount by remember { mutableStateOf("") }

    var location by remember { mutableStateOf("Select Location") }
    var amenities by remember { mutableStateOf(listOf<String>()) }
    var selectedAmenities by remember { mutableStateOf(mutableListOf<String>()) }

    var selectedMedia by remember { mutableStateOf<List<Uri>>(emptyList()) }

    var isLoading by remember { mutableStateOf(false) }
    var locationExpanded by remember { mutableStateOf(false) }
    var amenitiesExpanded by remember { mutableStateOf(false) }

    var locationsList by remember { mutableStateOf(listOf<String>()) }
    var amenitiesList by remember { mutableStateOf(listOf<String>()) }

    val mediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris -> if (uris != null) selectedMedia = uris }

    // Fetch locations & amenities
    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val locResponse = RetrofitClient.api.getLocations()
                val amenityResponse = RetrofitClient.api.getAmenities()

                    locationsList = locResponse.data.values.flatten()
                    amenitiesList = amenityResponse.amenities

            } catch (e: Exception) {
                e.printStackTrace()
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Failed to load dropdown data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun uploadMediaAndSave() {
        if (address.isBlank() || propertyType.isBlank() || rentAmount.isBlank() || location == "Select Location") {
            Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        isLoading = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val uploadedUrls = mutableListOf<String>()
                for (uri in selectedMedia) {
                    val filePart = RetrofitClient.createFilePart(context, uri, "media")
                    val uploadResponse = RetrofitClient.api.uploadMedia(filePart)
                    uploadedUrls.add(uploadResponse.url)
                }

                val request = PropertyRequest(
                    property_name = address,
                    location = location,
                    property_type = propertyType,
                    units_total = units.toIntOrNull() ?: 1,
                    amenities = selectedAmenities.joinToString(", "),
                    rent_amount = rentAmount,
                    media_urls = uploadedUrls
                )

                val response = RetrofitClient.api.addProperty(request)
                isLoading = false

                CoroutineScope(Dispatchers.Main).launch {
                    if (response.success) {
                        Toast.makeText(context, "Property added successfully!", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screens.LandlordPropertiesScreen.route)
                    } else {
                        Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                isLoading = false
                e.printStackTrace()
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                selectedItem = "properties",
                onHomeClick = onNavigateHome,
                onPropertiesClick = onNavigateProperties,
                onPaymentsClick = onNavigatePayments,
                onSupportClick = onNavigateSupport
            )
        }
    ) { innerPadding ->
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
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onBackClick() }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Add Property",
                    color = Color.White,
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(16.dp)) {

                    // Location dropdown
                    ExposedDropdownMenuBox(
                        expanded = locationExpanded,
                        onExpandedChange = { locationExpanded = !locationExpanded }
                    ) {
                        OutlinedTextField(
                            value = location,
                            onValueChange = {},
                            readOnly = true,
                            textStyle = LocalTextStyle.current.copy(color = Color(0xFF007BFF)),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color(0xFF007BFF),
                                unfocusedIndicatorColor = Color(0xFF007BFF),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedTextColor = Color(0xFF007BFF),
                                unfocusedTextColor = Color(0xFF007BFF)
                            ),
                            label = { Text("Location", color = Color(0xFF007BFF)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = locationExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = locationExpanded,
                            onDismissRequest = { locationExpanded = false }
                        ) {
                            locationsList.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, color = Color(0xFF007BFF)) },
                                    onClick = {
                                        location = option
                                        locationExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Property type
                    OutlinedTextField(
                        value = propertyType,
                        onValueChange = { propertyType = it },
                        label = { Text("Property Type", color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Number of units
                    OutlinedTextField(
                        value = units,
                        onValueChange = { units = it },
                        label = { Text("Number of Units", color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Amenities dropdown
                    ExposedDropdownMenuBox(
                        expanded = amenitiesExpanded,
                        onExpandedChange = { amenitiesExpanded = !amenitiesExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedAmenities.joinToString(", "),
                            onValueChange = {},
                            readOnly = true,
                            textStyle = LocalTextStyle.current.copy(color = Color(0xFF007BFF)),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color(0xFF007BFF),
                                unfocusedIndicatorColor = Color(0xFF007BFF),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedTextColor = Color(0xFF007BFF),
                                unfocusedTextColor = Color(0xFF007BFF)
                            ),
                            label = { Text("Amenities", color = Color(0xFF007BFF)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = amenitiesExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = amenitiesExpanded,
                            onDismissRequest = { amenitiesExpanded = false }
                        ) {
                            amenitiesList.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, color = Color(0xFF007BFF)) },
                                    onClick = {
                                        if (selectedAmenities.contains(option)) selectedAmenities.remove(option)
                                        else selectedAmenities.add(option)
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Rent Amount
                    OutlinedTextField(
                        value = rentAmount,
                        onValueChange = { rentAmount = it },
                        label = { Text("Rent Amount", color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Media upload
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Upload Media", fontFamily = montserrat, fontSize = 14.sp, color = Color.Black)
                        Text("Add photos or videos of the property", color = Color.Gray, fontFamily = montserrat)
                        Spacer(modifier = Modifier.height(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.upload_ic),
                            contentDescription = "Upload",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { mediaLauncher.launch("*/*") }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { uploadMediaAndSave() },
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
                    ) {
                        Text(
                            if (isLoading) "Uploading..." else "Add Property",
                            color = Color.White,
                            fontFamily = montserrat
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddPropertyPreview() {
    val navController = rememberNavController()
    LandlordAddPropertyScreen(
        navController,
        onBackClick = {},
        onAddProperty = { _, _, _, _, _, _ -> },
        onNavigateHome = {},
        onNavigateProperties = {},
        onNavigatePayments = {},
        onNavigateSupport = {}
    )
}
