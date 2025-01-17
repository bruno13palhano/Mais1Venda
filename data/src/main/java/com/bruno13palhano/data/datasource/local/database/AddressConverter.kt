package com.bruno13palhano.data.datasource.local.database

import androidx.room.TypeConverter
import com.bruno13palhano.data.model.shared.Address
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class AddressConverter {
    @TypeConverter
    fun fromAddress(address: Address): String {
        return Json.encodeToString(address)
    }

    @TypeConverter
    fun toAddress(addressString: String): Address {
        return Json.decodeFromString(addressString)
    }
}
