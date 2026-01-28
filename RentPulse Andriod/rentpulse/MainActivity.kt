package com.rentpulse

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.rentpulse.navigation.AppNavigation
import com.rentpulse.ui.theme.RentPulseTheme
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class MainActivity : ComponentActivity() {

    private val client = OkHttpClient()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Log.d("FCM", "✅ Notification permission granted")
                fetchFcmToken()
            } else {
                Log.w("FCM", "❌ Notification permission denied by user")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("FCM", "🚀 App started, initializing Firebase...")

        FirebaseApp.initializeApp(this)
        requestNotificationPermission()

        enableEdgeToEdge()
        setContent {
            RentPulseTheme {
                RentPulseApp()
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    fetchFcmToken()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            fetchFcmToken()
        }
    }

    private fun fetchFcmToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("FCM", "❌ Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }
                val token = task.result
                Log.d("FCM", "🎉 FCM Token generated: $token")

                // Send token dynamically for the current logged-in user
                val currentUserId = getCurrentUserId()
                if (currentUserId != null) {
                    sendTokenToServer(token, currentUserId)
                } else {
                    Log.w("FCM", "⚠️ No logged-in user, token not sent")
                }
            }
    }

    private fun sendTokenToServer(token: String, userId: Int) {
        val url = "http://192.168.0.192/rentpulse-api/users/update_fcm_token.php"
        val json = JSONObject()
        json.put("user_id", userId)
        json.put("fcm_token", token)

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            json.toString()
        )

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FCM", "❌ Failed to send token: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        Log.e("FCM", "❌ Server error while saving token: ${it.code}")
                    } else {
                        val serverResponse = it.body?.string()
                        Log.d("FCM", "✅ Token updated on server for user $userId → $serverResponse")
                    }
                }
            }
        })
    }

    // Replace this with your actual user session logic
    private fun getCurrentUserId(): Int? {
        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        return if (sharedPref.contains("user_id")) {
            sharedPref.getInt("user_id", -1).takeIf { it != -1 }
        } else null
    }
}

@Composable
fun RentPulseApp() {
    val navController = rememberNavController()
    AppNavigation(navController)
}
