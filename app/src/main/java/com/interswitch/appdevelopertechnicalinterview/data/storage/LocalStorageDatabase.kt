package com.interswitch.appdevelopertechnicalinterview.data.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.interswitch.appdevelopertechnicalinterview.data.model.ItemFee
import com.interswitch.appdevelopertechnicalinterview.utils.DataConverters
import com.interswitch.appdevelopertechnicalinterview.utils.ItemFeeDao

@Database(entities = [ItemFee::class], version = 1, exportSchema = false)
@TypeConverters(value = [DataConverters::class])
abstract class LocalStorageDatabase : RoomDatabase() {
    abstract fun itemFeeDao(): ItemFeeDao

    companion object {
        @Volatile
        private var INSTANCE: LocalStorageDatabase? = null

        fun getDatabase(context: Context): LocalStorageDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalStorageDatabase::class.java,
                    "local_storage_db"
                )
                    .build()
                INSTANCE = instance
                /*return */
                instance
            }
        }
    }
}