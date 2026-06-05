package com.epn.mockcrud.db

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class GlobalLogNoSQL(
    @Id var id: Long = 0,
    var date: String = "" // "yyyy-MM-dd"
)
