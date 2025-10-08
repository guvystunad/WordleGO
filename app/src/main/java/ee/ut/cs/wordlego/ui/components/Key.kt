package ee.ut.cs.wordlego.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun Key(text: String, onClick: () -> Unit, backgroundColor: Color) {
    val width = if (text == "ENTER" || text == "⌫") 60.dp else 32.dp

    Box(
        modifier = Modifier
            .width(width)
            .height(48.dp)
            .background(backgroundColor, RoundedCornerShape(4.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = if (text.length > 1) 12.sp else 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (backgroundColor == Color(0xFFD3D6DA)) Color.Black else Color.White
        )
    }
}
