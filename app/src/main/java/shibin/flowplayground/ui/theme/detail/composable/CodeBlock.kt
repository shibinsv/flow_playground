package shibin.flowplayground.ui.theme.detail.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import shibin.flowplayground.ui.theme.BgCode
import shibin.flowplayground.ui.theme.BgCodeHeader
import shibin.flowplayground.ui.theme.CodeBorder
import shibin.flowplayground.ui.theme.TextCode

@Composable
fun CodeBlock(snippet: String) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BgCode)
            .border(1.dp, CodeBorder, RoundedCornerShape(14.dp))
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BgCodeHeader)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "kotlin",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color(0xFF8B949E),
                    fontFamily = FontFamily.Monospace
                )
            )
            // macOS traffic light dots
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(Color(0xFFFF5F57), Color(0xFFFFBD2E), Color(0xFF28CA41))
                    .forEach { dotColor ->
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(dotColor)
                        )
                    }
            }
        }

        Text(
            text = snippet,
            modifier = Modifier.padding(16.dp),
            style = TextStyle(
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                color = TextCode,
                lineHeight = 21.sp
            )
        )
    }
}
