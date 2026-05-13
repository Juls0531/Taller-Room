package com.example.salesapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.salesapp.data.dao.ArticuloDao
import com.example.salesapp.data.dao.VentaDao
import com.example.salesapp.data.entities.Articulo
import com.example.salesapp.data.entities.Venta


@Database(
    entities = [Articulo::class, Venta::class],
    version = 1,
    exportSchema = false
)
abstract class SalesDatabase : RoomDatabase() {

    abstract fun articuloDao(): ArticuloDao
    abstract fun ventaDao(): VentaDao

    companion object {
        @Volatile
        private var INSTANCE: SalesDatabase? = null


        fun getDatabase(context: Context): SalesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SalesDatabase::class.java,
                    "sales_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
