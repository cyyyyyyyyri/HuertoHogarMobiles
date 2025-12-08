package com.example.huertohogarmobiles.data.di

import android.content.Context
import com.example.huertohogarmobiles.data.local.AppDatabase
import com.example.huertohogarmobiles.data.local.PreferenciasManager
import com.example.huertohogarmobiles.data.local.dao.CarritoDao
import com.example.huertohogarmobiles.data.local.dao.ProductoDao
import com.example.huertohogarmobiles.data.remote.api.ProductApiService
import com.example.huertohogarmobiles.data.repository.CarritoRepositoryImpl
import com.example.huertohogarmobiles.data.repository.ProductoRepositoryImpl
import com.example.huertohogarmobiles.domain.repository.CarritoRepository
import com.example.huertohogarmobiles.domain.repository.ProductoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideProductoDao(database: AppDatabase): ProductoDao {
        return database.productoDao()
    }

    @Provides
    @Singleton
    fun provideCarritoDao(database: AppDatabase): CarritoDao {
        return database.carritoDao()
    }

    @Provides
    @Singleton
    fun providePreferenciasManager(@ApplicationContext context: Context): PreferenciasManager {
        return PreferenciasManager(context)
    }

    @Provides
    @Singleton
    fun provideProductoRepository(
        productoDao: ProductoDao,
        apiService: ProductApiService
    ): ProductoRepository {
        return ProductoRepositoryImpl(productoDao, apiService)
    }

    @Provides
    @Singleton
    fun provideCarritoRepository(carritoDao: CarritoDao): CarritoRepository {
        // Aquí es donde ocurría el error: ahora ya existe CarritoRepositoryImpl
        return CarritoRepositoryImpl(carritoDao)
    }
}