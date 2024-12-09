package com.example.bitacora

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class BitacoraActivity : AppCompatActivity() {

    private lateinit var historiaDelDiaEditText: EditText
    private lateinit var agradecimientosEditText: EditText
    private lateinit var victoriasEditText: EditText
    private lateinit var aprendizajesEditText: EditText
    private lateinit var bloqueosEditText: EditText
    private lateinit var ruidoInternoEditText: EditText
    private lateinit var guardarBitacoraButton: Button
    private lateinit var retrospectivaButton: Button
    private lateinit var cerrarSesionButton: Button // Declaramos el botón de cerrar sesión
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bitacora)


        // Inicializar vistas
        historiaDelDiaEditText = findViewById(R.id.historiaDelDiaEditText)
        agradecimientosEditText = findViewById(R.id.agradecimientosEditText)
        victoriasEditText = findViewById(R.id.victoriasEditText)
        aprendizajesEditText = findViewById(R.id.aprendizajesEditText)
        bloqueosEditText = findViewById(R.id.bloqueosEditText)
        ruidoInternoEditText = findViewById(R.id.ruidoInternoEditText)
        guardarBitacoraButton = findViewById(R.id.guardarBitacoraButton)
        retrospectivaButton = findViewById(R.id.retrospectivaButton)
        cerrarSesionButton = findViewById(R.id.cerrarSesionButton) // Inicializamos el botón de cerrar sesión

        // Botón para guardar los datos en Firestore
        guardarBitacoraButton.setOnClickListener {
            val historiaDelDia = historiaDelDiaEditText.text.toString()
            val agradecimientos = agradecimientosEditText.text.toString()
            val victorias = victoriasEditText.text.toString()
            val aprendizajes = aprendizajesEditText.text.toString()
            val bloqueos = bloqueosEditText.text.toString()
            val ruidoInterno = ruidoInternoEditText.text.toString()

            // Recuperar el correo del usuario
            val sharedPreferences = getSharedPreferences("userSession", Context.MODE_PRIVATE)
            val userEmail = sharedPreferences.getString("userEmail", null)

            // Verificar que los campos no estén vacíos
            if (historiaDelDia.isNotEmpty() && agradecimientos.isNotEmpty() &&
                victorias.isNotEmpty() && aprendizajes.isNotEmpty() && bloqueos.isNotEmpty() &&
                ruidoInterno.isNotEmpty() && userEmail != null) {

                // Crear el objeto Bitacora con todos los campos, correo y agregar el timestamp
                val bitacora = hashMapOf(
                    "historiaDelDia" to historiaDelDia,
                    "agradecimientos" to agradecimientos,
                    "victorias" to victorias,
                    "aprendizajes" to aprendizajes,
                    "bloqueos" to bloqueos,
                    "ruidoInterno" to ruidoInterno,
                    "correoUsuario" to userEmail, // Añadir el correo
                    "timestamp" to FieldValue.serverTimestamp()
                )

                // Guardar en Firestore
                db.collection("bitacoras")
                    .add(bitacora)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Bitácora guardada con éxito", Toast.LENGTH_SHORT).show()

                        // Limpiar los campos después de guardar
                        limpiarCampos()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al guardar la bitácora: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                if (userEmail == null) {
                    Toast.makeText(this, "No se encontró el correo del usuario", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
        }

        retrospectivaButton.setOnClickListener {
            val intent2 = Intent(this, RetrospectivaActivity::class.java)
            startActivity(intent2)
        }

        // Botón para cerrar sesión
        cerrarSesionButton.setOnClickListener {
            val preferences = getSharedPreferences("userSession", MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putBoolean("isLoggedIn", false)

            // Usar commit() para forzar la escritura inmediata
            val isSuccess = editor.commit() // Devuelve true si se actualizó correctamente
            if (isSuccess) {
                Toast.makeText(this, "Sesión cerrada y preferencia actualizada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al actualizar preferencias", Toast.LENGTH_SHORT).show()
            }

            // Cerrar sesión en FirebaseAuth
            FirebaseAuth.getInstance().signOut()

            // Redirigir a MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun limpiarCampos() {
        historiaDelDiaEditText.text.clear()
        agradecimientosEditText.text.clear()
        victoriasEditText.text.clear()
        aprendizajesEditText.text.clear()
        bloqueosEditText.text.clear()
        ruidoInternoEditText.text.clear()
    }
}
