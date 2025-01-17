package com.bruno13palhano.data.datasource.local.database

import androidx.room.TypeConverter
import com.bruno13palhano.data.model.shared.Order
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class OrderListConverter {
    @TypeConverter
    fun fromOrderList(orderList: List<Order>): String {
        return Json.encodeToString(orderList)
    }

    @TypeConverter
    fun toOrderList(orderListString: String): List<Order> {
        return Json.decodeFromString(orderListString)
    }
}
