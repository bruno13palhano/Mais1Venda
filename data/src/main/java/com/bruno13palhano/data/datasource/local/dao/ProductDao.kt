package com.bruno13palhano.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.data.datasource.local.data.ProductLocalData
import com.bruno13palhano.data.model.company.Product
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ProductDao : ProductLocalData {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(product: Product)

    @Update
    override suspend fun update(product: Product)

    @Query("DELETE FROM product WHERE id = :id")
    override suspend fun delete(id: Long)

    @Query("SELECT * FROM product WHERE id = :id")
    override suspend fun getById(id: Long): Product?

    @Query("SELECT * FROM product")
    override fun getAll(): Flow<List<Product>>
}
