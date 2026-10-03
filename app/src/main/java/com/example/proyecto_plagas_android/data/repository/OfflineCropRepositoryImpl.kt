package com.example.proyecto_plagas_android.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.example.proyecto_plagas_android.data.ml.OnnxPlantClassifier
import com.example.proyecto_plagas_android.data.model.CropAnalysisResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OfflineCropRepositoryImpl(
    context: Context,
) : GeminiRepository {

    private val classifier = OnnxPlantClassifier(context)

    override suspend fun analyzeCropImage(
        bitmap: Bitmap?,
        notes: String?,
    ): Result<CropAnalysisResult> = withContext(Dispatchers.Default) {
        try {
            val result = classifier.analyzeAndMapToCropResult(
                bitmap = bitmap,
                notes = notes,
                imageUri = null,
            )
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun analyzeCropImageWithUri(
        bitmap: Bitmap?,
        notes: String?,
        imageUri: String?,
    ): Result<CropAnalysisResult> = withContext(Dispatchers.Default) {
        try {
            val result = classifier.analyzeAndMapToCropResult(
                bitmap = bitmap,
                notes = notes,
                imageUri = imageUri,
            )
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
