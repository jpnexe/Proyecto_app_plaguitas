package com.example.proyecto_plagas_android.ui.state

enum class ThemeMode(val label: String) {
    SYSTEM("Seguir el sistema"),
    LIGHT("Modo Claro"),
    DARK("Modo Oscuro"),
}

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val apiKey: String = "",
    val notificationsEnabled: Boolean = true,
    val detailedDiagnosisEnabled: Boolean = true,
    val preferredCrop: String = "General / Multicultivo",
    val areaUnit: String = "Hectáreas (ha)",
    val alertFrequency: String = "Diaria",
    val showClearHistoryDialog: Boolean = false,
    val userSavedMessage: String? = null,
)
