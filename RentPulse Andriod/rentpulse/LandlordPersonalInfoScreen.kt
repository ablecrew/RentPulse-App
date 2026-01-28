package com.rentpulse

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.rentpulse.data.models.ApiResponse
import com.rentpulse.data.models.LandlordRequest
import com.rentpulse.navigation.Screens
import kotlinx.coroutines.*

@Composable
fun LandlordPersonalInfoScreen(
    navController: NavController,
    onBack: () -> Unit = {},
    onCancel: () -> Unit = {},
    onHelpCenter: () -> Unit = {}
) {
    val montserrat = FontFamily(Font(R.font.montserrat_regular))
    var fullName by remember { mutableStateOf("") }
    var phoneCode by remember { mutableStateOf("+254") }
    var phoneNumber by remember { mutableStateOf("") }
    var idPassport by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var profileUpdatesAllowed by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // ✅ Image picker launcher with URI permission
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
            imageUri = it
        }
    }

    // ✅ SharedPreferences for user_id
    fun getUserId(context: Context): Int {
        val prefs = context.getSharedPreferences("RentPulsePrefs", Context.MODE_PRIVATE)
        return prefs.getInt("user_id", 0)
    }

    // ✅ Base64 converter with logging
    fun convertImageToBase64(uri: Uri?): String? {
        if (uri == null) return null

        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val bytes = inputStream.readBytes()
                if (bytes.isNotEmpty()) {
                    android.util.Log.d("ProfileImage", "✅ Image size: ${bytes.size} bytes")
                    val base64 = Base64.encodeToString(bytes, Base64.DEFAULT)
                    android.util.Log.d("ProfileImage", "✅ Encoded length: ${base64.length}")
                    base64
                } else {
                    android.util.Log.e("ProfileImage", "⚠️ Image file is empty!")
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            android.util.Log.e("ProfileImage", "⚠️ Failed to convert image: ${e.message}")
            null
        }
    }

    // ✅ Save landlord info (background safe)
    fun saveLandlordInfo() {
        if (fullName.isBlank() || phoneNumber.isBlank() || idPassport.isBlank()) {
            Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        isLoading = true

        CoroutineScope(Dispatchers.IO).launch {
            val base64Image = convertImageToBase64(imageUri)

            val request = LandlordRequest(
                user_id = getUserId(context),
                name = fullName,
                phone = phoneCode + phoneNumber,
                id_number = idPassport,
                profile_image = base64Image,
                allow_updates = profileUpdatesAllowed,
                email = null
            )

            try {
                val response: ApiResponse = RetrofitClient.api.addLandlord(request)

                withContext(Dispatchers.Main) {
                    isLoading = false
                    if (response.success) {
                        Toast.makeText(context, "Profile saved successfully!", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screens.LandlordDashboardd.route)
                    } else {
                        Toast.makeText(context, "Error: ${response.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    isLoading = false
                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // ✅ UI layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to Color(0xFF007BFF),
                        0.5f to Color(0xFF007BFF),
                        0.55f to Color(0xFFBFDFFF),
                        1.0f to Color(0xFFF5F5F5)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔙 Back Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
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
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Personal Information",
                fontFamily = montserrat,
                fontSize = 20.sp,
                fontWeight = Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🧾 White Container Card
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(8.dp))

                    // 🖼 Profile Image Picker
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(imageUri),
                                contentDescription = "Profile Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .background(Color.White, shape = CircleShape)
                                .padding(4.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 🧍 Full Name
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full name", fontFamily = montserrat, color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(fontFamily = montserrat, color = Color.Black),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 📞 Phone Fields
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = phoneCode,
                            onValueChange = { phoneCode = it },
                            label = { Text("Code", fontFamily = montserrat, color = Color.Black) },
                            modifier = Modifier.width(100.dp),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = TextStyle(fontFamily = montserrat, color = Color.Black),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            leadingIcon = { Text("🇰🇪", fontSize = 20.sp) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = { Text("Phone number", fontFamily = montserrat, color = Color.Black) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = TextStyle(fontFamily = montserrat, color = Color.Black),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 🪪 ID / Passport
                    OutlinedTextField(
                        value = idPassport,
                        onValueChange = { idPassport = it },
                        label = { Text("ID/Passport No.", fontFamily = montserrat, color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(fontFamily = montserrat, color = Color.Black),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 🔄 Allow Updates
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Allow future profile updates", fontSize = 14.sp, fontFamily = montserrat)
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = profileUpdatesAllowed,
                            onCheckedChange = { profileUpdatesAllowed = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF007BFF),
                                checkedTrackColor = Color(0xFFBFDFFF)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 💾 Save Button
                    Button(
                        onClick = { saveLandlordInfo() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
                    ) {
                        Text(if (isLoading) "Saving..." else "Save", color = Color.White, fontFamily = montserrat)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // ❌ Cancel
                    Text(
                        text = "Cancel",
                        fontFamily = montserrat,
                        color = Color(0xFF007BFF),
                        modifier = Modifier.clickable { onCancel() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🆘 Help Center
            Text(
                text = "Help Center",
                fontFamily = montserrat,
                color = Color(0xFF007BFF),
                modifier = Modifier.clickable { onHelpCenter() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LandlordPersonalInfoPreview() {
    val navController = rememberNavController()
    LandlordPersonalInfoScreen(
        navController,
        onBack = { navController.navigate(Screens.LandlordProfileCreationScreen.route) },
        onCancel = { navController.navigate(Screens.WelcomeScreen.route) },
        onHelpCenter = { navController.navigate(Screens.HelpCenterScreen.route) }
    )
}
