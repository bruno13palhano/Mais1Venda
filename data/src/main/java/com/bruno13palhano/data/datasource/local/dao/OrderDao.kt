package com.bruno13palhano.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.data.datasource.local.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: OrderEntity): Long

    @Update
    suspend fun update(order: OrderEntity): Int

    @Query("DELETE FROM `order` WHERE id = :id")
    suspend fun delete(id: Long): Int

    @Query("SELECT * FROM `order` WHERE id = :id")
    suspend fun getById(id: Long): OrderEntity?

    @Query("SELECT * FROM `order`")
    fun getAll(): Flow<List<OrderEntity>>
}
