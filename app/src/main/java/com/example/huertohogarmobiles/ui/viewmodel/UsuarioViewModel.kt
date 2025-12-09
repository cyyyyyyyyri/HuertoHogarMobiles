package com.example.huertohogarmobiles.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogarmobiles.data.local.PreferenciasManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val preferencias: PreferenciasManager
) : ViewModel() {

    private val _fotoUrl = MutableStateFlow<String?>(preferencias.obtenerFotoAdmin())
    val fotoUrl: StateFlow<String?> = _fotoUrl

    fun actualizarFoto(url: String) {
        viewModelScope.launch {
            preferencias.guardarFotoAdmin(url)
            _fotoUrl.value = url
        }
    }
}