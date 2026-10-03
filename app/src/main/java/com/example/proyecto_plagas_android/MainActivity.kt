package com.example.proyecto_plagas_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_plagas_android.ui.navigation.AgroScanNavGraph
import com.example.proyecto_plagas_android.ui.state.ThemeMode
import com.example.proyecto_plagas_android.ui.theme.AgroScanTheme
import com.example.proyecto_plagas_android.ui.viewmodel.AgroScanViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val agroScanViewModel: AgroScanViewModel = viewModel()
            val settingsState by agroScanViewModel.settingsUiState.collectAsState()

            val isDarkTheme = when (settingsState.themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            AgroScanTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AgroScanNavGraph(viewModel = agroScanViewModel)
                }
            }
        }
    }
}
