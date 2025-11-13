package com.example.huertohogarmobiles.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.huertohogarmobiles.data.local.dao.CarritoDao
import com.example.huertohogarmobiles.data.local.dao.ProductoDao
import com.example.huertohogarmobiles.data.local.entity.CarritoEntity
import com.example.huertohogarmobiles.data.local.entity.ProductoEntity

/**
 * Database principal de la app (HuertoHogar)
 * Singleton para una única instancia en toda la app.
 */
@Database(
    // Listado de todas las Entidades (tablas)
    entities = [CarritoEntity::class, ProductoEntity::class],
    version = 2, // Versión 2 porque incluimos CarritoEntity y ProductoEntity
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Abstract functions para acceder a los DAOs
    abstract fun carritoDao(): CarritoDao
    abstract fun productoDao(): ProductoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene instancia única de la base de datos (Singleton).
         */
        fun getDatabase(context: Context): AppDatabase {
            // Si la instancia ya existe, la retorna
            return INSTANCE ?: synchronized(this) {
                // Si no existe, crea una nueva
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "huertohogar_database" // Nombre del archivo de base de datos
                )
                    .fallbackToDestructiveMigration() // Destruye y recrea la BD si la versión cambia
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
