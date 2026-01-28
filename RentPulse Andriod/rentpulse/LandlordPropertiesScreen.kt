package com.rentpulse

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.navigation.Screens
import com.rentpulse.ui.theme.Montserrat


// Renamed to avoid redeclaration/clashes
data class LandlordPropertyItem(
    val imageRes: Int,
    val name: String,
    val address: String,
    val occupied: List<String>,
    val vacant: List<String>
)

@Composable
fun LandlordPropertiesScreen(
    navController: NavController,
    onBack: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {

    val properties: List<LandlordPropertyItem> = listOf(
        LandlordPropertyItem(
            R.drawable.property_1, "Parkside Plaza", "488 Cak St, Kilimani",
            occupied = listOf("Apt 1A"),
            vacant = listOf("Apt 8H2")
        ),
        LandlordPropertyItem(
            R.drawable.property_2, "Grandview Apartments", "Rio St, Langata Rd",
            occupied = listOf("F2 3"),
            vacant = listOf("F5 3b, 6, 9, 11, 15")
        ),
        LandlordPropertyItem(
            R.drawable.property_3, "Prinston Villa", "122, Mombasa Rd",
            occupied = listOf("1st & 2nd Floors"),
            vacant = listOf("3rd, 4th & 5th Floors")
        ),
        LandlordPropertyItem(
            R.drawable.property_4, "Oak Residency", "5th Avenue, Westlands",
            occupied = listOf("B2 1"),
            vacant = listOf("B2 2, 3, 5")
        ),
        LandlordPropertyItem(
            R.drawable.property_5, "Maple Heights", "Ngong Rd, Adams",
            occupied = listOf("Block A 101, 102"),
            vacant = listOf("Block A 201, 202")
        ),
        LandlordPropertyItem(
            R.drawable.cozyapartment, "Skyline Towers", "Uhuru Highway",
            occupied = listOf("Suite 401"),
            vacant = listOf("Suite 402, 403, 405")
        )
    )

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
        // Top bar
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
                    .clickable { onBack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Properties",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.White
            )
        }

        Spacer(Modifier.height(8.dp))

        // Add Property
        Button(
            onClick = { navController.navigate(Screens.LandlordAddPropertyScreen.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text(
                text = "+ Add Property",
                color = Color(0xFF007BFF),
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(10.dp))

        // List
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .weight(1f)
        ) {
            items(
                items = properties,
                key = { it.name } // stable key
            ) { p: LandlordPropertyItem ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = backgroundColor),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = p.imageRes),
                            contentDescription = p.name,
                            modifier = Modifier
                                .size(90.dp)
                                .padding(end = 10.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = p.name,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = p.address,
                                fontFamily = Montserrat,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            // Occupied
                            p.occupied.forEach { occ ->
                                Text(
                                    text = "$occ - Occupied",
                                    fontFamily = Montserrat,
                                    fontSize = 13.sp,
                                    color = Color(0xFF26A269) // greenish
                                )
                            }
                            // Vacant
                            p.vacant.forEach { vac ->
                                Text(
                                    text = "$vac - Vacant",
                                    fontFamily = Montserrat,
                                    fontSize = 13.sp,
                                    color = Color(0xFFD14343),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom nav
        BottomNavigationBar(
            navController = navController,
            selectedItem = "home",
            onHomeClick = { navController.navigate(Screens.LandlordDashboardd.route) },
            onPropertiesClick = { navController.navigate(Screens.LandlordPropertiesScreen.route) },
            onPaymentsClick = { navController.navigate(Screens.LandlordPaymentInfoScreen.route) },
            onSupportClick = { navController.navigate(Screens.HelpCenterScreen.route) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLandlordPropertyScreen() {
    val navController = rememberNavController()
    LandlordPropertiesScreen(
        navController,
        onBack = {},
        onNavigate = {}
    )
}
