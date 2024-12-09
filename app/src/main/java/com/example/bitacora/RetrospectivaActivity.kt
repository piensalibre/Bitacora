package com.example.bitacora

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class RetrospectivaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrospectiva)

        val historiaDelDiaButton: Button = findViewById(R.id.historiaDelDiaButton)
        val agradecimientosButton: Button = findViewById(R.id.agradecimientosButton)
        val victoriasButton: Button = findViewById(R.id.victoriasButton)
        val aprendizajesButton: Button = findViewById(R.id.aprendizajesButton)
        val bloqueosButton: Button = findViewById(R.id.bloqueosButton)
        val ruidoInternoButton: Button = findViewById(R.id.ruidoInternoButton)

        historiaDelDiaButton.setOnClickListener {
            val intent = Intent(this, HistoriaDelDiaActivity::class.java)
            startActivity(intent)
        }

        agradecimientosButton.setOnClickListener {
            val intent = Intent(this, Agradecimientos::class.java)
            startActivity(intent)
        }

        victoriasButton.setOnClickListener {
            val intent = Intent(this, VictoriasActivity::class.java)
            startActivity(intent)
        }

        aprendizajesButton.setOnClickListener {
            val intent = Intent(this, AprendizajesActivity::class.java)
            startActivity(intent)
        }

        bloqueosButton.setOnClickListener {
            val intent = Intent(this, BloqueosActivity::class.java)
            startActivity(intent)
        }

        ruidoInternoButton.setOnClickListener {
            val intent = Intent(this, RuidoInternoActivity::class.java)
            startActivity(intent)
        }
    }
}
