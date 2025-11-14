package com.example.huertohogarmobiles.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.huertohogarmobiles.domain.model.FormularioRegistro
import com.example.huertohogarmobiles.domain.validator.ValidadorFormulario
import com.example.huertohogarmobiles.ui.state.RegistroUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * RegistroViewModel: Gestiona el estado del formulario de registro y su validación.
 */
class RegistroViewModel : ViewModel() { // No necesita repositorio, es más simple

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    // --- Handlers de cambio y validación de campos ---

    private fun actualizarFormularioYValidar(update: FormularioRegistro.() -> FormularioRegistro) {
        val nuevoFormulario = _uiState.value.formulario.update()
        val nuevosErrores = ValidadorFormulario.validarFormulario(nuevoFormulario)

        _uiState.value = _uiState.value.copy(
            formulario = nuevoFormulario,
            errores = nuevosErrores
        )
    }

    fun onNombreChange(nombre: String) = actualizarFormularioYValidar { copy(nombreCompleto = nombre) }
    fun onEmailChange(email: String) = actualizarFormularioYValidar { copy(email = email) }
    fun onTelefonoChange(telefono: String) = actualizarFormularioYValidar { copy(telefono = telefono) }
    fun onDireccionChange(direccion: String) = actualizarFormularioYValidar { copy(direccion = direccion) }
    fun onPasswordChange(password: String) = actualizarFormularioYValidar { copy(password = password) }
    fun onConfirmarPasswordChange(confirmacion: String) = actualizarFormularioYValidar { copy(confirmarPassword = confirmacion) }

    fun onAceptaTerminosChange(acepta: Boolean) {
        // La validación de términos es simple, la manejamos aparte o la incluimos en la validación completa
        val nuevoFormulario = _uiState.value.formulario.copy(aceptaTerminos = acepta)
        val nuevosErrores = ValidadorFormulario.validarFormulario(nuevoFormulario)

        _uiState.value = _uiState.value.copy(
            formulario = nuevoFormulario,
            errores = nuevosErrores
        )
    }

    // --- Acción de Registro ---

    fun intentarRegistro() {
        val form = _uiState.value.formulario
        val errores = ValidadorFormulario.validarFormulario(form)

        if (errores.hayErrores) {
            // Si hay errores de validación, actualiza el UI State para mostrarlos
            _uiState.value = _uiState.value.copy(errores = errores)
            return
        }

        // Simulación de registro exitoso (Aquí iría la llamada al Repository/Backend)
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(estaRegistrando = true, errorGeneral = null)

            // Simular trabajo en el backend (ej: delay(2000))
            kotlinx.coroutines.delay(2000)

            // Si es exitoso
            _uiState.value = _uiState.value.copy(
                estaRegistrando = false,
                registroExitoso = true
                // No es necesario limpiar el formulario, la pantalla navega
            )
        }
    }
}

/**
 * Factory simple ya que RegistroViewModel no toma parámetros en el constructor.
 */
class RegistroViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistroViewModel::class.java)) {
            return RegistroViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}