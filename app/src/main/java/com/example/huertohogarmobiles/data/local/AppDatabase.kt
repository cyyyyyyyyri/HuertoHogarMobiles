package com.example.huertohogarmobiles.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.huertohogarmobiles.data.local.dao.CarritoDao
import com.example.huertohogarmobiles.data.local.dao.ProductoDao
import com.example.huertohogarmobiles.data.local.entity.CarritoEntity
import com.example.huertohogarmobiles.data.local.entity.ProductoEntity

//Database principal

@Database(
    entities = [CarritoEntity::class, ProductoEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Funciones para acceder a los DAOs
    abstract fun carritoDao(): CarritoDao
    abstract fun productoDao(): ProductoDao

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
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
