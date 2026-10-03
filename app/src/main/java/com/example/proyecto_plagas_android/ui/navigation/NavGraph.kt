package com.example.proyecto_plagas_android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyecto_plagas_android.ui.screens.CaptureScreen
import com.example.proyecto_plagas_android.ui.screens.HomeScreen
import com.example.proyecto_plagas_android.ui.screens.ResultScreen
import com.example.proyecto_plagas_android.ui.screens.SettingsScreen
import com.example.proyecto_plagas_android.ui.viewmodel.AgroScanViewModel

@Composable
fun AgroScanNavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: AgroScanViewModel = viewModel(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToCapture = {
                    viewModel.resetAnalysisState()
                    navController.navigate(Screen.Capture.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToResult = { analysisId ->
                    viewModel.selectAnalysis(analysisId)
                    navController.navigate(Screen.Result.createRoute(analysisId))
                },
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }

        composable(Screen.Capture.route) {
            CaptureScreen(
                viewModel = viewModel,
                onNavigateToResult = {
                    navController.navigate(Screen.Result.createRoute("current")) {
                        popUpTo(Screen.Capture.route) { inclusive = true }
                    }
                },
            ) {
                navController.popBackStack()
            }
        }

        composable(
            route = Screen.Result.route,
            arguments = listOf(
                navArgument("analysisId") {
                    type = NavType.StringType
                    defaultValue = "current"
                },
            ),
        ) { backStackEntry ->
            val analysisId = backStackEntry.arguments?.getString("analysisId") ?: "current"
            if (analysisId != "current") {
                viewModel.selectAnalysis(analysisId)
            }
            ResultScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
            ) {
                navController.popBackStack(Screen.Home.route, inclusive = false)
            }
        }
    }
}
