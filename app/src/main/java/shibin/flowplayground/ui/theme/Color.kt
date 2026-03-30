package shibin.flowplayground.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


val BgPrimary = Color(0xFF0A0A0F)
val BgSurface = Color(0xFF16162A)
val BgCode = Color(0xFF0D1117)
val BgCodeHeader = Color(0xFF161B22)
val CodeBorder = Color(0xFF30363D)
val TextMuted = Color(0xFF8888AA)
val TextLight = Color(0xFFCCCCDD)
val TextCode = Color(0xFFE6EDF3)


fun operatorAccentColor(index: Int): Color {
    val colors = listOf(
        Color(0xFF25631B), // strong blue
        Color(0xFFDC2626), // deep red
        Color(0xFF7C3AED), // rich purple
        Color(0xFFD97706), // amber/orange
        Color(0xFF059669), // deep green (NOT mint)
        Color(0xFFDB2777), // pink-magenta
        Color(0xFF0891B2), // cyan (not minty)
        Color(0xFF4B5563)  // neutral grey
    )
    return colors[index % colors.size]
}