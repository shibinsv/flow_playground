package shibin.flowplayground.ui.theme.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import shibin.flowplayground.enums.DemoType
import shibin.flowplayground.ui.theme.BgPrimary
import shibin.flowplayground.ui.theme.detail.composable.CodeBlock
import shibin.flowplayground.ui.theme.detail.composable.DetailHero
import shibin.flowplayground.ui.theme.detail.composable.ImpactBanner
import shibin.flowplayground.ui.theme.detail.composable.LiveDemoPanel
import shibin.flowplayground.ui.theme.detail.composable.MarbleDiagram
import shibin.flowplayground.ui.theme.operatorAccentColor
import shibin.flowplayground.viewmodels.OperatorDetailViewModel
import shibin.flowplayground.voiceHelper.VoiceHelper


private val operatorIds = listOf(
    "map", "filter", "flatMapLatest", "flatMapMerge", "debounce",
    "distinctUntilChanged", "zip", "combine", "buffer", "collectLatest"
)

@Composable
fun DetailScreen(
    onBack: () -> Unit,
    viewModel: OperatorDetailViewModel = hiltViewModel()
) {
    var currentStepIndex by remember { mutableStateOf<Int?>(null) }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val operator = state.operator ?: return
    val accent = operatorAccentColor(operatorIds.indexOf(operator.id))

    val context = LocalContext.current
    val voiceHelper = remember {
        VoiceHelper(context)
    }

    DisposableEffect(Unit) {
        onDispose { voiceHelper.release() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 48.dp)
        ) {
            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BgPrimary)
                ) {
                    DetailHero(
                        operator = operator,
                        accent = accent,
                        onBack = onBack
                    )
                }
            }
            item { ImpactBanner(operator.impact, accent) }
            item { Spacer(Modifier.height(20.dp)) }
            item {
                Text(
                    text = "STEP EXPLANATION",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = accent,
                        letterSpacing = 1.5.sp,
                        fontFamily = FontFamily.Monospace
                    )
                )

                Text(
                    text = "Tap a step to listen",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp),
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color(0xFF8888AA)
                    )
                )

                Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                    operator.explanationSteps.forEachIndexed { index, step ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (index == currentStepIndex)
                                        accent.copy(alpha = 0.15f)
                                    else Color.Transparent
                                )
                                .clickable {
                                    currentStepIndex = index
                                    voiceHelper.speak(step)
                                }
                                .padding(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            // 🔊 Icon instead of text (clean UI)
                            Text(
                                text = if (index == currentStepIndex) "🔊" else "•",
                                fontSize = 14.sp,
                                color = accent
                            )

                            Spacer(Modifier.width(8.dp))

                            Text(
                                text = step,
                                fontSize = 14.sp,
                                color = if (index == currentStepIndex) accent else Color.White,
                                fontWeight = if (index == currentStepIndex)
                                    FontWeight.SemiBold
                                else FontWeight.Normal
                            )
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(20.dp)) }
            item {
                Text(
                    text = "MARBLE DIAGRAM",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = accent,
                        letterSpacing = 1.5.sp,
                        fontFamily = FontFamily.Monospace
                    )
                )
                MarbleDiagram(
                    demoType = operator.demoType,
                    accent = accent,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            item { Spacer(Modifier.height(20.dp)) }
            item { CodeBlock(operator.codeSnippet) }
            item { Spacer(Modifier.height(20.dp)) }
            item {
                LiveDemoPanel(
                    state = state,
                    operator = operator,
                    accent = accent,
                    onInputChanged = viewModel::onDemoInputChanged,
                    onRunDemo = viewModel::runDemo,
                    onStopDemo = viewModel::stopDemo
                )
            }
        }
    }
}


@Composable
fun rememberInputHint(demoType: DemoType): String? = remember(demoType) {
    when (demoType) {
        DemoType.MAP -> "Multiplier (e.g. 3)"
        DemoType.FILTER -> "Min threshold (e.g. 3)"
        DemoType.FLAT_MAP_LATEST -> "Queries comma-separated (e.g. a,ab,abc)"
        DemoType.DEBOUNCE -> "Keystrokes comma-separated (e.g. h,he,hel,hello)"
        else -> null
    }
}