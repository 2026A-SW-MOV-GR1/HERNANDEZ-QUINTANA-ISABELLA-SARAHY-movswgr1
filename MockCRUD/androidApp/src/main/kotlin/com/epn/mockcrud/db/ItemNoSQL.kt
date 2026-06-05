package com.epn.mockcrud.db

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class ItemNoSQL(
    @Id var id: Long = 0,
    var name: String = "",
    var author: String = "",
    var description: String = "",
    var imageUrl: String? = null,
    var startDate: Long? = null,
    var endDate: Long? = null,
    var readingLog: String = "", // CSV: "timestamp1,timestamp2,..."
    var status: String = "Pendiente", // Pendiente, Leyendo, Completado, Abandonado
    var epubUri: String? = null
)
