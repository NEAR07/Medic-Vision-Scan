package com.example.medicalvisionscan

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.ImagePickerMode
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.imagepicker.features.registerImagePicker
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var button: LinearLayout
    private lateinit var progress: ProgressBar
    private lateinit var result: TextView

    private val launcher = registerImagePicker {
        if(it.isNotEmpty()) {
            val image = it[0]
            val image64 = imageToBase64(image.path)

            predict(image64, image.uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNetwork()

        val config = ImagePickerConfig {
            mode = ImagePickerMode.SINGLE
            language = "in"
            returnMode = ReturnMode.ALL

        }

        result = findViewById(R.id.tv_result)
        progress = findViewById(R.id.progress)
        button = findViewById(R.id.btn_scan)
        button.setOnClickListener {
            launcher.launch(config)
        }
    }

    private fun initNetwork() {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okhttp = OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit
            .Builder()
            .baseUrl("http://auth.saddan.my.id")
            .client(okhttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    private fun imageToBase64(path: String): String {
        val bm = BitmapFactory.decodeFile(path)
        val baos = ByteArrayOutputStream()
        val newBm = Bitmap.createScaledBitmap(bm, 240, 360, false)
        newBm.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun predict(image64: String, uri: Uri) {
        result.visibility = GONE
        progress.visibility = VISIBLE
        button.visibility = GONE

        val requestData = RequestData(imageData = image64)
        val requestBody = RequestBody(requestData)
        apiService.predict(requestBody).enqueue(object: Callback<PredictionResult> {
            override fun onResponse(call: Call<PredictionResult>, response: Response<PredictionResult>) {
                progress.visibility = GONE
                button.visibility = VISIBLE

                if(response.isSuccessful) {
                    val data = response.body()!!

                    val i = Intent(this@MainActivity, ResultActivity::class.java)
                    i.putExtra("result", data.data.prediction)
                    i.putExtra("uri", uri.toString())

                    startActivity(i)
                }
            }

            override fun onFailure(call: Call<PredictionResult>, t: Throwable) {
                progress.visibility = GONE
                button.visibility = VISIBLE

                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}