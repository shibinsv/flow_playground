package shibin.flowplayground.ui.theme.detail.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import shibin.flowplayground.data.FlowOperator
import shibin.flowplayground.ui.theme.BgPrimary
import shibin.flowplayground.ui.theme.TextLight

@Composable
fun DetailHero(
    operator: FlowOperator,
    accent: Color,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(accent.copy(alpha = 0.18f), BgPrimary)
                )
            )
            .padding(start = 20.dp, end = 20.dp, top = 56.dp, bottom = 32.dp)
    ) {

        // Back button
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.08f))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Column(modifier = Modifier.padding(top = 60.dp)) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(accent.copy(alpha = 0.15f))
                    .border(1.dp, accent.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Flow Operator",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = accent
                )
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = ".${operator.name}()",
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = accent
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = operator.description,
                fontSize = 15.sp,
                color = TextLight,
                lineHeight = 23.sp
            )
        }
    }
}