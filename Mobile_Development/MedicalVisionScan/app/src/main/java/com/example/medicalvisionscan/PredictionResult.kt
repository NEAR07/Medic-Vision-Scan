package com.example.medicalvisionscan

data class PredictionResult(
    val success: Boolean,
    val data: PredictionData
)

data class PredictionData(
    val prediction: String,
    val success: Boolean
)