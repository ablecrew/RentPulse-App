package com.rentpulse

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.rentpulse.ui.theme.Montserrat
import com.rentpulse.ui.theme.RentPulseTheme
import java.text.NumberFormat
import java.util.*

// --- Data models for sample usage ---
data class PaymentOverview(
    val title: String,
    val collected: Double,
    val target: Double,
    val overdueCount: Int,
    val periodLabel: String = "This Month"
)

data class RecentPayment(
    val id: String,
    val payerName: String,
    val unit: String,
    val amount: Double,
    val status: PaymentStatus
)

enum class PaymentStatus { PAID, PARTIAL, PENDING }

data class TenantActivity(
    val id: String,
    val title: String,
    val count: Int,
    val icon: @Composable () -> Unit,
    val onClick: () -> Unit = {}
)

// --- Theme constants (adjust according to your app theme) ---
val gradientBackground = Brush.verticalGradient(
    colorStops = arrayOf(
        0.0f to Color(0xFF007BFF),
        0.5f to Color(0xFF007BFF),
        0.55f to Color(0xFFBFDFFF),
        1.0f to Color(0xFFF5F5F5)
    )
)
private val CardCorner = RoundedCornerShape(16.dp)
private val BottomNavCorner = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)


// --- Top-level composable screen ---
@Composable
fun RentPulseDashboardScreen(
    navController: NavHostController,
    paymentsOverview: PaymentOverview,
    recentPayments: List<RecentPayment>,
    onTenantActivityClick: (TenantActivity) -> Unit = {},
    onReconcileClick: () -> Unit = {},
    onPredictorClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val bottomItems = listOf(
        DScreenBottomNavItem("home", "Home", Icons.Default.Home),
        DScreenBottomNavItem("properties", "Properties", Icons.Default.Apartment),
        DScreenBottomNavItem("payments", "Payments", Icons.Default.CreditCard),
        DScreenBottomNavItem("support", "Support", Icons.Default.SupportAgent)
    )

    Scaffold(
        topBar = { TopBar() },
        bottomBar = {
            BottomNavigationBar(navController = navController, items = bottomItems)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* AI agent action */ }) {
                Icon(Icons.Default.SmartToy, contentDescription = "AI Agent")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        modifier = modifier
    ) { innerPadding ->
        // Use a LazyColumn so the entire screen is scrollable efficiently
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)   // <<<<<<<<<<<<<< ADDED HERE
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        )
        {
            item { Spacer(modifier = Modifier.height(12.dp)) }

            // Top quick actions row
            item {
                QuickActionRow()
            }

            // Payments overview with tabs
            item {
                PaymentsOverviewCard(
                    overview = paymentsOverview,
                    onPeriodSelected = { /* handle if you want to update */ }
                )
            }

            // Tenant Directory (clickable items)
            item {
                val tenantActivities = remember {
                    listOf(
                        TenantActivity(
                            id = "maintenance",
                            title = "Maintenance",
                            count = 3,
                            icon = { Icon(Icons.Default.RoomService, contentDescription = null) },
                            onClick = { onTenantActivityClick.invoke(TenantActivity("maintenance", "Maintenance", 3, {})) }
                        ),
                        TenantActivity(
                            id = "messages",
                            title = "Unread Messages",
                            count = 5,
                            icon = { Icon(Icons.Default.ChatBubble, contentDescription = null) }
                        ),
                        TenantActivity(
                            id = "verifications",
                            title = "Verifications",
                            count = 2,
                            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null) }
                        ),
                    )
                }
                TenantSelfServiceCard(activities = tenantActivities, onActivityClick = onTenantActivityClick)
            }

            // Recent payments header
            item {
                SectionTitle(title = "Recent Payments")
            }

            // Recent payments list (scrollable lazy items inside parent LazyColumn)
            items(recentPayments) { payment ->
                RecentPaymentRow(payment = payment, onClick = { /* open payment details */ })
            }

            // M-Pesa Reconciliation card
            item {
                MpesaReconciliationCard(
                    incomingText = "KSH 245,300",
                    matched = 18,
                    unmatched = 4,
                    flagged = 1,
                    onTapReconcile = onReconcileClick
                )
            }

            // Payment Predictor card
            item {
                PaymentPredictorCard(onClick = onPredictorClick)
            }

            item { Spacer(modifier = Modifier.height(80.dp)) } // space for bottom nav
        }
    }
}

// --- Top bar ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Placeholder profile circle (you can use AsyncImage from Coil)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0A74FF))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Welcome, Landlord!",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Montserrat,
                    fontSize = 18.sp
                )
            }
        },
        actions = {
            IconButton(onClick = { /* notifications */ }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications")
            }
        }
    )
}

