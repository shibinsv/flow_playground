package shibin.flowplayground.ui.theme.detail.composable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import shibin.flowplayground.enums.DemoType

// --- Data model for the diagram ---

data class MarbleEvent(
    val time: Float,   // 0f..1f position on timeline
    val label: String,
    val isDropped: Boolean = false
)

data class MarbleDiagramData(
    val inputMarbles: List<MarbleEvent>,
    val outputMarbles: List<MarbleEvent>,
    val operatorLabel: String,
    val operatorSublabel: String = ""
)

// --- Color constants (mirrors detail screen palette) ---

private val ColorInputMarble  = Color(0xFF6C63FF)
private val ColorOutputMarble = Color(0xFF00D4AA)
private val ColorDropped      = Color(0xFFFF6B6B)
private val ColorTimeline     = Color(0xFF444466)
private val ColorPlayhead     = Color(0xFFFFFFFF)
private val ColorTickLabel    = Color(0xFF666688)

// --- Marble data per DemoType ---

fun marbleDiagramDataFor(demoType: DemoType): MarbleDiagramData = when (demoType) {

    DemoType.MAP -> MarbleDiagramData(
        inputMarbles = listOf(
            MarbleEvent(0.1f, "1"), MarbleEvent(0.3f, "2"), MarbleEvent(0.5f, "3"),
            MarbleEvent(0.7f, "4"), MarbleEvent(0.9f, "5")
        ),
        outputMarbles = listOf(
            MarbleEvent(0.1f, "2"), MarbleEvent(0.3f, "4"), MarbleEvent(0.5f, "6"),
            MarbleEvent(0.7f, "8"), MarbleEvent(0.9f, "10")
        ),
        operatorLabel = ".map { it × 2 }"
    )

    DemoType.FILTER -> MarbleDiagramData(
        inputMarbles = listOf(
            MarbleEvent(0.1f, "1", isDropped = true),
            MarbleEvent(0.3f, "2"),
            MarbleEvent(0.5f, "3", isDropped = true),
            MarbleEvent(0.7f, "4"),
            MarbleEvent(0.9f, "5", isDropped = true)
        ),
        outputMarbles = listOf(
            MarbleEvent(0.3f, "2"), MarbleEvent(0.7f, "4")
        ),
        operatorLabel = ".filter { it % 2 == 0 }",
        operatorSublabel = "odd values dropped"
    )

    DemoType.DEBOUNCE -> MarbleDiagramData(
        inputMarbles = listOf(
            MarbleEvent(0.05f, "a", isDropped = true),
            MarbleEvent(0.15f, "b", isDropped = true),
            MarbleEvent(0.28f, "c"),
            MarbleEvent(0.5f,  "d", isDropped = true),
            MarbleEvent(0.62f, "e"),
            MarbleEvent(0.88f, "f")
        ),
        outputMarbles = listOf(
            MarbleEvent(0.38f, "c"),
            MarbleEvent(0.72f, "e"),
            MarbleEvent(0.98f, "f")
        ),
        operatorLabel = ".debounce(300ms)",
        operatorSublabel = "rapid bursts suppressed"
    )

    DemoType.DISTINCT_UNTIL_CHANGED -> MarbleDiagramData(
        inputMarbles = listOf(
            MarbleEvent(0.1f, "1"),
            MarbleEvent(0.25f, "1", isDropped = true),
            MarbleEvent(0.4f, "2"),
            MarbleEvent(0.55f, "2", isDropped = true),
            MarbleEvent(0.75f, "3")
        ),
        outputMarbles = listOf(
            MarbleEvent(0.1f, "1"),
            MarbleEvent(0.4f, "2"),
            MarbleEvent(0.75f, "3")
        ),
        operatorLabel = ".distinctUntilChanged()",
        operatorSublabel = "consecutive dupes dropped"
    )

    DemoType.FLAT_MAP_LATEST -> MarbleDiagramData(
        inputMarbles = listOf(
            MarbleEvent(0.1f, "A"),
            MarbleEvent(0.4f, "B"),
            MarbleEvent(0.75f, "C")
        ),
        outputMarbles = listOf(
            MarbleEvent(0.55f, "B→", isDropped = false),
            MarbleEvent(0.95f, "C→")
        ),
        operatorLabel = ".flatMapLatest { }",
        operatorSublabel = "A cancelled by B, B cancelled by C"
    )

    DemoType.FLAT_MAP_MERGE -> MarbleDiagramData(
        inputMarbles = listOf(
            MarbleEvent(0.1f, "1"),
            MarbleEvent(0.3f, "2"),
            MarbleEvent(0.5f, "3")
        ),
        outputMarbles = listOf(
            MarbleEvent(0.55f, "R2"),
            MarbleEvent(0.65f, "R1"),
            MarbleEvent(0.82f, "R3")
        ),
        operatorLabel = ".flatMapMerge { }",
        operatorSublabel = "all run concurrently, unordered"
    )

    DemoType.ZIP -> MarbleDiagramData(
        inputMarbles = listOf(
            MarbleEvent(0.1f, "A"), MarbleEvent(0.4f, "B"), MarbleEvent(0.7f, "C")
        ),
        outputMarbles = listOf(
            MarbleEvent(0.35f, "A+1"),
            MarbleEvent(0.6f,  "B+2"),
            MarbleEvent(0.85f, "C+3")
        ),
        operatorLabel = ".zip(flowB) { a, b -> }",
        operatorSublabel = "paired 1:1, waits for both"
    )

    DemoType.COMBINE -> MarbleDiagramData(
        inputMarbles = listOf(
            MarbleEvent(0.1f, "A"), MarbleEvent(0.5f, "B"), MarbleEvent(0.8f, "C")
        ),
        outputMarbles = listOf(
            MarbleEvent(0.25f, "A+1"),
            MarbleEvent(0.5f,  "B+1"),
            MarbleEvent(0.65f, "B+2"),
            MarbleEvent(0.8f,  "C+2"),
            MarbleEvent(0.9f,  "C+3")
        ),
        operatorLabel = "combine(flowA, flowB) { }",
        operatorSublabel = "emits on any update"
    )

    DemoType.BUFFER -> MarbleDiagramData(
        inputMarbles = listOf(
            MarbleEvent(0.1f, "1"), MarbleEvent(0.2f, "2"), MarbleEvent(0.3f, "3"),
            MarbleEvent(0.4f, "4"), MarbleEvent(0.5f, "5"), MarbleEvent(0.6f, "6")
        ),
        outputMarbles = listOf(
            MarbleEvent(0.45f, "[1,2,3]"),
            MarbleEvent(0.85f, "[4,5,6]")
        ),
        operatorLabel = ".buffer(capacity = 3)",
        operatorSublabel = "batched into chunks"
    )

    DemoType.COLLECT_LATEST -> MarbleDiagramData(
        inputMarbles = listOf(
            MarbleEvent(0.1f, "1", isDropped = true),
            MarbleEvent(0.3f, "2", isDropped = true),
            MarbleEvent(0.5f, "3", isDropped = true),
            MarbleEvent(0.7f, "4", isDropped = true),
            MarbleEvent(0.9f, "5")
        ),
        outputMarbles = listOf(
            MarbleEvent(0.98f, "5")
        ),
        operatorLabel = ".collectLatest { }",
        operatorSublabel = "only last value fully processed"
    )
}

