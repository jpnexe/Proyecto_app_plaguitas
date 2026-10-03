package com.example.proyecto_plagas_android

import android.graphics.Bitmap
import com.example.proyecto_plagas_android.data.model.CropAnalysisResult
import com.example.proyecto_plagas_android.data.model.DiseaseDetail
import com.example.proyecto_plagas_android.data.model.SeverityLevel
import com.example.proyecto_plagas_android.data.model.TreatmentMeasures
import com.example.proyecto_plagas_android.data.repository.GeminiRepository
import com.example.proyecto_plagas_android.domain.usecase.AnalyzeCropUseCase
import com.example.proyecto_plagas_android.ui.state.AnalysisUiState
import com.example.proyecto_plagas_android.ui.state.ThemeMode
import com.example.proyecto_plagas_android.ui.viewmodel.AgroScanViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AgroScanViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private class FakeGeminiRepository(
        private val shouldFail: Boolean = false,
    ) : GeminiRepository {
        override suspend fun analyzeCropImage(
            bitmap: Bitmap?,
            notes: String?,
        ): Result<CropAnalysisResult> {
            return if (shouldFail) {
                Result.failure(Exception("Error de red simulado"))
            } else {
                Result.success(
                    CropAnalysisResult(
                        id = "test-123",
                        plantName = "Café",
                        diseaseDetail = DiseaseDetail(
                            commonName = "Roya del Café",
                            scientificName = "Hemileia vastatrix",
                            severityLevel = SeverityLevel.HIGH,
                            confidencePercentage = 95,
                            description = "Pústulas de roya en follaje",
                            symptoms = listOf("Pústulas anaranjadas"),
                            treatment = TreatmentMeasures(
                                cultural = listOf("Poda"),
                                biological = listOf("Bacillus subtilis"),
                                chemical = listOf("Cobre"),
                            ),
                        ),
                        notes = notes,
                    ),
                )
            }
        }
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialDashboardState_loadsMockData() {
        val repository = FakeGeminiRepository()
        val useCase = AnalyzeCropUseCase(repository)
        val viewModel = AgroScanViewModel(useCase)

        val dashboardState = viewModel.dashboardUiState.value
        assertTrue(dashboardState.recentAnalyses.isNotEmpty())
        assertEquals(3, dashboardState.totalScansCount)
        assertEquals(1, dashboardState.healthyCropsCount)
        assertEquals(2, dashboardState.diseasedCropsCount)
        assertEquals(33, dashboardState.overallHealthPercentage)
    }

    @Test
    fun selectAnalysis_updatesAnalysisUiStateToSuccess() {
        val repository = FakeGeminiRepository()
        val useCase = AnalyzeCropUseCase(repository)
        val viewModel = AgroScanViewModel(useCase)

        viewModel.selectAnalysis("mock-1")

        val state = viewModel.analysisUiState.value
        assertTrue(state is AnalysisUiState.Success)
        assertEquals("mock-1", (state as AnalysisUiState.Success).result.id)
    }

    @Test
    fun resetAnalysisState_resetsToIdle() {
        val repository = FakeGeminiRepository()
        val useCase = AnalyzeCropUseCase(repository)
        val viewModel = AgroScanViewModel(useCase)

        viewModel.selectAnalysis("mock-1")
        viewModel.resetAnalysisState()

        val state = viewModel.analysisUiState.value
        assertTrue(state is AnalysisUiState.Idle)
    }

    @Test
    fun saveAnalysisToHistory_addsNewEntry() {
        val repository = FakeGeminiRepository()
        val useCase = AnalyzeCropUseCase(repository)
        val viewModel = AgroScanViewModel(useCase)

        val newResult = CropAnalysisResult(
            id = "new-scan-1",
            plantName = "Maíz",
            diseaseDetail = DiseaseDetail(
                commonName = "Planta Saludable",
                scientificName = "N/A",
                severityLevel = SeverityLevel.HEALTHY,
                confidencePercentage = 99,
                description = "Sano",
                symptoms = emptyList(),
                treatment = TreatmentMeasures(),
            ),
        )

        viewModel.saveAnalysisToHistory(newResult)

        val dashboardState = viewModel.dashboardUiState.value
        assertEquals(4, dashboardState.totalScansCount)
        assertEquals(2, dashboardState.healthyCropsCount)
        assertEquals("new-scan-1", dashboardState.recentAnalyses.first().id)
    }

    @Test
    fun updateThemeMode_updatesSettingsState() {
        val repository = FakeGeminiRepository()
        val useCase = AnalyzeCropUseCase(repository)
        val viewModel = AgroScanViewModel(useCase)

        viewModel.updateThemeMode(ThemeMode.DARK)

        assertEquals(
            ThemeMode.DARK,
            viewModel.settingsUiState.value.themeMode,
        )
    }

    @Test
    fun clearAnalysisHistory_clearsDashboardAnalyses() {
        val repository = FakeGeminiRepository()
        val useCase = AnalyzeCropUseCase(repository)
        val viewModel = AgroScanViewModel(useCase)

        viewModel.clearAnalysisHistory()

        val dashboardState = viewModel.dashboardUiState.value
        assertEquals(0, dashboardState.totalScansCount)
        assertTrue(dashboardState.recentAnalyses.isEmpty())
    }
}
