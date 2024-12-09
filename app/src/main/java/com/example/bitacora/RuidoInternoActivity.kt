package com.example.bitacora

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class RuidoInternoActivity : AppCompatActivity() {

    private lateinit var ruidoInternoContainer: LinearLayout
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ruido_interno)

        ruidoInternoContainer = findViewById(R.id.ruidoInternoContainer)

        // Recuperar el correo del usuario de SharedPreferences
        val sharedPreferences = getSharedPreferences("userSession", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail", null)

        if (userEmail != null) {
            fetchRuidoInterno(userEmail)
        } else {
            Toast.makeText(this, "No se encontró el correo del usuario", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchRuidoInterno(email: String) {
        db.collection("bitacoras")
            .whereEqualTo("correoUsuario", email)
            .orderBy("timestamp") // Asegúrate de que el índice exista
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "No hay datos de Ruido Interno", Toast.LENGTH_SHORT).show()
                } else {
                    for (document in documents) {
                        val ruidoInterno = document.getString("ruidoInterno")
                        ruidoInterno?.let {
                            agregarTexto(it)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al obtener los datos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun agregarTexto(texto: String) {
        val textView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 16) // Margen entre textos
            }
            text = texto
            textSize = 16f
            gravity = Gravity.START
        }
        ruidoInternoContainer.addView(textView)
    }
}
