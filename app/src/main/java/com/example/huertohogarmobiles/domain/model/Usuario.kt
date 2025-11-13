package com.example.huertohogarmobiles.domain.model

/**
 * Modelo de Usuario (para autenticación)
 */
data class Usuario(
    val username: String,
    val password: String,
    val rol: Rol = Rol.USUARIO
)

enum class Rol {
    USUARIO,
    ADMIN
}