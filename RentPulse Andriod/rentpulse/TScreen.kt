package com.rentpulse

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun TScreen(navController: NavController) {
    // Selected tab state
    val selectedRangeState = remember { mutableStateOf(6) } // 6 months selected by default
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF0D6EFD), Color(0xFFEAF2FF)),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar()
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradientBrush)
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                // ✅ STATIC TOP BAR (OUTSIDE SCROLL)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFBFD9FF),
                            shape = RoundedCornerShape(topStart = 22.dp, bottomEnd = 22.dp)
                        )
                        .padding(top = 24.dp, bottom = 14.dp, start = 12.dp, end = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // RIGHT SIDE -> Profile + Welcome
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Icon(
                            painter = painterResource(id = R.drawable.profile_icon),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            tint = Color.White
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "Welcome, Dande",
                            fontFamily = montserrat,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                    }

                    // LEFT SIDE -> Notifications + Menu
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        val notificationsCount = 0
                        BadgedBox(
                            badge = {
                                if (notificationsCount > 0) {
                                    Badge(containerColor = Color.Red, contentColor = Color.White) {
                                        Text(notificationsCount.toString(), fontSize = 10.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.Black,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(18.dp))

                        IconButton(onClick = { /* open menu later */ }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu",
                                tint = Color.Black
                            )
                        }
                    }
                }

                // ✅ SCROLLABLE CONTENT BELOW TOP BAR
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                ) {

                    Spacer(modifier = Modifier.height(16.dp))

                    // 🔹 Summary Cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SummaryCard("Total Due", "Ksh. 36,000")
                        SummaryCard("Upcoming Bills", "2")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 🔹 My Rent Section
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "My Rent",
                                    fontFamily = montserrat,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black
                                )

                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = Color(0x1AFF0000),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "• Overdue",
                                        color = Color.Red,
                                        fontFamily = montserrat,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }


                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Your monthly rent payment is past its due date.",
                                fontFamily = montserrat,
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            // Buttons Centered
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {},
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD))
                                ) {
                                    Text("Pay Now", fontFamily = montserrat, color = Color.White)
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                TextButton(onClick = {}) {
                                    Text("View History", color = Color(0xFF0D6EFD), fontFamily = montserrat)
                                }
                            }

                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ✅ everything else stays EXACTLY as your file had…

                    // 🔹 Utilities Section
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Utilities",
                                fontFamily = montserrat,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            UtilityRow("Water Bill", "2,500")
                            Spacer(modifier = Modifier.height(8.dp))
                            Divider(color = Color.LightGray, thickness = 1.dp)

                            UtilityRow("Electricity Bill", "4,655")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 🔹 Maintenance Section
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Maintenance",
                                fontFamily = montserrat,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // background blue 10% opacity container
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = Color(0x1A007BFF), shape = RoundedCornerShape(12.dp))
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Leaky Faucet", fontFamily = montserrat, fontWeight = FontWeight.Medium, color = Color.Black)
                                    Text(
                                        "In Progress",
                                        color = Color(0xFFFFA726),
                                        fontFamily = montserrat,
                                        fontSize = 13.sp
                                    )
                                }
                                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Button(
                                    onClick = {},
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD)),
                                    modifier = Modifier.width(220.dp) // reduced width
                                ) {
                                    Text("Submit a New Request", fontFamily = montserrat, color = Color.White)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 🔹 Inbox & Alerts
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                "Inbox & Alerts",
                                fontFamily = montserrat,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = Color(0x1A007BFF), // 10% opacity blue bg container
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Row(verticalAlignment = Alignment.CenterVertically) {

                                    Icon(
                                        painter = painterResource(id = R.drawable.message_ic),
                                        contentDescription = "Messages Icon",
                                        modifier = Modifier.size(26.dp),
                                        tint = Color.Unspecified
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Column {
                                        Text("2 Unread Messages", fontFamily = montserrat, fontWeight = FontWeight.Medium)
                                        Text("From Management", fontFamily = montserrat, color = Color.Gray, fontSize = 13.sp)
                                    }
                                }

                                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
                            }
                        }
                    }



                    Spacer(modifier = Modifier.height(16.dp))

                    // 🔹 Analytics Section
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                "Analytics",
                                fontFamily = montserrat,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                "Ksh. 360,000",
                                fontFamily = montserrat,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 20.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Full 12 months data
                            val months12 = listOf("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec")
                            val values12 = listOf(50000f, 60000f, 52000f, 70000f, 90000f, 100000f, 95000f, 85000f, 110000f, 105000f, 98000f, 120000f)

                            // Displayed data based on selected tab
                            val displayMonths: List<String>
                            val displayValues: List<Float>
                            when (selectedRangeState.value) {
                                3 -> {
                                    displayMonths = months12.subList(0,3)
                                    displayValues = values12.subList(0,3)
                                }
                                6 -> {
                                    displayMonths = months12.subList(0,6)
                                    displayValues = values12.subList(0,6)
                                }
                                else -> {
                                    displayMonths = months12
                                    displayValues = values12
                                }
                            }

                            // Chart
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .padding(horizontal = 6.dp)
                            ) {

                                val path = androidx.compose.ui.graphics.Path()
                                val widthStep = size.width / (displayValues.size - 1)
                                val maxValue = displayValues.maxOrNull() ?: 0f
                                val minValue = displayValues.minOrNull() ?: 0f
                                val heightScale = if (maxValue - minValue == 0f) 1f else size.height / (maxValue - minValue)

                                val points = displayValues.mapIndexed { index, value ->
                                    Offset(
                                        x = widthStep * index,
                                        y = size.height - (value - minValue) * heightScale
                                    )
                                }

                                path.moveTo(points.first().x, points.first().y)

                                for (i in 1 until points.size) {
                                    val midX = (points[i].x + points[i - 1].x) / 2
                                    val midY = (points[i].y + points[i - 1].y) / 2
                                    path.quadraticBezierTo(points[i - 1].x, points[i - 1].y, midX, midY)
                                }

                                drawPath(
                                    path = path,
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF86B6FF), Color(0xFF0D6EFD))
                                    ),
                                    style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Months Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                displayMonths.forEach {
                                    Text(it, fontFamily = montserrat, fontSize = 12.sp, color = Color.Gray)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Tabs (3 months | 6 months | 1 year)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF1F1F1), RoundedCornerShape(12.dp))
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                listOf(3,6,12).forEach { range ->
                                    val label = when(range){ 3 -> "3 Months"; 6 -> "6 Months"; else -> "1 Year" }
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 4.dp)
                                            .background(if(selectedRangeState.value==range) Color.White else Color.Transparent, RoundedCornerShape(8.dp))
                                            .clickable { selectedRangeState.value = range }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            label,
                                            fontFamily = montserrat,
                                            fontSize = 13.sp,
                                            color = if(selectedRangeState.value==range) Color.Black else Color.Gray,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    // 🔹 RentPulse Ad Image
                    Image(
                        painter = painterResource(id = R.drawable.tenant_ad),
                        contentDescription = "RentPulse Promotion",
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .align(Alignment.CenterHorizontally)
                            .clip(RoundedCornerShape(16.dp))
                            .height(180.dp)
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .padding(bottom = 8.dp),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(90.dp))
                }
            }
        }
    }
}


