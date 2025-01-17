package com.bruno13palhano.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.data.datasource.local.data.ProductLocalData
import com.bruno13palhano.data.datasource.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ProductDao : ProductLocalData<ProductEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(product: ProductEntity)

    @Update
    override suspend fun update(product: ProductEntity)

    @Query("DELETE FROM product WHERE id = :id")
    override suspend fun delete(id: Long)

    @Query("SELECT * FROM product WHERE id = :id")
    override suspend fun getById(id: Long): ProductEntity?

    @Query("SELECT * FROM product")
    override fun getAll(): Flow<List<ProductEntity>>
}
