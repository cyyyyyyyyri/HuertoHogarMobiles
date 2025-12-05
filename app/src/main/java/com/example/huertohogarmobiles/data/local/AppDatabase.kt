package com.example.huertohogarmobiles.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.huertohogarmobiles.data.local.dao.CarritoDao
import com.example.huertohogarmobiles.data.local.dao.ProductoDao
import com.example.huertohogarmobiles.data.local.entity.CarritoEntity
import com.example.huertohogarmobiles.data.local.entity.ProductoEntity

// Define las entidades (tablas) y la versión de la base de datos
@Database(
    entities = [
        CarritoEntity::class,
        ProductoEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // ✅ Estas son las funciones que Hilt y la App usarán para obtener los DAOs
    abstract fun carritoDao(): CarritoDao
    abstract fun productoDao(): ProductoDao

    // El Companion Object se mantiene por si necesitas acceso manual (testing, etc),
    // pero Hilt usará el Módulo definido abajo.
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "huertohogar_database"
                )
                    .fallbackToDestructiveMigration() // Borra datos si cambias la versión sin migración
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
