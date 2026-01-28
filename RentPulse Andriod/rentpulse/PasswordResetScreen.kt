package com.rentpulse

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay


@Composable
fun PasswordResetScreen(navController: NavController) {
    var emailOrPhone by remember { mutableStateOf(TextFieldValue("")) }
    var showSuccess by remember { mutableStateOf(false) }

    // Tick animation state
    val tickAlpha by animateFloatAsState(
        targetValue = if (showSuccess) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "tickAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to Color(0xFF007BFF),
                        0.5f to Color(0xFF007BFF),
                        0.55f to Color(0xFFBFDFFF),
                        1.0f to Color(0xFFF5F5F5)
                    )
                )
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(top = 90.dp)
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.icon_1),
                contentDescription = "RentPulse Logo",
                modifier = Modifier.height(130.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Reset Your Password",
                fontFamily = MontserratFont,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Enter your email or phone number to\nreceive a reset link or code",
                fontFamily = MontserratFont,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(30.dp))

            // White Container
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Email or Phone Number",
                        fontFamily = MontserratFont,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = emailOrPhone,
                        onValueChange = { emailOrPhone = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(
                            fontFamily = com.rentpulse.ui.theme.Montserrat,
                            color = Color.Black),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "We'll send a code to help you reset your\npassword securely",
                        fontFamily = MontserratFont,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            // Simulate reset and show animated tick
                            showSuccess = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
                    ) {
                        Text(
                            text = "Send Reset Code",
                            fontFamily = MontserratFont,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Didn’t receive code? Resend",
                        fontFamily = MontserratFont,
                        fontSize = 14.sp,
                        color = Color.Black
                    )


                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "← Back to Sign In",
                fontFamily = MontserratFont,
                fontSize = 12.sp,
                color = Color(0xFF007BFF)
            )
        }

        // Animated Tick Overlay
        if (showSuccess) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80007BFF)) // Semi-transparent blue
                    .clickable { showSuccess = false },
                contentAlignment = Alignment.TopCenter
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.check_icon),
                    contentDescription = "Success Tick",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(top = 80.dp)
                        .size(80.dp)
                        .alpha(tickAlpha)
                )
            }

            // Automatically hide tick after delay
            LaunchedEffect(key1 = true) {
                delay(2000)
                showSuccess = false
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPasswordResetScreen() {
    val navController = rememberNavController()
    PasswordResetScreen(navController)
}
