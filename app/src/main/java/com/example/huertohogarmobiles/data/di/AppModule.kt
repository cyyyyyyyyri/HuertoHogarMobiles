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
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://huerto-hogar.onrender.com/"

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideProductoDao(database: AppDatabase): ProductoDao = database.productoDao()

    @Provides
    @Singleton
    fun provideCarritoDao(database: AppDatabase): CarritoDao = database.carritoDao()

    @Provides
    @Singleton
    fun providePreferenciasManager(@ApplicationContext context: Context): PreferenciasManager = PreferenciasManager(context)

    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideProductApiService(retrofit: Retrofit): ProductApiService = retrofit.create(ProductApiService::class.java)

    @Provides
    @Singleton
    fun provideProductoRepository(productoDao: ProductoDao, apiService: ProductApiService, ioDispatcher: CoroutineDispatcher): ProductoRepository {
        return ProductoRepositoryImpl(productoDao, apiService, ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideCarritoRepository(carritoDao: CarritoDao): CarritoRepository {
        return CarritoRepositoryImpl(carritoDao)
    }
}