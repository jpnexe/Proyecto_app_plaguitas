package com.example.proyecto_plagas_android.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DiseaseDetail(
    val commonName: String,
    val scientificName: String,
    val severityLevel: SeverityLevel,
    val confidencePercentage: Int,
    val description: String,
    val symptoms: List<String> = emptyList(),
    val treatment: TreatmentMeasures,
)
