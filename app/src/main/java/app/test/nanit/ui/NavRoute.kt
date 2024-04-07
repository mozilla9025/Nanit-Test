package app.test.nanit.ui

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class NavRoute(val destination: String, val args: List<NamedNavArgument> = emptyList()) {
    data object Main : NavRoute("main")
    data object Birthday : NavRoute("birthday/{birthdayId}", listOf(navArgument("birthdayId") {
        type = NavType.StringType
    }))
}