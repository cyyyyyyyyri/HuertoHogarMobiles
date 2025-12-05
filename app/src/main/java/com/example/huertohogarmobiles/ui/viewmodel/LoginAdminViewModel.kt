package com.example.huertohogarmobiles.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.huertohogarmobiles.data.local.PreferenciasManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginAdminViewModel @Inject constructor(
    private val preferenciasManager: PreferenciasManager
) : ViewModel() {

    fun validarCredenciales(user: String, pass: String): Boolean {
        return preferenciasManager.validarCredencialesAdmin(user, pass)
    }

    fun guardarSesion(username: String) {
        preferenciasManager.guardarSesionAdmin(username)
    }
}