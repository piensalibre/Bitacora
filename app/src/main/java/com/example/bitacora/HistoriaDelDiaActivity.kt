package com.example.bitacora

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class HistoriaDelDiaActivity : AppCompatActivity() {

    private lateinit var historiasTextView: TextView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historia_del_dia)

        historiasTextView = findViewById(R.id.historiasTextView)

        // Obtener el email del usuario
        val sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail", null)

        if (userEmail != null) {
            cargarHistoriasDelDia(userEmail)
        } else {
            Toast.makeText(this, "No se encontró el correo del usuario", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarHistoriasDelDia(email: String) {
        db.collection("bitacoras")
            .whereEqualTo("correoUsuario", email) // Filtrar por el correo del usuario
            .orderBy("timestamp") // Ordenar por fecha
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    historiasTextView.text = "No se encontraron historias del día."
                    return@addOnSuccessListener
                }

                val historias = StringBuilder()
                for (document in documents) {
                    val historia = document.getString("historiaDelDia") ?: "Sin contenido"
                    val fecha = document.getTimestamp("timestamp")?.toDate() ?: "Fecha desconocida"
                    historias.append("Fecha: $fecha\nHistoria: $historia\n\n-----------------\n\n")
                }
                historiasTextView.text = historias.toString()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar las historias: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
