package com.example.proyecto_plagas_android.domain.usecase

import android.graphics.Bitmap
import com.example.proyecto_plagas_android.data.model.CropAnalysisResult
import com.example.proyecto_plagas_android.data.repository.GeminiRepository

class AnalyzeCropUseCase(
    private val repository: GeminiRepository,
) {
    suspend operator fun invoke(
        bitmap: Bitmap?,
        notes: String?,
        imageUri: String? = null,
    ): Result<CropAnalysisResult> {
        val result = repository.analyzeCropImage(bitmap, notes)
        return result.map { analysis ->
            analysis.copy(
                imageUri = imageUri ?: analysis.imageUri,
                notes = notes ?: analysis.notes,
            )
        }
    }
}
