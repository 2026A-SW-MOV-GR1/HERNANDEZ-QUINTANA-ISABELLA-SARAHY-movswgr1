package com.epn.mockcrud.db

import android.content.Context
import androidx.room.Room
import io.objectbox.BoxStore

object DatabaseProvider {
    private var roomDb: AppDatabase? = null
    private var boxStore: BoxStore? = null

    fun getRoomDb(context: Context): AppDatabase {
        return roomDb ?: synchronized(this) {
            roomDb ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "app_db"
            ).fallbackToDestructiveMigration().build().also { roomDb = it }
        }
    }

    fun getBoxStore(context: Context): BoxStore {
        return boxStore ?: synchronized(this) {
            boxStore ?: MyObjectBox.builder()
                .androidContext(context.applicationContext)
                .build().also { boxStore = it }
        }
    }
}
