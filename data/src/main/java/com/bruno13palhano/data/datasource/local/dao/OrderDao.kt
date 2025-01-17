package com.bruno13palhano.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.data.datasource.local.data.OrderLocalData
import com.bruno13palhano.data.datasource.local.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface OrderDao : OrderLocalData<OrderEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(order: OrderEntity)

    @Update
    override suspend fun update(order: OrderEntity)

    @Query("DELETE FROM `order` WHERE id = :id")
    override suspend fun delete(id: Long)

    @Query("SELECT * FROM `order` WHERE id = :id")
    override suspend fun getById(id: Long): OrderEntity?

    @Query("SELECT * FROM `order`")
    override fun getAll(): Flow<List<OrderEntity>>
}
