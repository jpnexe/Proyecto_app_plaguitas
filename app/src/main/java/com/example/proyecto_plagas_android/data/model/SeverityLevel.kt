package com.example.proyecto_plagas_android.data.model

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SeverityLevel(
    val label: String,
    val colorHex: String,
) {
    @SerialName("HEALTHY")
    HEALTHY("Saludable", "#2E7D32"),

    @Suppress("unused")
    @SerialName("LOW")
    LOW("Bajo", "#66BB6A"),

    @SerialName("MEDIUM")
    MEDIUM("Medio", "#F57C00"),

    @SerialName("HIGH")
    HIGH("Alto", "#E65100"),

    @SerialName("CRITICAL")
    CRITICAL("Crítico", "#C62828");

    val color: Color
        get() = Color(colorHex.toColorInt())
}
