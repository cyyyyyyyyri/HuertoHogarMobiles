package com.example.huertohogarmobiles.data.local

import android.content.Context
import android.content.SharedPreferences

/**
 * Gestor para guardar las preferencias locales (SharedPreferences).
 * Se usa para la sesión del administrador.
 */
class PreferenciasManager(context: Context) {

    // Constantes para SharedPreferences
    private val PREFS_NAME = "HuertoHogar_Prefs"
    private val KEY_IS_ADMIN_LOGGED_IN = "is_admin_logged_in"
    private val KEY_ADMIN_USERNAME = "admin_username"

    // Credenciales hardcodeadas (De tu documentación de Fase 9, con contraseña de ejemplo)
    private val ADMIN_USER = "admin"
    private val ADMIN_PASS = "1234" // Usando una contraseña simple para evitar errores

    // Instancia de SharedPreferences
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // ----------------------
    // FUNCIONES REQUERIDAS POR NAVGRAPH
    // ----------------------

    /**
     * Valida las credenciales de administrador.
     */
    fun validarCredencialesAdmin(user: String, pass: String): Boolean {
        return user == ADMIN_USER && pass == ADMIN_PASS
    }

    /**
     * Guarda la sesión del administrador y el nombre de usuario.
     */
    fun guardarSesionAdmin(username: String) {
        prefs.edit().apply {
            putBoolean(KEY_IS_ADMIN_LOGGED_IN, true)
            putString(KEY_ADMIN_USERNAME, username)
            apply()
        }
    }

    /**
     * Cierra la sesión del administrador, eliminando las claves.
     */
    fun cerrarSesionAdmin() {
        prefs.edit().apply {
            putBoolean(KEY_IS_ADMIN_LOGGED_IN, false)
            remove(KEY_ADMIN_USERNAME)
            apply()
        }
    }

    /**
     * Retorna true si el administrador está logueado.
     */
    fun estaAdminLogueado(): Boolean {
        return prefs.getBoolean(KEY_IS_ADMIN_LOGGED_IN, false)
    }

    /**
     * Obtiene el nombre de usuario del administrador logueado.
     */
    fun obtenerUsernameAdmin(): String? {
        return prefs.getString(KEY_ADMIN_USERNAME, null)
    }
}