// 🔹 Reusable Components

    @Composable
    fun RowScope.SummaryCard(title: String, value: String) {
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(title, fontFamily = montserrat, color = Color.Gray, fontSize = 13.sp)
                Text(
                    value,
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 18.sp
                )
            }
        }
    }


@Composable
fun UtilityRow(title: String, amount: String) {

    val iconRes = when (title) {
        "Water Bill" -> R.drawable.ic_water
        "Electricity Bill" -> R.drawable.ic_drop
        else -> R.drawable.ic_drop   // fallback default
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = Color.Unspecified   // <-- FIX
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(title, fontFamily = montserrat, color = Color.Black)
        }

        Button(
            onClick = {},
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEAF2FF))
        ) {
            Text("Pay $amount", fontFamily = montserrat, color = Color(0xFF0D6EFD))
        }
    }
}


@Composable
fun BottomNavigationBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 8.dp,
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .width(260.dp)
                .height(60.dp)
        ) {
            NavigationBarItem(
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(1.dp) // small spacing between icon and text
                    ) {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Color(0xFF0D6EFD)
                        )
                        Text("Home", fontFamily = montserrat, fontSize = 12.sp)
                    }
                },
                selected = true,
                onClick = {}
            )
            NavigationBarItem(
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        Icon(Icons.Default.Payments, contentDescription = "Payments")
                        Text("Payments", fontFamily = montserrat, fontSize = 12.sp)
                    }
                },
                selected = false,
                onClick = {}
            )
            NavigationBarItem(
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        Icon(Icons.Default.Article, contentDescription = "Lease")
                        Text("Lease", fontFamily = montserrat, fontSize = 12.sp)
                    }
                },
                selected = false,
                onClick = {}
            )
        }
    }
}




@Preview(
    name = "Pixel 7 Preview",
    showSystemUi = true,
    showBackground = true,
    device = Devices.PIXEL_7
)
@Composable
fun TenantDashboard_Pixel7_Preview() {
    val navController = rememberNavController()
    TScreen(navController = navController)
}