// --- The marble lane Canvas ---

@Composable
private fun MarbleLane(
    marbles: List<MarbleEvent>,
    playhead: Float,       // 0f..1f
    marbleColor: Color,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier.height(56.dp)) {
        val W = size.width
        val H = size.height
        val trackY = H * 0.48f
        val padH = 28.dp.toPx()

        fun xOf(t: Float) = padH + t * (W - padH * 2f)

        // Timeline base
        drawLine(
            color = ColorTimeline,
            start = Offset(padH, trackY),
            end = Offset(W - padH, trackY),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Progress fill
        val pX = xOf(playhead.coerceIn(0f, 1f))
        drawLine(
            color = marbleColor.copy(alpha = 0.4f),
            start = Offset(padH, trackY),
            end = Offset(pX, trackY),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Arrow head
        val arrowX = W - padH + 10.dp.toPx()
        val arrowPath = Path().apply {
            moveTo(arrowX, trackY)
            lineTo(arrowX - 8.dp.toPx(), trackY - 5.dp.toPx())
            lineTo(arrowX - 8.dp.toPx(), trackY + 5.dp.toPx())
            close()
        }
        drawPath(arrowPath, color = ColorTimeline.copy(alpha = 0.5f))

        // Playhead
        if (playhead < 0.99f) {
            drawLine(
                color = ColorPlayhead.copy(alpha = 0.2f),
                start = Offset(pX, 4f),
                end = Offset(pX, H - 4f),
                strokeWidth = 1.dp.toPx(),
                pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                    floatArrayOf(4f, 4f), 0f
                )
            )
        }

        // Marbles
        marbles.forEach { marble ->
            if (marble.time > playhead) return@forEach

            val x = xOf(marble.time)
            val r = 13.dp.toPx()
            val color = if (marble.isDropped) ColorDropped else marbleColor

            // Glow
            drawCircle(color = color.copy(alpha = 0.12f), radius = r + 4.dp.toPx(), center = Offset(x, trackY + 2))
            // Fill
            drawCircle(color = color.copy(alpha = 0.18f), radius = r, center = Offset(x, trackY))
            // Border
            drawCircle(color = color, radius = r, center = Offset(x, trackY), style = Stroke(1.5.dp.toPx()))

            if (marble.isDropped) {
                // X mark
                val cr = 5.dp.toPx()
                drawLine(color = color.copy(alpha = 0.8f), start = Offset(x - cr, trackY - cr), end = Offset(x + cr, trackY + cr), strokeWidth = 1.5.dp.toPx(), cap = StrokeCap.Round)
                drawLine(color = color.copy(alpha = 0.8f), start = Offset(x + cr, trackY - cr), end = Offset(x - cr, trackY + cr), strokeWidth = 1.5.dp.toPx(), cap = StrokeCap.Round)
            } else {
                // Value label
                val label = if (marble.label.length > 5) marble.label.take(4) + "…" else marble.label
                val measured = textMeasurer.measure(
                    label,
                    style = TextStyle(fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = color, fontWeight = FontWeight.Bold)
                )
                drawText(measured, topLeft = Offset(x - measured.size.width / 2f, trackY - measured.size.height / 2f))
            }

            // Time tick below
            val tick = "t${(marble.time * 10).toInt()}"
            val tickMeasured = textMeasurer.measure(
                tick,
                style = TextStyle(fontSize = 8.sp, fontFamily = FontFamily.Monospace, color = ColorTickLabel)
            )
            drawText(tickMeasured, topLeft = Offset(x - tickMeasured.size.width / 2f, trackY + r + 4.dp.toPx()))
        }
    }
}

