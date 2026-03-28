package shibin.flowplayground.ui.theme.detail.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import shibin.flowplayground.data.FlowOperator
import shibin.flowplayground.model.OperatorDetailState
import shibin.flowplayground.ui.theme.BgCode
import shibin.flowplayground.ui.theme.BgSurface
import shibin.flowplayground.ui.theme.CodeBorder
import shibin.flowplayground.ui.theme.TextCode
import shibin.flowplayground.ui.theme.TextMuted
import shibin.flowplayground.ui.theme.detail.rememberInputHint

@Composable
fun LiveDemoPanel(
    state: OperatorDetailState,
    operator: FlowOperator,
    accent: Color,
    onInputChanged: (String) -> Unit,
    onRunDemo: () -> Unit,
    onStopDemo: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BgSurface)
            .border(1.dp, accent.copy(alpha = 0.22f), RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        // Section label
        Text(
            text = "LIVE DEMO",
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = accent,
                letterSpacing = 1.5.sp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Optional input field
        val inputHint = rememberInputHint(operator.demoType)
        if (inputHint != null) {
            OutlinedTextField(
                value = state.demoInput,
                onValueChange = onInputChanged,
                label = { Text(inputHint, fontSize = 13.sp) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accent,
                    focusedLabelColor = accent,
                    cursorColor = accent,
                    unfocusedBorderColor = Color(0xFF333355),
                    unfocusedLabelColor = TextMuted,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Run / Stop buttons
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = onRunDemo,
                enabled = !state.isDemoRunning,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accent,
                    disabledContainerColor = accent.copy(alpha = 0.35f)
                )
            ) {
                Text(
                    text = "▶  Run",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF0A0A0F)
                    )
                )
            }

            if (state.isDemoRunning) {
                OutlinedButton(
                    onClick = onStopDemo,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFFFF6B6B)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFFF6B6B)
                    )
                ) {
                    Text(
                        text = "⏹  Stop",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }

        // Progress bar
        if (state.isDemoRunning) {
            Spacer(modifier = Modifier.height(14.dp))
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp)),
                color = accent,
                trackColor = accent.copy(alpha = 0.18f)
            )
        }

        // Terminal output
        if (state.demoOutput.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(BgCode)
                    .border(1.dp, CodeBorder, RoundedCornerShape(10.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.demoOutput.forEach { line ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = ">",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                color = accent.copy(alpha = 0.7f),
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = line,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TextCode,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }
        }
    }
}