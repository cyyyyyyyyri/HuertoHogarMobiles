package com.example.huertohogarmobiles.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.huertohogarmobiles.data.local.dao.CarritoDao
import com.example.huertohogarmobiles.data.local.dao.ProductoDao
import com.example.huertohogarmobiles.data.local.entity.CarritoEntity
import com.example.huertohogarmobiles.data.local.entity.ProductoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Define las entidades (tablas) y la versión de la base de datos
@Database(
    entities = [
        ProductoEntity::class,
        CarritoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun carritoDao(): CarritoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "huerto_db"
                )
                    .addCallback(SeedCallback())
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }

    /** CALLBACK PARA INSERTAR PRODUCTOS AL CREAR LA DB **/
    private class SeedCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            // Ejecutar en background
            CoroutineScope(Dispatchers.IO).launch {
                INSTANCE?.productoDao()?.insertarProductos(
                    productosIniciales()
                )
            }
        }
    }
}

fun productosIniciales(): List<ProductoEntity> {
    return listOf(
        ProductoEntity(
            id = 1,
            nombre = "Tomate",
            precio = 1590.0,
            descripcion = "Tomate fresco y jugoso.",
            categoria = "Verduras",
            imagenUrl = "file:///android_asset/tomate.jpeg",
            stock = 25
        ),
        ProductoEntity(
            id = 2,
            nombre = "Lechuga",
            precio = 1290.0,
            descripcion = "Lechuga recién cosechada.",
            categoria = "Hojas",
            imagenUrl = "file:///android_asset/lechuga.jpeg",
            stock = 20
        ),
        ProductoEntity(
            id = 3,
            nombre = "Bananas",
            precio = 1990.0,
            descripcion = "Bananas maduras y dulces.",
            categoria = "Frutas",
            imagenUrl = "file:///android_asset/Bananas.jpeg",
            stock = 30
        ),
        ProductoEntity(
            id = 4,
            nombre = "Berenjena",
            precio = 1490.0,
            descripcion = "Berenjena fresca lista para cocinar.",
            categoria = "Verduras",
            imagenUrl = "file:///android_asset/berengena.jpeg",
            stock = 15
        ),
        ProductoEntity(
            id = 5,
            nombre = "Brócoli",
            precio = 1790.0,
            descripcion = "Brócoli verde y crocante.",
            categoria = "Verduras",
            imagenUrl = "file:///android_asset/brocoli.jpeg",
            stock = 18
        ),
        ProductoEntity(
            id = 6,
            nombre = "Arvejas",
            precio = 890.0,
            descripcion = "Arvejas verdes y tiernas.",
            categoria = "Legumbres",
            imagenUrl = "file:///android_asset/arbejas.jpeg",
            stock = 22
        ),
        ProductoEntity(
            id = 7,
            nombre = "Espárragos",
            precio = 2490.0,
            descripcion = "Espárragos frescos premium.",
            categoria = "Verduras",
            imagenUrl = "file:///android_asset/esparragos.jpeg",
            stock = 12
        )
    )
}