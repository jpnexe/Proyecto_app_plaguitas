package com.example.proyecto_plagas_android.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Capture : Screen("capture")
    data object Settings : Screen("settings")
    data object Result : Screen("result/{analysisId}") {
        fun createRoute(analysisId: String = "current"): String = "result/$analysisId"
    }
}
