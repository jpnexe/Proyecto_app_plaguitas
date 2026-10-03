package com.example.proyecto_plagas_android.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_plagas_android.data.model.CropAnalysisResult
import com.example.proyecto_plagas_android.data.model.DiseaseDetail
import com.example.proyecto_plagas_android.data.model.SeverityLevel
import com.example.proyecto_plagas_android.data.model.TreatmentMeasures
import com.example.proyecto_plagas_android.data.repository.GeminiRepositoryImpl
import com.example.proyecto_plagas_android.data.repository.OfflineCropRepositoryImpl
import com.example.proyecto_plagas_android.domain.usecase.AnalyzeCropUseCase
import com.example.proyecto_plagas_android.ui.state.AgriculturalNews
import com.example.proyecto_plagas_android.ui.state.AlertType
import com.example.proyecto_plagas_android.ui.state.AnalysisUiState
import com.example.proyecto_plagas_android.ui.state.DashboardUiState
import com.example.proyecto_plagas_android.ui.state.PestAlert
import com.example.proyecto_plagas_android.ui.state.SettingsUiState
import com.example.proyecto_plagas_android.ui.state.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class AgroScanViewModel(
    private val analyzeCropUseCase: AnalyzeCropUseCase = AnalyzeCropUseCase(GeminiRepositoryImpl()),
) : ViewModel() {

    private val _analysisUiState = MutableStateFlow<AnalysisUiState>(AnalysisUiState.Idle)
    val analysisUiState: StateFlow<AnalysisUiState> = _analysisUiState.asStateFlow()

    private val _dashboardUiState = MutableStateFlow(DashboardUiState())
    val dashboardUiState: StateFlow<DashboardUiState> = _dashboardUiState.asStateFlow()

    private val _settingsUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState.asStateFlow()

    private var textToSpeech: TextToSpeech? = null
    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    init {
        loadInitialMockData()
    }

    fun analyzeSample(
        context: Context,
        imageUri: Uri?,
        bitmap: Bitmap?,
        notes: String,
    ) {
        viewModelScope.launch {
            _analysisUiState.value = AnalysisUiState.Loading

            val resolvedBitmap = bitmap ?: imageUri?.let { loadBitmapFromUri(context, it) }

            val offlineUseCase = AnalyzeCropUseCase(OfflineCropRepositoryImpl(context))
            val result = offlineUseCase(
                bitmap = resolvedBitmap,
                notes = notes,
                imageUri = imageUri?.toString(),
            )

            result.fold(
                onSuccess = { analysisResult ->
                    _analysisUiState.value = AnalysisUiState.Success(analysisResult)
                    saveAnalysisToHistory(analysisResult)
                },
            ) { error ->
                _analysisUiState.value = AnalysisUiState.Error(
                    error.localizedMessage ?: "Error al analizar la muestra del cultivo",
                )
            }
        }
    }

    fun speakDiagnosis(context: Context, textToSpeak: String) {
        if (textToSpeech == null) {
            textToSpeech = TextToSpeech(context.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech?.language = Locale.forLanguageTag("es-ES")
                    speakTextInternal(textToSpeak)
                }
            }
        } else {
            speakTextInternal(textToSpeak)
        }
    }

    private fun speakTextInternal(text: String) {
        textToSpeech?.stop()
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "AgroScanTTS")
        _isSpeaking.value = true
    }

    fun stopSpeech() {
        textToSpeech?.stop()
        _isSpeaking.value = false
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }

    fun saveAnalysisToHistory(result: CropAnalysisResult) {
        val currentList = _dashboardUiState.value.recentAnalyses.toMutableList()
        // Avoid duplicate entries if ID exists
        val existingIndex = currentList.indexOfFirst { it.id == result.id }
        if (existingIndex != -1) {
            currentList[existingIndex] = result
        } else {
            currentList.add(0, result)
        }

        updateDashboardMetrics(currentList)
    }

    fun resetAnalysisState() {
        _analysisUiState.value = AnalysisUiState.Idle
    }

    fun selectAnalysis(analysisId: String) {
        _dashboardUiState.value.recentAnalyses.find { it.id == analysisId }?.let { found ->
            _analysisUiState.value = AnalysisUiState.Success(found)
        }
    }

    fun updateThemeMode(mode: ThemeMode) {
        _settingsUiState.value = _settingsUiState.value.copy(themeMode = mode)
    }

    fun updateApiKey(apiKey: String) {
        _settingsUiState.value = _settingsUiState.value.copy(
            apiKey = apiKey,
            userSavedMessage = "Clave API de Gemini actualizada",
        )
    }

    fun updateNotificationsEnabled(enabled: Boolean) {
        _settingsUiState.value = _settingsUiState.value.copy(notificationsEnabled = enabled)
    }

    fun updateDetailedDiagnosisEnabled(enabled: Boolean) {
        _settingsUiState.value = _settingsUiState.value.copy(detailedDiagnosisEnabled = enabled)
    }

    fun updatePreferredCrop(crop: String) {
        _settingsUiState.value = _settingsUiState.value.copy(preferredCrop = crop)
    }

    fun updateAreaUnit(unit: String) {
        _settingsUiState.value = _settingsUiState.value.copy(areaUnit = unit)
    }

    fun updateAlertFrequency(frequency: String) {
        _settingsUiState.value = _settingsUiState.value.copy(alertFrequency = frequency)
    }

    fun setShowClearHistoryDialog(show: Boolean) {
        _settingsUiState.value = _settingsUiState.value.copy(showClearHistoryDialog = show)
    }

    fun clearAnalysisHistory() {
        _dashboardUiState.value = _dashboardUiState.value.copy(
            recentAnalyses = emptyList(),
            totalScansCount = 0,
            healthyCropsCount = 0,
            diseasedCropsCount = 0,
            overallHealthPercentage = 100,
        )
        _settingsUiState.value = _settingsUiState.value.copy(
            showClearHistoryDialog = false,
            userSavedMessage = "Historial de diagnósticos eliminado",
        )
    }

    fun clearUserSavedMessage() {
        _settingsUiState.value = _settingsUiState.value.copy(userSavedMessage = null)
    }

    private fun updateDashboardMetrics(analyses: List<CropAnalysisResult>) {
        val totalScans = analyses.size
        val healthyCount = analyses.count { it.diseaseDetail.severityLevel == SeverityLevel.HEALTHY }
        val diseasedCount = totalScans - healthyCount

        val healthRatio = if (totalScans > 0) {
            ((healthyCount.toDouble() / totalScans.toDouble()) * 100).toInt()
        } else {
            100
        }

        _dashboardUiState.value = _dashboardUiState.value.copy(
            recentAnalyses = analyses,
            totalScansCount = totalScans,
            healthyCropsCount = healthyCount,
            diseasedCropsCount = diseasedCount,
            overallHealthPercentage = healthRatio,
        )
    }

    private fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun loadInitialMockData() {
        val now = System.currentTimeMillis()
        val hourInMs = 3600000L
        val dayInMs = 86400000L

        val mockList = listOf(
            CropAnalysisResult(
                id = "mock-1",
                timestamp = now - (2 * hourInMs),
                plantName = "Café (Coffea arabica)",
                diseaseDetail = DiseaseDetail(
                    commonName = "Roya del Café",
                    scientificName = "Hemileia vastatrix",
                    severityLevel = SeverityLevel.HIGH,
                    confidencePercentage = 94,
                    description = "Pústulas amarillentas-anaranjadas en envés foliar con riesgo de defoliación parcial.",
                    symptoms = listOf("Manchas cloróticas", "Polvillo anaranjado", "Defoliación leve"),
                    treatment = TreatmentMeasures(
                        cultural = listOf("Poda de ventilación", "Ajuste de sombra"),
                        biological = listOf("Bacillus subtilis"),
                        chemical = listOf("Fungicida a base de Cobre"),
                    ),
                ),
                notes = "Muestra tomada en lote El Mirador, sector norte.",
            ),
            CropAnalysisResult(
                id = "mock-2",
                timestamp = now - (1 * dayInMs),
                plantName = "Maíz (Zea mays)",
                diseaseDetail = DiseaseDetail(
                    commonName = "Planta Saludable",
                    scientificName = "N/A",
                    severityLevel = SeverityLevel.HEALTHY,
                    confidencePercentage = 98,
                    description = "Crecimiento vigoroso, follaje limpio y sin presencia de gusano cogollero.",
                    symptoms = listOf("Color verde intenso", "Turgencia foliar óptima"),
                    treatment = TreatmentMeasures(
                        cultural = listOf("Mantener fertilización nitrogenada regular"),
                        biological = listOf("Microorganismos benéficos del suelo"),
                        chemical = listOf("Ninguno"),
                    ),
                ),
                notes = "Evaluación de rutina parcela 4.",
            ),
            CropAnalysisResult(
                id = "mock-3",
                timestamp = now - (3 * dayInMs),
                plantName = "Tomate (Solanum lycopersicum)",
                diseaseDetail = DiseaseDetail(
                    commonName = "Tuta Absoluta",
                    scientificName = "Tuta absoluta",
                    severityLevel = SeverityLevel.CRITICAL,
                    confidencePercentage = 92,
                    description = "Galerías minadoras necróticas en hojas tiernas e inicio de perforación en frutos.",
                    symptoms = listOf("Hojas minadas translúcidas", "Excremento cerca del tallo"),
                    treatment = TreatmentMeasures(
                        cultural = listOf("Colocar mallas anti-insectos", "Eliminar residuos infectados"),
                        biological = listOf("Nesidiocoris tenuis"),
                        chemical = listOf("Spinosa biorracional"),
                    ),
                ),
                notes = "Invernadero B, detectado en surco 3.",
            ),
        )

        val mockAlerts = listOf(
            PestAlert(
                id = "alert-1",
                type = AlertType.WARNING,
                title = "ALERTA DE ROYA",
                description = "Riesgo alto en maíz. Revise hojas inferiores."
            ),
            PestAlert(
                id = "alert-2",
                type = AlertType.INFO,
                title = "Pronóstico de Lluvia",
                description = "Se esperan lluvias moderadas mañana por la tarde. Planifique la siembra."
            ),
            PestAlert(
                id = "alert-3",
                type = AlertType.TIP,
                title = "Consejo de Campo",
                description = "Aplique fertilizante antes de la lluvia para mejor absorción."
            )
        )

        val mockNews = listOf(
            AgriculturalNews(
                id = "news-1",
                title = "Directivas fitosanitarias contra la Roya en zonas de alta humedad",
                category = "Fitosanitario",
                date = "Hoy, 08:30 AM",
                summary = "Se aconseja regular la sombra del cafetal y aplicar bioinsumos de cobre tras las lluvias del fin de semana.",
                source = "Ministerio de Agricultura",
            ),
            AgriculturalNews(
                id = "news-2",
                title = "Boletín de precios de mercado regional hoy",
                category = "Mercado",
                date = "Ayer",
                summary = "El precio del quintal de café pergamino se mantiene estable con alta demanda de grano verde de calidad.",
                source = "Bolsa Agrícola Regional",
            ),
            AgriculturalNews(
                id = "news-3",
                title = "Talleres de manejo integrado de plagas en maíz y hortalizas",
                category = "Capacitación",
                date = "29 Sep",
                summary = "Capacitaciones gratuitas en campo sobre control biológico de gusano cogollero y polilla minadora.",
                source = "Asociación de Agricultores",
            ),
        )

        val totalScans = mockList.size
        val healthyCount = mockList.count { it.diseaseDetail.severityLevel == SeverityLevel.HEALTHY }
        val diseasedCount = totalScans - healthyCount
        val healthPercentage = ((healthyCount.toDouble() / totalScans.toDouble()) * 100).toInt()

        _dashboardUiState.value = DashboardUiState(
            recentAnalyses = mockList,
            totalScansCount = totalScans,
            healthyCropsCount = healthyCount,
            diseasedCropsCount = diseasedCount,
            overallHealthPercentage = healthPercentage,
            activePestAlerts = mockAlerts,
            regionalNews = mockNews,
        )
    }
}
