package com.example.bitacora

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class Agradecimientos : AppCompatActivity() {

    private lateinit var agradecimientosContainer: LinearLayout
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agradecimientos)

        agradecimientosContainer = findViewById(R.id.agradecimientosContainer)

        // Recuperar el correo del usuario desde SharedPreferences
        val sharedPreferences = getSharedPreferences("userSession", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail", null)

        if (userEmail != null) {
            fetchAgradecimientos(userEmail)
        } else {
            Toast.makeText(this, "No se encontró el correo del usuario", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchAgradecimientos(email: String) {
        db.collection("bitacoras")
            .whereEqualTo("correoUsuario", email)
            .orderBy("timestamp") // Crear índice en Firestore
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "No hay datos de Agradecimientos", Toast.LENGTH_SHORT).show()
                } else {
                    for (document in documents) {
                        val agradecimientos = document.getString("agradecimientos")
                        agradecimientos?.let {
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
                setMargins(0, 16, 0, 16)
            }
            text = texto
            textSize = 16f
            gravity = Gravity.START
        }
        agradecimientosContainer.addView(textView)
    }
}
