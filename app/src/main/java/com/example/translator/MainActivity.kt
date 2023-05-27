package com.example.translator

import android.os.Bundle
import android.view.View

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translation.getClient
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

class MainActivity : AppCompatActivity() {
    private lateinit var editTextTextToTranslate: EditText
    private lateinit var buttonTranslate: Button
    private lateinit var textViewTranslatedText: TextView
    private lateinit var translator: Translator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextTextToTranslate = findViewById(R.id.editTextTextToTranslate)
        buttonTranslate = findViewById(R.id.buttonTranslate)
        textViewTranslatedText = findViewById(R.id.textViewTranslatedText)

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.INDONESIAN)
            .build()
        translator = getClient(options)

        buttonTranslate.setOnClickListener {
            val textToTranslate = editTextTextToTranslate.text.toString()
            if (textToTranslate.isNotEmpty()) {
                translateText(textToTranslate)
            }
        }
    }

    private fun translateText(text: String) {
        translator.downloadModelIfNeeded(DownloadConditions.Builder().build())
            .addOnSuccessListener {
                translator.translate(text)
                    .addOnSuccessListener { translatedText ->
                        textViewTranslatedText.text = translatedText
                    }
                    .addOnFailureListener { exception ->
                        textViewTranslatedText.text = "Terjemahan gagal: ${exception.message}"
                    }
            }
            .addOnFailureListener { exception ->
                textViewTranslatedText.text = "Pengunduhan model gagal: ${exception.message}"
            }
    }
}
