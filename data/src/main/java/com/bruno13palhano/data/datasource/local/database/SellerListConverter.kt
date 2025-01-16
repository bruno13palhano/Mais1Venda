package com.bruno13palhano.data.datasource.local.database

import androidx.room.TypeConverter
import com.bruno13palhano.data.model.company.Seller
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class SellerListConverter {
    @TypeConverter
    fun fromSellerList(sellerList: List<Seller>): String {
        return Json.encodeToString(sellerList)
    }

    @TypeConverter
    fun toSellerList(sellerListString: String): List<Seller> {
        return Json.decodeFromString(sellerListString)
    }
}
