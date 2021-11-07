package com.example.option.data

import androidx.room.Database
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.option.data.entities.*

private const val DB_NAME = "stock.db"
private const val DB_PATH = "databases/stock.db"

@Database( entities = [Stock::class], version = 1, exportSchema = false )

abstract class AppDatabase : RoomDatabase() {
    abstract fun stock(): StockDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                ).allowMainThreadQueries().build()

                INSTANCE = instance
                return instance
            }
        }
    }
}

