package com.example.proyecto_plagas_android.ui.state

import com.example.proyecto_plagas_android.data.model.CropAnalysisResult

sealed interface AnalysisUiState {
    data object Idle : AnalysisUiState
    data object Loading : AnalysisUiState
    data class Success(val result: CropAnalysisResult) : AnalysisUiState
    data class Error(val message: String) : AnalysisUiState
}

enum class AlertType { WARNING, INFO, TIP }

data class PestAlert(
    val id: String,
    val type: AlertType,
    val title: String,
    val description: String,
)

data class AgriculturalNews(
    val id: String,
    val title: String,
    val category: String,
    val date: String,
    val summary: String,
    val source: String,
)

data class DashboardUiState(
    val recentAnalyses: List<CropAnalysisResult> = emptyList(),
    val totalScansCount: Int = 0,
    val healthyCropsCount: Int = 0,
    val diseasedCropsCount: Int = 0,
    val overallHealthPercentage: Int = 100,
    val activePestAlerts: List<PestAlert> = emptyList(),
    val regionalNews: List<AgriculturalNews> = emptyList(),
    val isLoading: Boolean = false,
)
