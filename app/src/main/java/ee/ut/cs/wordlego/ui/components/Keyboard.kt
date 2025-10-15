package ee.ut.cs.wordlego.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ee.ut.cs.wordlego.GameState
import androidx.compose.ui.Alignment

@Composable
fun Keyboard(onKeyPress: (String) -> Unit, gameState: GameState) {
    val rows = listOf(
        listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "Ü", "Õ"),
        listOf("A", "S", "D", "F", "G", "H", "J", "K", "L", "Ö", "Ä"),
        listOf("ENTER", "Z", "X", "C", "V", "B", "N", "M", "Š", "Ž", "⌫")
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                row.forEach { key ->
                    val keyColor = getKeyColor(key, gameState)

                    val keyWeight = when (key) {
                        "ENTER" -> 2f
                        else -> 1f
                    }

                    Key(
                        text = key,
                        onClick = { onKeyPress(key) },
                        backgroundColor = keyColor,
                        modifier = Modifier.weight(keyWeight)
                    )
                }
            }
        }
    }
}
