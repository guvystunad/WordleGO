package ee.ut.cs.wordlego.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ee.ut.cs.wordlego.ui.components.BackButton
import ee.ut.cs.wordlego.viewmodel.StatsViewModel

@Composable
fun StatsScreen(navController: NavHostController, statsViewModel: StatsViewModel) {
    val distribution = statsViewModel.guessDistribution

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

            if (distribution.isEmpty()) {
                Text(text = "No stats yet. Play a game to see your stats!")
            } else {
                (1..6).forEach { guess ->
                    val percent = distribution.getOrElse(guess) { 0f }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
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
                                    if (percent > 0) Color(0xFF6AAA64) else Color(0xFFD3D6DA),
                                    RoundedCornerShape(4.dp)
                                )
                        )
                    }
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