// --- Operator box in the middle ---

@Composable
private fun OperatorBox(label: String, sublabel: String, accent: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(accent.copy(alpha = 0.08f))
            .border(1.dp, accent.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left connector arrow
        Text("↓", style = TextStyle(color = accent.copy(alpha = 0.6f), fontSize = 14.sp))
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = accent
                )
            )
            if (sublabel.isNotBlank()) {
                Text(
                    text = sublabel,
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = accent.copy(alpha = 0.6f),
                        letterSpacing = 0.3.sp
                    )
                )
            }
        }
        Text("↓", style = TextStyle(color = accent.copy(alpha = 0.6f), fontSize = 14.sp))
    }
}

// --- Legend ---

@Composable
private fun MarbleLegend() {
    Row(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendDot(ColorInputMarble); Spacer(Modifier.width(4.dp))
        Text("input", style = TextStyle(fontSize = 10.sp, color = Color(0xFF8888AA)))
        Spacer(Modifier.width(14.dp))
        LegendDot(ColorOutputMarble); Spacer(Modifier.width(4.dp))
        Text("output", style = TextStyle(fontSize = 10.sp, color = Color(0xFF8888AA)))
        Spacer(Modifier.width(14.dp))
        LegendDot(ColorDropped); Spacer(Modifier.width(4.dp))
        Text("dropped", style = TextStyle(fontSize = 10.sp, color = Color(0xFF8888AA)))
    }
}

@Composable
private fun LegendDot(color: Color) {
    Canvas(modifier = Modifier
        .width(8.dp)
        .height(8.dp)) {
        drawCircle(color = color.copy(alpha = 0.3f), radius = size.minDimension / 2)
        drawCircle(color = color, radius = size.minDimension / 2, style = Stroke(1.dp.toPx()))
    }
}

// --- The main public Composable to use in detail screen ---

@Composable
fun MarbleDiagram(
    demoType: DemoType,
    accent: Color,
    modifier: Modifier = Modifier
) {
    val data = marbleDiagramDataFor(demoType)

    val infiniteTransition = rememberInfiniteTransition(label = "marble_playhead")
    val playhead by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "playhead"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF0D1117))
            .border(1.dp, Color(0xFF30363D), RoundedCornerShape(14.dp))
            .padding(horizontal = 8.dp, vertical = 12.dp)
    ) {
        // Input label
        Text(
            text = "INPUT",
            modifier = Modifier.padding(horizontal = 8.dp),
            style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp, color = ColorInputMarble.copy(alpha = 0.7f))
        )

        MarbleLane(
            marbles = data.inputMarbles,
            playhead = playhead,
            marbleColor = ColorInputMarble,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        OperatorBox(
            label = data.operatorLabel,
            sublabel = data.operatorSublabel,
            accent = accent
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "OUTPUT",
            modifier = Modifier.padding(horizontal = 8.dp),
            style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp, color = ColorOutputMarble.copy(alpha = 0.7f))
        )

        MarbleLane(
            marbles = data.outputMarbles,
            playhead = playhead,
            marbleColor = ColorOutputMarble,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        MarbleLegend()
    }
}