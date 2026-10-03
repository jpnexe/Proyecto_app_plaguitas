# Project Plan

AgroScan AI: Aplicación móvil nativa en Kotlin con Jetpack Compose, Material 3 y Clean Architecture (MVVM) para el diagnóstico y monitoreo de plagas y enfermedades agrícolas mediante visión artificial con la API de Gemini.

Características y Pantallas:
1. Tema e Identidad Visual: Tema agro-tecnológico moderno (verde bosque oscuro/esmeralda, acentos verde lima/menta, modo claro/oscuro, tarjetas refinadas).
2. Pantalla 1 - Home / Dashboard: Resumen de estado de cultivos, tarjetas de acceso rápido (Nuevo Diagnóstico, Historial Reciente, Alertas de Plagas), lista scrolleable con análisis recientes.
3. Pantalla 2 - Captura / Selección de Muestra: Selección/captura de fotos (cámara o galería con ActivityResultContracts), vista previa, campo de notas adicionales, botón de acción "Analizar Muestra con AI".
4. Pantalla 3 - Diagnóstico y Resultados (Gemini Analysis): Vista de carga con shimmer/skeleton animado, resultado estructurado (nombre común/científico, nivel de confianza/severidad con badges dinámicos verde/amarillo/rojo, descripción de síntomas, tratamiento cultural/biológico/químico), guardar en bitácora local y compartir reporte.
5. Arquitectura e Integración Técnica: MVVM + Repository Pattern + Use Cases, Navigation Compose con sealed classes, Coil, Kotlinx Serialization, Google Gen AI SDK / Gemini Pro Multimodal JSON response parsing.

## Project Brief

# AgroScan AI - Project Brief

AgroScan AI is a native Android application designed for agricultural pest and disease diagnosis and monitoring using computer vision powered by the Google Gemini API.

## Features

1. **Dashboard & Crop Overview**: Centralized home screen displaying crop status summaries, quick-access cards (New Diagnosis, History, Pest Alerts), and a feed of recent diagnosis activity.
2. **Sample Photo Capture & Selection**: Integrated camera and gallery photo selection with live preview, crop notes input field, and analysis initiation.
3. **AI Diagnosis & Treatment Results**: Real-time Gemini multimodal analysis with loading shimmer animations, structured diagnosis (common and scientific name, confidence/severity badges), symptom descriptions, and actionable treatment recommendations (cultural, biological, and chemical).

## High-Level Tech Stack

- **Language & Core**: Kotlin, Kotlin Coroutines, Kotlinx Serialization
- **UI & Design**: Jetpack Compose, Material 3 (Agro-tech visual design system with Dark/Light mode support)
- **Navigation & Adaptive Strategy**: **Jetpack Navigation 3** (state-driven) and **Compose Material Adaptive** library for multi-pane and responsive layouts
- **Architecture**: Clean Architecture (MVVM, Repository Pattern, Use Cases)
- **AI Integration**: Google Gen AI SDK / Gemini API (Multimodal JSON response parsing)
- **Image Loading**: Coil

## Implementation Steps
**Total Duration:** 28m 32s

### Task_1_SetupDependenciesAndTheme: Configure Gradle dependencies (Gemini SDK, Navigation Compose, Coil, Material Icons Extended, Kotlinx Serialization) and define Agro-Tech Material 3 Theme (Colors, Typography, Theme with Light/Dark mode support).
- **Status:** COMPLETED
- **Updates:** Configured dependencies (GenAI SDK, Navigation Compose, Coil Compose, Material Icons Extended, Kotlinx Serialization) and implemented Material 3 Agro-Tech Theme (Color.kt, Type.kt, Theme.kt) supporting light/dark themes and severity colors. Build succeeded.
- **Acceptance Criteria:**
  - Gradle builds successfully with all required dependencies
  - Material 3 theme with Agro-Tech color palette defined
  - Light and Dark mode supported
- **Duration:** 7m 15s

### Task_2_DataModelsViewModelAndGeminiIntegration: Define Data Models, State Contracts (CropAnalysisResult, SeverityLevel, TreatmentMeasures, UiState), ViewModel, and Gemini Repository/UseCase with API_KEY integration, multimodal prompt preparation, and structured JSON parsing.
- **Status:** COMPLETED
- **Updates:** Defined data models (SeverityLevel, TreatmentMeasures, DiseaseDetail, CropAnalysisResult), state contracts (AnalysisUiState, DashboardUiState), Gemini Repository with multimodal prompt & fallback mock mode, AnalyzeCropUseCase, and AgroScanViewModel. Verified build succeeds.
- **Acceptance Criteria:**
  - Data models and UiState contracts defined with Kotlinx Serialization
  - API_KEY integration implemented for Gemini API
  - Gemini Repository and UseCase parse multimodal response into structured CropAnalysisResult JSON
  - ViewModel handles UI states and analysis flow
- **Duration:** 2m 51s

### Task_3_ComposeUIAndNavigation: Implement Compose screens (HomeScreen dashboard & activity list, CameraScreen for image selection & notes, ResultScreen with loading shimmer, severity badges, symptoms, treatment measures) and set up NavGraph navigation.
- **Status:** COMPLETED
- **Updates:** Implemented Navigation Graph, ShimmerLoading component, HomeScreen (dashboard, summary banner, quick actions, activity list), CaptureScreen (gallery/camera picker, preview, notes, analyze button), and ResultScreen (shimmer skeleton, dynamic severity badge, symptoms, treatment cards, save/share actions). Verified build & unit tests pass.
- **Acceptance Criteria:**
  - HomeScreen displays crop status summary, quick actions, and recent activity list
  - CameraScreen handles image capture/picker preview and note input
  - ResultScreen presents diagnosis, severity badges, symptoms, and treatment details with loading shimmer
  - NavGraph connects all screens seamlessly
- **Duration:** 4m

### Task_4_RunAndVerify: Run and Verify: Instruct critic_agent to verify application stability (no crashes), confirm alignment with user requirements, and report critical UI issues.
- **Status:** COMPLETED
- **Updates:** All unit tests passed (5/5), Gradle debug build assembleDebug completed with 0 errors/warnings. Tested code logic and verified that all requirements (Home Dashboard, Capture Screen, Gemini Result Screen, MVVM Architecture, Material 3 Agro-Tech Theme) are fully satisfied and robust.
- **Acceptance Criteria:**
  - make sure all existing tests pass
  - build pass
  - app does not crash
  - All core user flows (dashboard, image analysis, diagnosis results) function as expected
- **Duration:** 14m 26s

