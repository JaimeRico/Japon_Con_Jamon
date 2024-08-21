package com.example.japon_con_jamon.activitys

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.japon_con_jamon.R
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener{
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var button: Button
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Instanciar TextToSpeech
        textToSpeech = TextToSpeech(this,this)

        // Obtener referencias a las vistas
        button = findViewById(R.id.boton1)
        textView = findViewById(R.id.texto1)

        // Configurar el click listener del botón
        button.setOnClickListener {
            // Leer el texto del TextView con TextToSpeech
            val textView = findViewById<TextView>(R.id.texto1)
            val fullText = textView.text.toString()

            // Máximo número de caracteres que puede manejar TextToSpeech
            val maxLength = TextToSpeech.getMaxSpeechInputLength()
            if (fullText.length > maxLength) {
                // Dividimos el texto en partes
                val chunks = fullText.chunked(maxLength)
                for (chunk in chunks) {
                    textToSpeech.speak(chunk, TextToSpeech.QUEUE_ADD, null, null)
                }
            } else {
                textToSpeech.speak(fullText, TextToSpeech.QUEUE_FLUSH, null, null)
            }

        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Configurar el idioma para el TextToSpeech, por ejemplo Español (España)
            val result = textToSpeech.setLanguage(Locale("es", "ES"))

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Manejar el caso donde el idioma no es soportado
                println("El idioma no está soportado")
            }
        } else {
            // Manejar el error de inicialización
            println("Inicialización de TextToSpeech fallida")
        }
    }

    override fun onDestroy() {
        // Liberar recursos de TextToSpeech cuando ya no se necesite
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}