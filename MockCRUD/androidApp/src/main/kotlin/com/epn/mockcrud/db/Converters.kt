package com.epn.mockcrud.db

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromList(value: List<Long>?): String? {
        return value?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toList(value: String?): List<Long>? {
        return value?.let { Json.decodeFromString<List<Long>>(it) }
    }
}