// --- Quick action row (Payments, Tenants, Properties) ---
@Composable
private fun QuickActionRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickActionButton(icon = Icons.Default.CreditCard, label = "Payments")

        QuickActionButton(icon = Icons.Default.People, label = "Tenants")

        QuickActionButton(icon = Icons.Default.Home, label = "Properties")

    }
}

@Composable
private fun QuickActionButton(icon: ImageVector, label: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .height(64.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = label)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, fontWeight = FontWeight.Medium)
        }
    }
}

// --- Payments Overview Card (with mini tabs Today/Week/Month) ---
@Composable
private fun PaymentsOverviewCard(
    overview: PaymentOverview,
    onPeriodSelected: (String) -> Unit
) {
    var selected by remember { mutableStateOf("Today") }
    Card(
        shape = CardCorner,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F0F0) // Light gray color
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Payments Overview",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Montserrat,
                    modifier = Modifier.weight(1f),
                    fontSize = 16.sp,
                    color = Color.Blue
                )
                Text(text = "This Month", color = Color.Gray, fontSize = 12.sp)
            }


    Spacer(modifier = Modifier.height(8.dp))

            // Tabs
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Today", "This Week", "This Month").forEach { label ->
                    val isSelected = label == selected
                    FilterPill(text = label, selected = isSelected) {
                        selected = label
                        onPeriodSelected(label)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Amounts row (approximate of your uploaded design)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    val fmt = NumberFormat.getCurrencyInstance(Locale("en", "KE")).apply { maximumFractionDigits = 0 }
                    Text(text = fmt.format(overview.collected), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(text = "of ${fmt.format(overview.target)} collected", color = Color.Gray, fontSize = 12.sp)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "${overview.overdueCount} Overdue", color = Color(0xFFCC1F1F), fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress bar
            val progress = (overview.collected / (overview.target.takeIf { it > 0 } ?: 1.0)).toFloat().coerceIn(0f, 1f)
            LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth().height(8.dp))

            Spacer(modifier = Modifier.height(8.dp))

            // Small footer text
            Text(text = "Tap to view detailed payments", color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
private fun FilterPill(text: String, selected: Boolean, onClick: () -> Unit) {
    val bg by animateColorAsState(targetValue = if (selected) Color(0xFF0A74FF) else Color(0xFFF3F6FB))
    val contentColor = if (selected) Color.White else Color(0xFF2B2B2B)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text = text, color = contentColor, fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium)
    }
}

// --- Tenant Self-Service summary card ---
@Composable
private fun TenantSelfServiceCard(
    activities: List<TenantActivity>,
    onActivityClick: (TenantActivity) -> Unit = {}
) {
    Card(
        shape = CardCorner,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F0F0) // Light gray background
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Tenant Directory",
                fontWeight = FontWeight.SemiBold,
                fontFamily = Montserrat,
                color = Color.Blue
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                activities.forEach { activity ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onActivityClick(activity) }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFEFF7FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            activity.icon()
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = activity.title,
                            fontSize = 12.sp,
                            maxLines = 1,
                            color = Color.Black,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (activity.count > 0) {
                            Text(
                                text = activity.count.toString(),
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Improve operational efficiency by 25%",
                fontSize = 12.sp,
                color = Color(0xFF6B7280)
            )
        }
    }
}


// --- Recent payment row ---
@Composable
private fun RecentPaymentRow(payment: RecentPayment, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F0F0) // Light gray background
        )
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            // avatar initials
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFDDEEFF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = payment.payerName.split(" ").map { it.first().uppercaseChar() }.joinToString("").take(2),
                    fontWeight = FontWeight.Bold,
                    color = Color.Cyan
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(payment.payerName, fontWeight = FontWeight.Medium, color = Color.Black)
                Text(payment.unit, fontSize = 12.sp, color = Color.Gray)
            }

            val fmt = NumberFormat.getCurrencyInstance(Locale("en", "KE")).apply { maximumFractionDigits = 0 }
            Column(horizontalAlignment = Alignment.End) {
                Text(fmt.format(payment.amount), fontWeight = FontWeight.SemiBold, color = Color.Black)
                when (payment.status) {
                    PaymentStatus.PAID -> Text("Paid", color = Color(0xFF12B76A), fontSize = 12.sp)
                    PaymentStatus.PARTIAL -> Text("Partial", color = Color(0xFFFFB020), fontSize = 12.sp)
                    PaymentStatus.PENDING -> Text("Pending", color = Color(0xFF6B7280), fontSize = 12.sp)
                }
            }
        }
    }
}

