package com.rentpulse

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import com.rentpulse.navigation.Screens
import com.rentpulse.ui.screens.TDBottomNavigationBar


// Sample Property Model
data class Property(
    val type: String,
    val title: String,
    val details: String,
    val imageRes: Int
)

@Composable
fun TenantFindHomeScreen(navController: NavController) {
    val gradientBackground = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF007BFF),
            0.5f to Color(0xFF007BFF),
            0.55f to Color(0xFFBFDFFF),
            1.0f to Color(0xFFF5F5F5)
        )
    )

    var searchQuery by remember { mutableStateOf("") }
    var locationExpanded by remember { mutableStateOf(false) }
    var priceExpanded by remember { mutableStateOf(false) }
    var amenitiesExpanded by remember { mutableStateOf(false) }

    val properties = listOf(
        Property("Apartment", "Cozy Studio, Downtown", "1 bed. 1 bath. 600 sq ft", R.drawable.cozyapartment),
        Property("House", "Spacious 3-Bedroom House", "3 beds. 2 baths. 1500 sq ft", R.drawable.house4),
        Property("Apartment", "Luxury Penthouse with city view", "2 beds. 2 baths. 1200 sq ft", R.drawable.apartment3)
    )

    Scaffold(
        bottomBar = {
            TDBottomNavigationBar(
                navController = navController,
                currentScreen = Screens.TenantLeaseDetailsScreen.route
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Navigate to AI Contact Support
                navController.navigate("support")
            }, containerColor = Color(0xFFBFDFFF)) {
                Text("AI", fontFamily = montserrat, fontSize = 16.sp, color = Color.Black)
            }
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
                Text("Find a Home", fontFamily = montserrat, fontSize = 20.sp, color = Color.White)
            }

            // Search Field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        "Search by city, address or zip code",
                        fontFamily = montserrat,
                        color = Color.Gray
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(14.dp)),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    cursorColor = Color(0xFF007BFF),
                    focusedIndicatorColor = Color(0xFF007BFF),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )




            Spacer(Modifier.height(12.dp))

            // Dropdown Filters
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DropdownFilter("Location", locationExpanded, { locationExpanded = !locationExpanded })
                DropdownFilter("Price", priceExpanded, { priceExpanded = !priceExpanded })
                DropdownFilter("Amenities", amenitiesExpanded, { amenitiesExpanded = !amenitiesExpanded })
            }

            Spacer(Modifier.height(16.dp))

            // Property List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(properties) { property ->
                    PropertyCard(property)
                }

                // Quick Actions
                item {
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate("quick_actions") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                    ) {
                        Text("Quick Actions", fontFamily = montserrat, fontWeight = Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownFilter(title: String, expanded: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(title, fontFamily = montserrat, fontSize = 14.sp, color = Color.Black)
    }
}

@Composable
fun PropertyCard(property: Property) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(property.type, fontFamily = montserrat, fontSize = 12.sp, color = Color.Gray)
            Text(property.title, fontFamily = montserrat, fontSize = 16.sp, color = Color.Black)
            Text(property.details, fontFamily = montserrat, fontSize = 14.sp, color = Color.Black)
        }
        Spacer(Modifier.width(8.dp))
        Image(
            painter = painterResource(id = property.imageRes),
            contentDescription = property.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}


@Preview(showBackground = true)
@Composable
fun TenantFindHomePreview() {
    val navController = rememberNavController()
    TenantFindHomeScreen(navController)
}
