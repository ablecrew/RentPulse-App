package com.rentpulse.push.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rentpulse.data.models.TokenRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "🎉 New FCM Token generated: $token")

        // Save locally for retry if user isn't logged in yet
        val tempPrefs = getSharedPreferences("TempFCM", MODE_PRIVATE)
        tempPrefs.edit().putString("pending_fcm_token", token).apply()

        // Try sending immediately
        sendTokenToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "📩 Message received from: ${remoteMessage.from}")

        remoteMessage.notification?.let {
            showNotification(it.title, it.body)
        }
    }

    private fun showNotification(title: String?, body: String?) {
        val channelId = "rentpulse_channel"

        // Create notification channel (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "RentPulse Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title ?: "RentPulse")
            .setContentText(body ?: "")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
        } else {
            Log.w("FCM", "⚠️ Notification permission not granted, skipping notification.")
        }
    }

    /**
     * Sends the FCM token to the backend using RetrofitClient.
     * Supports both TenantPreferences and LandlordPreferences.
     */
    private fun sendTokenToServer(token: String) {
        val tenantPrefs = getSharedPreferences("TenantPreferences", MODE_PRIVATE)
        val landlordPrefs = getSharedPreferences("LandlordPreferences", MODE_PRIVATE)

        val tenantId = tenantPrefs.getInt("user_id", -1)
        val landlordId = landlordPrefs.getInt("user_id", -1)
        val userId = if (tenantId != -1) tenantId else if (landlordId != -1) landlordId else -1

        if (userId == -1) {
            Log.w("FCM", "⚠️ No logged-in user found, token not sent. Saved for retry.")
            val tempPrefs = getSharedPreferences("TempFCM", MODE_PRIVATE)
            tempPrefs.edit().putString("pending_fcm_token", token).apply()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.updateFcmToken(
                    TokenRequest(user_id = userId, fcm_token = token)
                )

                if (response.success) {
                    Log.d("FCM", "✅ Token updated successfully for user_id=$userId")
                    // Clear any saved token
                    getSharedPreferences("TempFCM", MODE_PRIVATE)
                        .edit().remove("pending_fcm_token").apply()
                } else {
                    Log.e("FCM", "❌ Server error: ${response.message}")
                }

            } catch (e: IOException) {
                Log.e("FCM", "❌ Network error: ${e.message}")
            } catch (e: HttpException) {
                Log.e("FCM", "❌ HTTP error: ${e.response()?.errorBody()?.string()}")
            } catch (e: Exception) {
                Log.e("FCM", "❌ Unexpected error: ${e.localizedMessage}")
            }
        }
    }

    /**
     * Retry sending any pending FCM token saved earlier.
     * Call this after a successful login.
     */
    fun retryPendingToken() {
        val tempPrefs = getSharedPreferences("TempFCM", MODE_PRIVATE)
        val pendingToken = tempPrefs.getString("pending_fcm_token", null)
        if (pendingToken != null) {
            Log.d("FCM", "🔁 Retrying pending FCM token send...")
            sendTokenToServer(pendingToken)
        }
    }
}
