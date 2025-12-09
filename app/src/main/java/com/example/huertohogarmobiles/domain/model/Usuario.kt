package com.example.huertohogarmobiles.domain.model


data class Usuario(
    val username: String,
    val password: String,
    val rol: Rol = Rol.USUARIO,
    val fotoUrl: String? = null
)

enum class Rol {
    USUARIO,
    ADMIN
}