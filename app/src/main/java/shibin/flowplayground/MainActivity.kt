package shibin.flowplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import shibin.flowplayground.navigation.AppNavigation
import shibin.flowplayground.ui.theme.FlowPlaygroundTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlowPlaygroundTheme {
                AppNavigation()
            }
        }
    }
}
