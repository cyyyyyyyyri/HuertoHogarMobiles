package com.example.huertohogarmobiles.domain.model

/**
 * Datos del formulario de registro
 */
data class FormularioRegistro(
    val nombreCompleto: String = "",
    val email: String = "",
    val telefono: String = "",
    val direccion: String = "",
    val password: String = "",
    val confirmarPassword: String = "",
    val aceptaTerminos: Boolean = false,
    val fotoPerfilUri: String? = null
)

/**
 * Errores de validación del formulario
 */
data class ErroresFormulario(
    val nombreCompletoError: String? = null,
    val emailError: String? = null,
    val telefonoError: String? = null,
    val direccionError: String? = null,
    val passwordError: String? = null,
    val confirmarPasswordError: String? = null,
    val terminosError: String? = null
) {
    // True si hay al menos un error
    val hayErrores: Boolean
        get() = nombreCompletoError != null ||
                emailError != null ||
                telefonoError != null ||
                direccionError != null ||
                passwordError != null ||
                confirmarPasswordError != null ||
                terminosError != null
}