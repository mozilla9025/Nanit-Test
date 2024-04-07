package app.test.nanit.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.test.nanit.ui.screen.BirthdayScreen
import app.test.nanit.ui.screen.BirthdayViewModel
import app.test.nanit.ui.screen.MainScreen
import app.test.nanit.ui.theme.NanitTestTheme
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface ViewModelFactoryProvider {
        fun birthdayViewModelFactory(): BirthdayViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NanitTestTheme {
                val navController = rememberNavController()
                Scaffold { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = NavRoute.Main.destination,
                        modifier = Modifier.padding(padding)
                    ) {
                        composable(route = NavRoute.Main.destination) {
                            MainScreen(navController)
                        }
                        composable(
                            route = NavRoute.Birthday.destination,
                            arguments = NavRoute.Birthday.args
                        ) {
                            BirthdayScreen(
                                navController = navController,
                                birthdayId = it.arguments?.getString("birthdayId", "") ?: ""
                            )
                        }
                    }
                }
            }
        }
    }
}
