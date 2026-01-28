package com.rentpulse.ui.screens

// All imports
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.R
import com.rentpulse.ui.theme.RentPulseTheme

// ---------- Data models ----------
data class PaymentItem(
    val amountText: String,
    val methodIconRes: Int,
    val methodText: String,
    val dateText: String,
    val status: PaymentStatus,
    val idText: String
)

enum class PaymentStatus { COMPLETED, PENDING, FAILED }

// ---------- Screen composable ----------
@Composable
fun TenantPaymentHistoryScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onFilterClick: () -> Unit = {}
) {
    // sample gradient colors for "very subtle white" -> "very subtle light blue"
    val gradientStart = Color(0x1A007BFF)            // very subtle white
    val gradientEnd = Color(0xFFF3F9FF)              // very subtle light blue

    // sample tabs
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("All", "Completed", "Pending", "Failed")

    // sample data
    val items = remember {
        listOf(
            PaymentItem("Ksh. 36,000", R.drawable.card, "Visa **** 4242", "Oct 15, 2025", PaymentStatus.COMPLETED, "ID: GH1234K5"),
            PaymentItem("Ksh. 36,000", R.drawable.bank, "Bank Transfer", "Sep 14, 2025", PaymentStatus.PENDING, "ID: RF84X2P9"),
            PaymentItem("Ksh. 36,000", R.drawable.card, "Visa **** 4242", "Aug 15, 2025", PaymentStatus.COMPLETED, "ID: AB78D9C0"),
            PaymentItem("Ksh. 36,000", R.drawable.mpesa, "M-PESA", "Jul 15, 2025", PaymentStatus.FAILED, "ID: ZY98X7W6"),
            PaymentItem("Ksh. 36,000", R.drawable.card, "Mastercard **** 8910", "Jun 15, 2025", PaymentStatus.COMPLETED, "ID: QW12ER34")
        )
    }

    // Root container with diagonal gradient (Top-Left -> Bottom-Right)
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(gradientStart, gradientEnd),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 3000f)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Top app bar (static)
            TopBar(
                title = "Payment History",
                onBack = { navController.popBackStack() },
                onFilter = onFilterClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tabs Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = index == selectedTab
                    FilterTab(
                        text = title,
                        selected = isSelected,
                        onClick = { selectedTab = index }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(items) { item ->
                    // Optionally filter by selectedTab
                    if (selectedTab == 0 || matchesTab(item.status, selectedTab)) {
                        PaymentItemCard(paymentItem = item)
                    }
                }
            }
        }
    }
}

private fun matchesTab(status: PaymentStatus, tabIndex: Int): Boolean {
    return when (tabIndex) {
        1 -> status == PaymentStatus.COMPLETED
        2 -> status == PaymentStatus.PENDING
        3 -> status == PaymentStatus.FAILED
        else -> true
    }
}

// ---------- Top bar ----------
@Composable
private fun TopBar(
    title: String,
    onBack: () -> Unit,
    onFilter: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back icon (clickable)
        IconButton(onClick = onBack) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(22.dp)
            )
        }

        // Title centered — we use Box with weight to center visually while keeping icons at edges
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0B1724) // dark-ish text
            )
        }

        // Filter / slider icon (clickable)
        IconButton(onClick = onFilter) {
            Image(
                painter = painterResource(id = R.drawable.ic_sliders),
                contentDescription = "Filters",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

// ---------- Tab chip ----------
@Composable
private fun FilterTab(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .wrapContentWidth()
            .height(36.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        color = if (selected) Color(0xFF007BFF) else Color.White,
        tonalElevation = if (selected) 6.dp else 0.dp,
        shape = RoundedCornerShape(18.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 14.dp)) {
            Text(
                text = text,
                color = if (selected) Color.White else Color(0xFF2B3440),
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
            )
        }
    }
}

// ---------- Payment card composable ----------
@Composable
private fun PaymentItemCard(paymentItem: PaymentItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(14.dp)
        ) {
            // Top row: icon, amount, date + status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon circle
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF6F9FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = paymentItem.methodIconRes),
                        contentDescription = paymentItem.methodText,
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = paymentItem.amountText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0B1724)
                        )

                        // Date and status column
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = paymentItem.dateText,
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            StatusChip(status = paymentItem.status)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Method text
                    Text(
                        text = paymentItem.methodText,
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Divider gray line + ID text below
            Divider(color = Color(0xFFE6E9EE), thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = paymentItem.idText,
                fontSize = 12.sp,
                color = Color(0xFF9AA4B2)
            )
        }
    }
}

// ---------- Status chip ----------
@Composable
private fun StatusChip(status: PaymentStatus) {
    val (bg, text) = when (status) {
        PaymentStatus.COMPLETED -> Color(0xFF1DB954) to "Completed" // green
        PaymentStatus.PENDING -> Color(0xFFFFC23A) to "Pending"    // amber
        PaymentStatus.FAILED -> Color(0xFFFF4D4F) to "Failed"      // red
    }

    // The pills appear with colored background and white text similar to screenshot
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// ---------- Preview ----------
@Preview(device = Devices.PIXEL_7, showBackground = true)
@Composable
fun PaymentHistoryScreen_Preview() {
    // Provide a dummy navController so the composable compiles in preview
    val navController = rememberNavController()
    RentPulseTheme {
        TenantPaymentHistoryScreen(
            navController = navController,
            onFilterClick = { /* preview: no-op */ }
        )
    }
}
