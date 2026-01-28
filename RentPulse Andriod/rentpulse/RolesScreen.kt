package com.rentpulse

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.data.models.UpdateRoleRequest
import com.rentpulse.navigation.Screens
import com.rentpulse.ui.theme.Montserrat
import com.rentpulse.ui.theme.RentPulseTheme
import kotlinx.coroutines.launch

@Composable
fun RolesScreen(navController: NavController) {
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF007BFF), Color(0xFFECECEC))
    )

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }

    // 🔹 Helper function to fetch userId from SharedPreferences
    fun getUserId(context: Context): Int? {
        val sharedPreferences = context.getSharedPreferences("RentPulsePrefs", Context.MODE_PRIVATE)
        return if (sharedPreferences.contains("userId")) {
            sharedPreferences.getInt("userId", -1).takeIf { it != -1 }
        } else null
    }


    fun saveUserRole(role: String) {
        scope.launch {
            isLoading = true
            try {
                val userId = getUserId(context)
                if (userId == null) {
                    Toast.makeText(
                        context,
                        "User ID not found. Please log in again.",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate(Screens.SignInScreen.route) // fallback
                    return@launch
                }

                val response = RetrofitClient.api.updateRole(
                    UpdateRoleRequest(user_id = userId, role = role)
                )

                if (response.success) {
                    Toast.makeText(context, "Role saved as $role", Toast.LENGTH_SHORT).show()
                    if (role == "tenant") {
                        navController.navigate(Screens.TenantProfileCreationScreen.route)
                    } else {
                        navController.navigate(Screens.LandlordProfileCreationScreen.route)
                    }
                } else {
                    Toast.makeText(
                        context,
                        response.message ?: "Failed to save role",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                isLoading = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            // RentPulse Logo
            Image(
                painter = painterResource(id = R.drawable.icon_1),
                contentDescription = "RentPulse Logo",
                modifier = Modifier
                    .height(130.dp)
                    .padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Category Registration",
                fontFamily = Montserrat,
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(30.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Choose Your Role to\nGet Started",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = "Whether you’re managing properties or renting a unit, RentPulse helps you stay informed and connected",
                        fontFamily = Montserrat,
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Register as Tenant
                    OutlinedButton(
                        onClick = { if (!isLoading) saveUserRole("tenant") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = ButtonDefaults.outlinedButtonBorder
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.tenant_icon),
                                    contentDescription = "Tenant Icon",
                                    tint = Color.Black,
                                    modifier = Modifier.size(70.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Register as a Tenant",
                                    fontFamily = Montserrat,
                                    color = Color.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Register as Landlord (Auto-login flow)
                    OutlinedButton(
                        onClick = {
                            if (!isLoading) {
                                scope.launch {
                                    isLoading = true
                                    try {
                                        val userId = getUserId(context)
                                        if (userId == null) {
                                            Toast.makeText(context, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show()
                                            navController.navigate(Screens.SignInScreen.route)
                                            return@launch
                                        }

                                        val response = RetrofitClient.api.updateRole(
                                            UpdateRoleRequest(user_id = userId, role = "landlord")
                                        )

                                        if (response.success) {
                                            Toast.makeText(context, "Role saved as Landlord", Toast.LENGTH_SHORT).show()

                                            // 👇 Auto-login redirection
                                            navController.navigate(Screens.SignInScreen.route) {
                                                popUpTo(Screens.RolesScreen.route) { inclusive = true }
                                            }

                                        } else {
                                            Toast.makeText(context, response.message ?: "Failed to save role", Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = ButtonDefaults.outlinedButtonBorder
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.landlord_icon),
                                    contentDescription = "Landlord Icon",
                                    tint = Color.Black,
                                    modifier = Modifier.size(70.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Register as a Landlord/lady",
                                    fontFamily = Montserrat,
                                    color = Color.Black
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RolesScreenPreview() {
    RentPulseTheme {
        RolesScreen(navController = rememberNavController())
    }
}

