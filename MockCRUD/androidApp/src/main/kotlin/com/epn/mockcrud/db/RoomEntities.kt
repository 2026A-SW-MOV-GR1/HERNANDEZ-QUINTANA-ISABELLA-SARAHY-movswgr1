package com.epn.mockcrud.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val author: String = "",
    val description: String,
    val imageUrl: String? = null,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val status: String = "Pendiente", // Pendiente, Leyendo, Completado, Abandonado
    val epubUri: String? = null,
    val readingLog: String = "" 
)

@Entity(tableName = "global_reading_log")
data class GlobalReadingLog(
    @PrimaryKey val date: String, // Formato "yyyy-MM-dd"
    val completed: Boolean = true
)

@Dao
interface ItemDao {
    @Query("SELECT * FROM items")
    fun getAll(): Flow<List<ItemEntity>>

    @Insert
    suspend fun insert(item: ItemEntity)

    @Update
    suspend fun update(item: ItemEntity)

    @Delete
    suspend fun delete(item: ItemEntity)

    // Global Log
    @Query("SELECT * FROM global_reading_log")
    fun getGlobalLogs(): Flow<List<GlobalReadingLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGlobalLog(log: GlobalReadingLog)
    
    @Delete
    suspend fun deleteGlobalLog(log: GlobalReadingLog)
}

@Database(entities = [ItemEntity::class, GlobalReadingLog::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}
