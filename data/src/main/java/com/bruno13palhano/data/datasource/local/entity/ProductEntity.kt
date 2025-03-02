package com.bruno13palhano.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
internal data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val image: ByteArray,
    val category: List<String>,
    val description: String,
    val code: String,
    val quantity: Int,
    val exhibitToCatalog: Boolean,
    val lastModifiedTimestamp: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductEntity

        if (id != other.id) return false
        if (name != other.name) return false
        if (!image.contentEquals(other.image)) return false
        if (category != other.category) return false
        if (description != other.description) return false
        if (code != other.code) return false
        if (quantity != other.quantity) return false
        if (exhibitToCatalog != other.exhibitToCatalog) return false
        if (lastModifiedTimestamp != other.lastModifiedTimestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + category.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + code.hashCode()
        result = 31 * result + quantity
        result = 31 * result + exhibitToCatalog.hashCode()
        result = 31 * result + lastModifiedTimestamp.hashCode()
        return result
    }
}
