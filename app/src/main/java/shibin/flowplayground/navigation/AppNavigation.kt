package shibin.flowplayground.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import shibin.flowplayground.ui.theme.detail.DetailScreen
import shibin.flowplayground.ui.theme.home.HomeScreen

// navigation/AppNavigation.kt
sealed class Screen(val route: String) {
    object HomeScreen : Screen("operator_list")
    object DetailScreen : Screen("operator_detail/{operatorId}") {
        fun createRoute(operatorId: String) = "operator_detail/$operatorId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(
                onOperatorClick = { operatorId ->
                    navController.navigate(Screen.DetailScreen.createRoute(operatorId))
                }
            )
        }
        composable(
            route = Screen.DetailScreen.route,
            arguments = listOf(navArgument("operatorId") { type = NavType.StringType })
        ) {
            DetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
