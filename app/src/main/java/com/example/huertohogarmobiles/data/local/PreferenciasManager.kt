package com.example.huertohogarmobiles.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferenciasManager(context: Context) {

    private val PREFS_NAME = "HuertoHogar_Prefs"
    private val KEY_IS_ADMIN_LOGGED_IN = "is_admin_logged_in"
    private val KEY_ADMIN_USERNAME = "admin_username"

    // Clave única y oficial para la foto
    private val KEY_ADMIN_FOTO_URL = "admin_foto_url"

    private val ADMIN_USER = "admin"
    private val ADMIN_PASS = "1234"

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // -----------------------------------------------------
    // LOGIN ADMIN
    // -----------------------------------------------------
    fun validarCredencialesAdmin(user: String, pass: String): Boolean {
        return user == ADMIN_USER && pass == ADMIN_PASS
    }

    fun guardarSesionAdmin(username: String) {
        prefs.edit().apply {
            putBoolean(KEY_IS_ADMIN_LOGGED_IN, true)
            putString(KEY_ADMIN_USERNAME, username)
            apply()
        }
    }

    fun cerrarSesionAdmin() {
        prefs.edit().apply {
            putBoolean(KEY_IS_ADMIN_LOGGED_IN, false)
            remove(KEY_ADMIN_USERNAME)
            remove(KEY_ADMIN_FOTO_URL) // limpia la foto también
            apply()
        }
    }

    fun estaAdminLogueado(): Boolean {
        return prefs.getBoolean(KEY_IS_ADMIN_LOGGED_IN, false)
    }

    fun obtenerUsernameAdmin(): String? {
        return prefs.getString(KEY_ADMIN_USERNAME, null)
    }

    // -----------------------------------------------------
    // FOTO DE PERFIL
    // -----------------------------------------------------
    fun guardarFotoAdmin(url: String) {
        prefs.edit().putString(KEY_ADMIN_FOTO_URL, url).apply()
    }

    fun obtenerFotoAdmin(): String? {
        return prefs.getString(KEY_ADMIN_FOTO_URL, null)
    }
}