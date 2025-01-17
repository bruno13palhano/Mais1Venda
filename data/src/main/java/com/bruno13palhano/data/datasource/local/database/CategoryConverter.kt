package com.bruno13palhano.data.datasource.local.database

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class CategoryConverter {
    @TypeConverter
    fun fromListCategory(categories: List<String>): String {
        return Json.encodeToString(categories)
    }

    @TypeConverter
    fun toListCategory(categoriesString: String): List<String> {
        return Json.decodeFromString(categoriesString)
    }
}