// --- M-Pesa Reconciliation Card ---
@Composable
private fun MpesaReconciliationCard(
    incomingText: String,
    matched: Int,
    unmatched: Int,
    flagged: Int,
    onTapReconcile: () -> Unit
) {
    Card(shape = CardCorner, modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp), colors = CardDefaults.cardColors(
        containerColor = Color(0xFFF0F0F0) // Light gray background
    ))
    {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "M-Pesa Reconciliation", fontWeight = FontWeight.SemiBold, color = Color.Blue)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Today's Incoming M-Pesa", fontSize = 12.sp, color = Color.Gray)
            Text(text = incomingText, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))

            // lightweight sparkline placeholder as simple row of dots/bars
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                repeat(7) { i ->
                    Box(modifier = Modifier
                        .width(12.dp)
                        .height((6 + i * 4).dp)
                        .background(Color(0xFFD8EDFF), shape = RoundedCornerShape(6.dp)))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                ReconcileMetric(number = matched, label = "Matched")
                ReconcileMetric(number = unmatched, label = "Unmatched")
                ReconcileMetric(number = flagged, label = "Flagged")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = onTapReconcile, modifier = Modifier.fillMaxWidth()) {
                Text("Tap to Reconcile")
            }
        }
    }
}

@Composable
private fun ReconcileMetric(number: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = number.toString(), fontWeight = FontWeight.Bold, color = Color.Black)
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}

// --- Payment Predictor Card ---
@Composable
private fun PaymentPredictorCard(onClick: () -> Unit = {}) {
    Card(shape = CardCorner, modifier = Modifier.fillMaxWidth().clickable { onClick() }, elevation = CardDefaults.cardElevation(defaultElevation = 6.dp), colors = CardDefaults.cardColors(
        containerColor = Color(0xFFF0F0F0) // Light gray background
    )) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Payment Predictor (AI)", fontWeight = FontWeight.SemiBold, color = Color.Blue)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                // three status bullets (Low/Medium/High)
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatusDot(color = Color(0xFF12B76A)); Text("Low Risk: 75%", fontSize = 12.sp, color = Color.Black)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatusDot(color = Color(0xFFFFB020)); Text("Medium Risk: 15%", fontSize = 12.sp, color = Color.Black)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatusDot(color = Color(0xFFEF4444)); Text("High Risk: 10%", fontSize = 12.sp, color = Color.Black)
                    }
                }
                Icon(Icons.Default.AutoGraph, contentDescription = null, modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Powered by AI Prediction", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
private fun StatusDot(color: Color) {
    Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(10.dp)).background(color))
}

// --- Section title helper ---
@Composable
private fun SectionTitle(title: String) {
    Text(title, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(vertical = 8.dp), color = Color.Black)
}

// --- Bottom navigation items and bar ---
data class DScreenBottomNavItem(val route: String, val label: String, val icon: ImageVector)

@Composable
private fun BottomNavigationBar(navController: NavHostController, items: List<DScreenBottomNavItem>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Surface(
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        shape = BottomNavCorner
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                DScreenBottomNavItemView(item = item, selected = selected) {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    }
}

@Composable
private fun DScreenBottomNavItemView(item: DScreenBottomNavItem, selected: Boolean, onClick: () -> Unit) {
    val contentColor = if (selected) Color(0xFF0A74FF) else Color(0xFF7B7B7B)
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .clickable { onClick() }
        .padding(horizontal = 12.dp)
    ) {
        Icon(item.icon, contentDescription = item.label, tint = contentColor)
        Spacer(modifier = Modifier.height(4.dp))
        Text(item.label, fontSize = 12.sp, color = contentColor)

        // highlight bar
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .height(4.dp)
                .width(36.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(if (selected) Color(0xFF0A74FF) else Color.Transparent)
        )
    }
}

@Preview(
    name = "Landlord Dashboard - Pixel 7",
    showBackground = true,
    device = Devices.PIXEL_7
)
@Composable
fun LandlordDashboardPreviewPixel7() {
    val navController = rememberNavController()
    RentPulseTheme {
        RentPulseDashboardScreen(
            navController,
            paymentsOverview = PaymentOverview(
                title = "Payments Overview",
                collected = 18500.0,
                target = 21200.0,
                overdueCount = 2,
                periodLabel = "This Month"
            ),
            recentPayments = listOf(
                RecentPayment("1", "John Smith", "Unit 1A", 1200.0, PaymentStatus.PAID),
                RecentPayment("2", "Emily Davis", "Unit 3B", 850.0, PaymentStatus.PARTIAL),
                RecentPayment("3", "Samuel K", "Unit 2C", 3200.0, PaymentStatus.PAID),
                RecentPayment("4", "Aisha N", "Unit 5A", 1500.0, PaymentStatus.PENDING),
                RecentPayment("5", "David O", "Unit 7D", 2500.0, PaymentStatus.PAID),
            ),
            onTenantActivityClick = {},
            onReconcileClick = {},
            onPredictorClick = {},
            modifier = Modifier
        )
    }
}

