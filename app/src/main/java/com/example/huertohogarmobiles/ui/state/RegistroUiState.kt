package com.example.huertohogarmobiles.ui.state

import com.example.huertohogarmobiles.domain.model.ErroresFormulario
import com.example.huertohogarmobiles.domain.model.FormularioRegistro

/**
 * Estado completo de la UI del formulario de registro.
 */
data class RegistroUiState(
    val formulario: FormularioRegistro = FormularioRegistro(),
    val errores: ErroresFormulario = ErroresFormulario(),
    val estaRegistrando: Boolean = false, // Para mostrar loading al enviar
    val registroExitoso: Boolean = false,
    val errorGeneral: String? = null
)