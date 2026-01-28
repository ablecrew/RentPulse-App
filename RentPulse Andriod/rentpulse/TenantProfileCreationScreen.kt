package com.rentpulse

import android.content.Context
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.rentpulse.data.models.ApiResponse
import com.rentpulse.data.models.TenantRequest
import com.rentpulse.navigation.Screens
import com.rentpulse.utils.AuthPreferences
import com.rentpulse.utils.TenantPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream

@Composable
fun TenantProfileCreationScreen(
    navController: NavController,
    onSave: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val context = LocalContext.current
    var fullName by remember { mutableStateOf("") }
    var phoneCode by remember { mutableStateOf("+254") }
    var phoneNumber by remember { mutableStateOf("") }
    var idNumber by remember { mutableStateOf("") }
    var profileUpdatesAllowed by remember { mutableStateOf(true) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
    }

    // ✅ Fetch user_id from SharedPreferences
    fun getUserId(context: Context): Int {
        val prefs = context.getSharedPreferences("RentPulsePrefs", Context.MODE_PRIVATE)
        return prefs.getInt("user_id", 0)
    }

    // ✅ Convert image to Base64
    fun convertImageToBase64(uri: Uri?): String? {
        return try {
            uri?.let {
                val inputStream: InputStream? = context.contentResolver.openInputStream(it)
                val bytes = inputStream?.readBytes()
                inputStream?.close()
                bytes?.let { Base64.encodeToString(it, Base64.NO_WRAP) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // ✅ API call + save locally
    fun saveTenantInfo() {
        if (fullName.isBlank() || phoneNumber.isBlank() || idNumber.isBlank()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        isLoading = true
        val base64Image = convertImageToBase64(imageUri)

        val request = TenantRequest(
            user_id = getUserId(context),
            full_name = fullName,
            phone = "+254$phoneNumber",  // fixed Kenyan prefix
            id_number = idNumber,
            profile_image = base64Image ?: "",
            allow_updates = profileUpdatesAllowed
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: ApiResponse = RetrofitClient.api.addTenant(request)

                isLoading = false

                if (response.success) {
                    // ✅ Save tenant details locally
                    TenantPreferences.saveTenantDetails(
                        context = context,
                        fullName = fullName,
                        phone = "+254$phoneNumber",
                        idNumber = idNumber,
                        profileImage = base64Image,
                        allowUpdates = profileUpdatesAllowed
                    )

                    // ✅ Save role for this user
                    AuthPreferences.saveUserRole(context, "tenant")

                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "Tenant profile saved!", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screens.TenantDashboardScreen.route) {
                            popUpTo(Screens.TenantProfileCreationScreen.route) { inclusive = true }
                        }
                    }
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "Error: ${response.message}", Toast.LENGTH_LONG).show()
                    }
                }

            } catch (e: Exception) {
                isLoading = false
                e.printStackTrace()
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // ✅ UI (UNCHANGED)
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
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.padding(top = 40.dp)
        ) {
            Text(
                text = "Create Your\nTenant Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Help landlords identify and connect\nwith you",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Profile image picker
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                            .clickable { launcher.launch("image/*") },
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

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full name", color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(fontFamily = com.rentpulse.ui.theme.Montserrat, color = Color.Black),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

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

                    OutlinedTextField(
                        value = idNumber,
                        onValueChange = { idNumber = it },
                        label = { Text("ID Number", color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(fontFamily = com.rentpulse.ui.theme.Montserrat, color = Color.Black),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Allow future profile updates", fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.montserrat_regular)))
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

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { saveTenantInfo() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        Text(if (isLoading) "Saving..." else "Save Profile", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Cancel",
                        color = Color(0xFF007BFF),
                        modifier = Modifier.clickable {
                            navController.navigate(Screens.WelcomeScreen.route)
                        },
                        fontFamily = FontFamily(Font(R.font.montserrat_medium))
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TenantProfileScreenPreview() {
    val navController = rememberNavController()
    TenantProfileCreationScreen(navController)
}
