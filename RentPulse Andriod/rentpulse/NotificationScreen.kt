package com.rentpulse

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.data.models.Notification
import com.rentpulse.utils.AuthPreferences
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var notifications by remember { mutableStateOf(listOf<Notification>()) }
    var loading by remember { mutableStateOf(true) }

    // Initial fetch
    LaunchedEffect(Unit) {
        val token = AuthPreferences.readAuthToken(context)
        if (!token.isNullOrEmpty()) {
            coroutineScope.launch {
                try {
                    val response = RetrofitClient.api.getNotifications("Bearer $token")
                    if (response.success) {
                        notifications = response.data
                    } else {
                        Log.e("NotificationScreen", "Failed: ${response.message}")
                    }
                } catch (e: Exception) {
                    Log.e("NotificationScreen", "API error", e)
                } finally {
                    loading = false
                }
            }
        } else {
            loading = false
        }
    }

    // 🔄 Periodic polling to update notifications dynamically
    LaunchedEffect(Unit) {
        val token = AuthPreferences.readAuthToken(context)
        if (!token.isNullOrEmpty()) {
            while (true) {
                try {
                    val response = RetrofitClient.api.getNotifications("Bearer $token")
                    if (response.success) {
                        notifications = response.data
                    }
                } catch (e: Exception) {
                    Log.e("NotificationScreen", "Error fetching notifications", e)
                }
                delay(1000) // 1 second interval
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF007BFF))
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                notifications.isEmpty() -> {
                    Text(
                        text = "No notifications found",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(notifications) { notification ->
                            NotificationCard(notification = notification) {
                                // Mark as read automatically when tapped
                                coroutineScope.launch {
                                    val token = AuthPreferences.readAuthToken(context)
                                    if (!token.isNullOrEmpty()) {
                                        try {
                                            RetrofitClient.api.markNotificationRead(
                                                token = "Bearer $token",
                                                body = mapOf("notification_id" to notification.id)
                                            )
                                            // Update local UI immediately
                                            notifications = notifications.map {
                                                if (it.id == notification.id) it.copy(is_read = 1) else it
                                            }
                                        } catch (e: Exception) {
                                            Log.e("NotificationScreen", "Error marking read", e)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationCard(notification: Notification, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (notification.is_read == 0) Color(0xFFE3F2FD) else Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = notification.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                if (notification.is_read == 0) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(Color.Red, shape = CircleShape)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = notification.message,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = notification.created_at,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    val navController = rememberNavController()
    NotificationScreen(navController)
}
