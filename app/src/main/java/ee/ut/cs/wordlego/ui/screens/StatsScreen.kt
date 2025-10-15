package ee.ut.cs.wordlego.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ee.ut.cs.wordlego.ui.components.BackButton
import androidx.compose.foundation.background
import androidx.compose.ui.text.font.FontWeight

@Composable
fun StatsScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        BackButton(navController = navController, modifier = Modifier.align(Alignment.TopStart))

        Column(
            modifier = Modifier.fillMaxSize().padding(top = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Congrats!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            Text(
                text = "Guess distribution",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val distribution = listOf(1 to 0.1f, 2 to 0.15f, 3 to 0.5f, 4 to 0.2f, 5 to 0.3f, 6 to 0.18f)

            distribution.forEach { (guess, percent) ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = guess.toString(),
                        modifier = Modifier.width(24.dp),
                        fontSize = 14.sp
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(percent)
                            .height(24.dp)
                            .background(
                                if (guess == 3) Color(0xFF6AAA64) else Color(0xFFD3D6DA),
                                RoundedCornerShape(4.dp)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 32.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("Play more", color = Color.Black, fontSize = 16.sp)
            }
        }
    }
}
