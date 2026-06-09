package com.example.satellitetracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.satellitetracker.ui.screens.AlertsScreen
import com.example.satellitetracker.ui.screens.HomeScreen
import com.example.satellitetracker.ui.screens.LoginScreen
import com.example.satellitetracker.ui.screens.SatellitesScreen

// Rotas das telas do app
object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val SATELLITES = "satellites"
    const val ALERTS = "alerts"
}

/** Grafo de navegação do app: Login → Home → (Satélites | Alertas). */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    // Remove o login da pilha: o botão voltar não retorna para ele
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                onOpenSatellites = { navController.navigate(Routes.SATELLITES) },
                onOpenAlerts = { navController.navigate(Routes.ALERTS) }
            )
        }
        composable(Routes.SATELLITES) {
            SatellitesScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.ALERTS) {
            AlertsScreen(onBack = { navController.popBackStack() })
        }
    }
}
