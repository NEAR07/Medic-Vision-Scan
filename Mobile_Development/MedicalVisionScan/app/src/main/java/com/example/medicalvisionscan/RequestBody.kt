package com.example.medicalvisionscan

import com.google.gson.annotations.SerializedName

data class RequestBody(
    val data: RequestData
)

data class RequestData(
    val filename: String = "cataract.jpg",
    @SerializedName("b64_img_data")
    val imageData: String
)
