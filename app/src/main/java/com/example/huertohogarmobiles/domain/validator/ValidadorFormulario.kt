package com.example.huertohogarmobiles.domain.validator

import com.example.huertohogarmobiles.domain.model.ErroresFormulario
import com.example.huertohogarmobiles.domain.model.FormularioRegistro

// esto contiene la logica de validacion del formulario de registro
object ValidadorFormulario {


    fun validarNombreCompleto(nombre: String): String? {
        return if (nombre.isBlank() || !nombre.contains(" ")) {
            "Ingresa tu nombre y apellido completo."
        } else if (nombre.length < 5) {
            "El nombre debe tener al menos 5 caracteres."
        } else null
    }

    fun validarEmail(email: String): String? {
        return if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "El formato del correo electrónico no es válido."
        } else null
    }

    fun validarTelefono(telefono: String): String? {

        return if (telefono.length != 8 || !telefono.all { it.isDigit() }) {
            "El teléfono debe ser numérico y tener 8 dígitos."
        } else null
    }

    fun validarDireccion(direccion: String): String? {
        return if (direccion.isBlank() || direccion.length < 10) {
            "La dirección es demasiado corta o está vacía."
        } else null
    }

    fun validarPassword(password: String): String? {
        return if (password.length < 6) {
            "La contraseña debe tener al menos 6 caracteres."
        } else null
    }

    fun validarConfirmarPassword(password: String, confirmacion: String): String? {
        return if (password != confirmacion) {
            "Las contraseñas no coinciden."
        } else null
    }

    fun validarAceptaTerminos(acepta: Boolean): String? {
        return if (!acepta) {
            "Debes aceptar los términos y condiciones."
        } else null
    }


    fun validarFormulario(form: FormularioRegistro): ErroresFormulario {
        val nombreError = validarNombreCompleto(form.nombreCompleto)
        val emailError = validarEmail(form.email)
        val telefonoError = validarTelefono(form.telefono)
        val direccionError = validarDireccion(form.direccion)
        val passwordError = validarPassword(form.password)
        val confirmarPasswordError = validarConfirmarPassword(form.password, form.confirmarPassword)
        val terminosError = validarAceptaTerminos(form.aceptaTerminos)

        return ErroresFormulario(
            nombreCompletoError = nombreError,
            emailError = emailError,
            telefonoError = telefonoError,
            direccionError = direccionError,
            passwordError = passwordError,
            confirmarPasswordError = confirmarPasswordError,
            terminosError = terminosError
        )
    }
}