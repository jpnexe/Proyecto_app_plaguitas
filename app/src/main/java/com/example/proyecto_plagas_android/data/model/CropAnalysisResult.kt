package com.example.proyecto_plagas_android.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CropAnalysisResult(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val plantName: String,
    val diseaseDetail: DiseaseDetail,
    val imageUri: String? = null,
    val notes: String? = null,
)
