package com.example.medicalvisionscan

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/open/predict")
    fun predict(
        @Body requestBody: RequestBody
    ) : Call<PredictionResult>
}