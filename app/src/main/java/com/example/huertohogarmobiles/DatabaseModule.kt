package com.example.huertohogarmobiles

import android.content.Context
import androidx.room.Room
import com.example.huertohogarmobiles.data.local.AppDatabase
import com.example.huertohogarmobiles.data.local.dao.CarritoDao
import com.example.huertohogarmobiles.data.local.dao.ProductoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule { // ✅ Mantenlo como 'object' para mejor rendimiento (métodos estáticos)

    // 1. Enseña a Hilt cómo crear la Base de Datos
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "huertohogar_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    // 2. Enseña a Hilt cómo obtener el ProductoDao (para el Repositorio)
    @Provides
    fun provideProductoDao(database: AppDatabase): ProductoDao {
        return database.productoDao()
    }

    // 3. Enseña a Hilt cómo obtener el CarritoDao
    @Provides
    fun provideCarritoDao(database: AppDatabase): CarritoDao {
        return database.carritoDao()
    }
}