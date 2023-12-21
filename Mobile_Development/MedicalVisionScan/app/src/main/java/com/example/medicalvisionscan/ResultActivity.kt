package com.example.medicalvisionscan

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.medicalvisionscan.databinding.ActivityResultBinding
import java.util.Locale

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val result = intent.getStringExtra("result")!!
        val imageUri: Uri = Uri.parse(intent.getStringExtra("uri"))

        binding.apply {
            ivResult.setImageURI(imageUri)
            textViewvResult.text = result.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
            
            description.apply {
                setCollapsedText("Read More")
                setCollapsedTextColor(R.color.black)
                setExpandedText("Read Less")
                setTrimLines(3)
            }
            prevention.apply {
                setCollapsedText("Read More")
                setCollapsedTextColor(R.color.black)
                setExpandedText("Read Less")
                setTrimLines(3)
            }

            if(result.lowercase() != "normal") {
                tvResultMsg.text = getString(R.string.cataract)
                description.text = getString(R.string.cataract_desc)
                prevention.text = getString(R.string.cataract_prevention)
            } else {
                tvResultMsg.text = getString(R.string.normal)
                description.text = getString(R.string.normal_desc)
                prevention.text = getString(R.string.normal_prevention)
            }
        }

    }
}