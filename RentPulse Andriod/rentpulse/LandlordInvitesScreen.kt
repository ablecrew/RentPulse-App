package com.rentpulse.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.rentpulse.R
import com.rentpulse.navigation.Screens

data class Property(
    val id: Int,
    val title: String,
    val address: String,
    val details: List<String>,
    val imageUrl: String
)

data class TenantRequest(
    val id: Int,
    val name: String,
    val address: String,
    val requestedDate: String,
    val avatarUrl: String
)

@Composable
fun LandlordInvitesScreen(navController: NavController) {
    val properties = listOf(
        Property(
            id = 1,
            title = "Cozy Studio Apartment",
            address = "123 Main St, Apt 2B",
            details = listOf("Pet Friendly", "Parking", "1 Bedroom", "Furnished"),
            imageUrl = "https://via.placeholder.com/300x200.png?text=Studio+Apartment"
        ),
        Property(
            id = 2,
            title = "Spacious Family Home",
            address = "456 Oak Ave, House",
            details = listOf("Pet Friendly", "Parking", "4 Bedroom", "Play Ground"),
            imageUrl = "https://via.placeholder.com/300x200.png?text=Family+Home"
        )
    )

    val tenantRequests = remember {
        mutableStateListOf(
            TenantRequest(
                id = 1,
                name = "Liam Carter",
                address = "123 Main St, Apt 2B",
                requestedDate = "07/15/2025",
                avatarUrl = "https://via.placeholder.com/100.png?text=L"
            ),
            TenantRequest(
                id = 2,
                name = "Ben Frank",
                address = "",
                requestedDate = "",
                avatarUrl = "https://via.placeholder.com/100.png?text=B"
            ),
            TenantRequest(
                id = 3,
                name = "Sophia Bennett",
                address = "456 Oak Ave, House",
                requestedDate = "07/14/2025",
                avatarUrl = "https://via.placeholder.com/100.png?text=S"
            )
        )
    }

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
    ) {
        // Top bar with back
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Invites",
                fontFamily = Montserrat,
                fontSize = 22.sp,
                color = Color.White
            )
        }

        // Card content
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .weight(1f) // fill space above bottom nav
                .padding(8.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(12.dp)
            ) {
                item {
                    Text(
                        text = "Available Properties",
                        fontFamily = Montserrat,
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(properties) { property ->
                    PropertyCard(property)
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Pending Requests",
                        fontFamily = Montserrat,
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(tenantRequests) { tenant ->
                    TenantRequestCard(
                        tenant = tenant,
                        onAccept = { tenantRequests.remove(tenant) }
                    )
                }
            }
        }

        // Bottom navigation (inserted directly)
        com.rentpulse.BottomNavigationBar(
            navController = navController,
            selectedItem = "properties",
            onHomeClick = { navController.navigate(Screens.LandlordDashboardd.route) },
            onPropertiesClick = { navController.navigate(Screens.LandlordPropertiesScreen.route) },
            onPaymentsClick = { navController.navigate(Screens.LandlordPaymentInfoScreen.route) },
            onSupportClick = { navController.navigate(Screens.HelpCenterScreen.route) }
        )
    }
}

@Composable
fun PropertyCard(property: Property) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        AsyncImage(
            model = property.imageUrl,
            contentDescription = property.title,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = property.title,
                fontFamily = Montserrat,
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = property.address,
                fontFamily = Montserrat,
                fontSize = 14.sp,
                color = Color.Black
            )
            property.details.forEach {
                Text(
                    text = it,
                    fontFamily = Montserrat,
                    fontSize = 13.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun TenantRequestCard(tenant: TenantRequest, onAccept: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = tenant.avatarUrl,
            contentDescription = tenant.name,
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tenant.name,
                fontFamily = Montserrat,
                fontSize = 15.sp,
                color = Color.Black
            )
            if (tenant.address.isNotEmpty()) {
                Text(
                    text = tenant.address,
                    fontFamily = Montserrat,
                    fontSize = 13.sp,
                    color = Color.Black
                )
            }
            if (tenant.requestedDate.isNotEmpty()) {
                Text(
                    text = "Requested: ${tenant.requestedDate}",
                    fontFamily = Montserrat,
                    fontSize = 12.sp,
                    color = Color.Black
                )
            }
        }
        Button(
            onClick = { onAccept() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Accept", fontFamily = Montserrat, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun LIBottomNavigationBar(navController: NavController) {
    val items = listOf("Home", "Properties", "Payments", "Support")
    val icons = listOf(
        painterResource(id = R.drawable.home_ic),
        painterResource(id = R.drawable.properties_ic),
        painterResource(id = R.drawable.payments_ic),    
        painterResource(id = R.drawable.support_ic)
    )

    var selectedItem by remember { mutableStateOf("Properties") }

    NavigationBar(containerColor = Color.White) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == item,
                onClick = {
                    selectedItem = item
                    // navController.navigate("route_$item") // hook up routes if needed
                },
                icon = {
                    Icon(
                        painter = icons[index],
                        contentDescription = item,
                        modifier = Modifier.size(24.dp),
                        tint = if (selectedItem == item) Color(0xFF007BFF) else Color.Black
                    )
                },
                label = {
                    Text(
                        text = item,
                        fontFamily = Montserrat,
                        fontSize = 12.sp,
                        color = if (selectedItem == item) Color(0xFF007BFF) else Color.Black
                    )
                }
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun LandlordInvitesPreview() {
    val navController = rememberNavController()
    LandlordInvitesScreen(navController)
}
