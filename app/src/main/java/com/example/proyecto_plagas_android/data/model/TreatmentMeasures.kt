package com.example.proyecto_plagas_android.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TreatmentMeasures(
    val cultural: List<String> = emptyList(),
    val biological: List<String> = emptyList(),
    val chemical: List<String> = emptyList(),
)
