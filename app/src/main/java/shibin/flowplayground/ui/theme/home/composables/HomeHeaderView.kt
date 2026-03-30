package shibin.flowplayground.ui.theme.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import shibin.flowplayground.BuildConfig

@Composable
fun HomeHeaderView() {
    val version = "v${BuildConfig.VERSION_NAME}"
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A1A2E), Color(0xFF0A0A0F))
                )
            )
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {

            Column {
                Text(
                    text = "Flow", style = TextStyle(
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = (-1).sp
                    )
                )
                Text(
                    text = "Operators", style = TextStyle(
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = (-1).sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tap any operator to explore it live", style = TextStyle(
                        fontSize = 14.sp, color = Color(0xFF8888AA), letterSpacing = 0.sp
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = version, style = TextStyle(
                    fontSize = 12.sp, color = Color(0xFF8888AA)
                ), modifier = Modifier.align(Alignment.Top)
            )
        }
    }
}