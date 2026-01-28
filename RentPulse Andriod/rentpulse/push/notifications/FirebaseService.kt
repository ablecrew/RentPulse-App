package com.rentpulse.push.notifications


import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

object FirebaseService {

    fun getFCMToken(onTokenReceived: (String?) -> Unit) {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                    onTokenReceived(null)
                    return@addOnCompleteListener
                }

                // ✅ Get new FCM token
                val token = task.result
                Log.d("FCM", "FCM Token: $token")
                onTokenReceived(token)
            }
    }
}
