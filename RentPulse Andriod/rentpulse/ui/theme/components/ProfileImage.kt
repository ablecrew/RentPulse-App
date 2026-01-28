package com.rentpulse.ui.theme.components

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun ProfileImage(base64String: String?, modifier: Modifier = Modifier) {
    Log.d("ProfileImage", "Received string: ${base64String?.take(50)}...") // first 50 chars

    // Clean the base64 string (strip prefix if present)
    val cleanBase64 = base64String
        ?.replace("data:image/png;base64,", "")
        ?.replace("data:image/jpeg;base64,", "")
        ?.trim()

    Log.d("ProfileImage", "Cleaned string length: ${cleanBase64?.length ?: 0}")

    val bitmap = try {
        if (!cleanBase64.isNullOrEmpty()) {
            val imageBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
            Log.d("ProfileImage", "Decoded bytes size: ${imageBytes.size}")
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size).also {
                Log.d("ProfileImage", "Bitmap decode success? ${it != null}")
            }
        } else {
            Log.e("ProfileImage", "Base64 string is null or empty")
            null
        }
    } catch (e: Exception) {
        Log.e("ProfileImage", "Decoding failed: ${e.message}")
        null
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Profile Image",
            modifier = modifier
        )
    } else {
        Icon(
            imageVector = Icons.Outlined.AccountCircle,
            contentDescription = "Default Profile",
            tint = Color.Gray,
            modifier = modifier
        )
    }
}

