package com.bruno13palhano.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.data.datasource.local.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CustomerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(customer: CustomerEntity)

    @Update
    suspend fun update(customer: CustomerEntity)

    @Query("DELETE FROM customer WHERE uid = :uid")
    suspend fun delete(uid: String)

    @Query("SELECT * FROM customer WHERE uid = :uid")
    suspend fun getById(uid: String): CustomerEntity?

    @Query("SELECT * FROM customer")
    fun getAll(): Flow<List<CustomerEntity>>
}
