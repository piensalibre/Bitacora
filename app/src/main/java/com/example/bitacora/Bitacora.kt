package com.example.bitacora



data class Bitacora(
    val historiaDelDia: String = "",
    val agradecimientos: String = "",
    val victorias: String = "",
    val aprendizajes: String = "",
    val bloqueos: String = "",
    val ruidoInterno: String = "",
    val correoUsuario: String = "", // Campo adicional para el correo del usuario
    val timestamp: Any? = null // Aquí podemos usar Any? para poder almacenar el timestamp
)
