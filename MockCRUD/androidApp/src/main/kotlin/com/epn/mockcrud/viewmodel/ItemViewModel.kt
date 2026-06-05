package com.epn.mockcrud.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.epn.mockcrud.db.*
import io.objectbox.Box
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ItemViewModel(app: Application) : AndroidViewModel(app) {

    var useSQL by mutableStateOf(true)

    // Room (SQL)
    private val db = DatabaseProvider.getRoomDb(app)
    val sqlItems: StateFlow<List<ItemEntity>> = db.itemDao().getAll()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // ObjectBox (NoSQL)
    private val boxStore = try { DatabaseProvider.getBoxStore(app) } catch (e: Exception) { null }
    private val itemBox: Box<ItemNoSQL>? = boxStore?.boxFor(ItemNoSQL::class.java)
    private val logBox: Box<GlobalLogNoSQL>? = boxStore?.boxFor(GlobalLogNoSQL::class.java)
    
    var noSqlItems by mutableStateOf<List<ItemNoSQL>>(emptyList())

    init {
        loadNoSqlItems()
    }

    private fun loadNoSqlItems() {
        viewModelScope.launch {
            try {
                itemBox?.let {
                    noSqlItems = it.all
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Log de lectura global reactivo al switch useSQL
    @OptIn(ExperimentalCoroutinesApi::class)
    val globalReadingLogs: StateFlow<Set<String>> = snapshotFlow { useSQL }
        .flatMapLatest { isSql ->
            if (isSql) {
                db.itemDao().getGlobalLogs().map { logs -> logs.map { it.date }.toSet() }
            } else {
                // Flow para ObjectBox con polling simple para este demo
                flow {
                    while(true) {
                        emit(logBox?.all?.map { it.date }?.toSet() ?: emptySet())
                        delay(1000) 
                    }
                }
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

    fun toggleReadingDate(date: String) {
        viewModelScope.launch {
            if (useSQL) {
                val exists = db.itemDao().getGlobalLogs().first().any { it.date == date }
                if (exists) db.itemDao().deleteGlobalLog(GlobalReadingLog(date))
                else db.itemDao().insertGlobalLog(GlobalReadingLog(date))
            } else {
                // Usamos find para evitar dependencia de GlobalLogNoSQL_ generado si hay problemas de build
                val existing = logBox?.all?.find { it.date == date }
                if (existing != null) {
                    logBox?.remove(existing.id)
                } else {
                    logBox?.put(GlobalLogNoSQL(date = date))
                }
            }
        }
    }

    fun addItem(
        name: String, 
        author: String, 
        description: String, 
        imageUrl: String? = null, 
        startDate: Long? = null, 
        endDate: Long? = null,
        status: String = "Pendiente",
        epubUri: String? = null
    ) {
        viewModelScope.launch {
            if (useSQL) {
                db.itemDao().insert(ItemEntity(
                    name = name, 
                    author = author, 
                    description = description, 
                    imageUrl = imageUrl, 
                    startDate = startDate, 
                    endDate = endDate, 
                    status = status,
                    epubUri = epubUri
                ))
            } else {
                itemBox?.put(ItemNoSQL(
                    name = name, 
                    author = author, 
                    description = description, 
                    imageUrl = imageUrl, 
                    startDate = startDate, 
                    endDate = endDate, 
                    status = status,
                    epubUri = epubUri
                ))
                noSqlItems = itemBox?.all ?: emptyList()
            }
        }
    }

    fun updateItem(
        id: Long,
        name: String,
        author: String,
        description: String,
        imageUrl: String? = null,
        startDate: Long? = null,
        endDate: Long? = null,
        status: String = "Pendiente",
        epubUri: String? = null
    ) {
        viewModelScope.launch {
            if (useSQL) {
                db.itemDao().update(ItemEntity(
                    id = id.toInt(),
                    name = name,
                    author = author,
                    description = description,
                    imageUrl = imageUrl,
                    startDate = startDate,
                    endDate = endDate,
                    status = status,
                    epubUri = epubUri
                ))
            } else {
                val item = itemBox?.get(id)
                item?.let {
                    it.name = name
                    it.author = author
                    it.description = description
                    it.imageUrl = imageUrl
                    it.startDate = startDate
                    it.endDate = endDate
                    it.status = status
                    it.epubUri = epubUri
                    itemBox?.put(it)
                    noSqlItems = itemBox?.all ?: emptyList()
                }
            }
        }
    }

    fun updateItemStatus(bookId: Long, newStatus: String) {
        viewModelScope.launch {
            if (useSQL) {
                val currentList = db.itemDao().getAll().first()
                val item = currentList.find { it.id.toLong() == bookId }
                item?.let { db.itemDao().update(it.copy(status = newStatus)) }
            } else {
                val item = itemBox?.get(bookId)
                item?.let {
                    it.status = newStatus
                    itemBox?.put(it)
                    noSqlItems = itemBox?.all ?: emptyList()
                }
            }
        }
    }

    fun deleteItem(sqlItem: ItemEntity? = null, noSqlItem: ItemNoSQL? = null) {
        viewModelScope.launch {
            if (useSQL && sqlItem != null) {
                db.itemDao().delete(sqlItem)
            } else if (!useSQL && noSqlItem != null) {
                itemBox?.remove(noSqlItem.id)
                noSqlItems = itemBox?.all ?: emptyList()
            }
        }
    }
}
