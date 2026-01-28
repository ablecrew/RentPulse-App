package com.rentpulse

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rentpulse.navigation.Screens


// Font setup (make sure the font file is added to res/font)
val montserrat = FontFamily(Font(R.font.montserrat_regular))

@Composable
fun PasswordResetSuccessScreen(navController: NavController) {
    var tickAlpha by remember { mutableStateOf(0f) }

    // Animate tick fade-in
    LaunchedEffect(Unit) {
        val anim = Animatable(0f)
        anim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800)
        )
        tickAlpha = anim.value
    }

    // Full screen with gradient background
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
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            // Tick inside a white circle
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White, shape = CircleShape)
                    .alpha(tickAlpha)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.blue_tick),
                    contentDescription = "Checkmark",
                    tint = Color(0xFF007BFF),
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Password Successfully Reset",
                fontFamily = montserrat,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your password has been updated\nYou can now log in with your new password",
                fontFamily = montserrat,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(40.dp))

            // "Back to Sign In" Button
            Button(
                onClick = {
                    navController.navigate(Screens.SignInScreen.route)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_icon),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Back to Sign In",
                    fontFamily = montserrat,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPasswordResetSuccessScreen() {
    val navController = rememberNavController()
    PasswordResetSuccessScreen(navController)
}
