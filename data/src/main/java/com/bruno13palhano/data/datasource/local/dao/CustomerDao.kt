package com.bruno13palhano.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.data.datasource.local.data.CustomerLocalData
import com.bruno13palhano.data.datasource.local.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CustomerDao : CustomerLocalData<CustomerEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(customer: CustomerEntity)

    @Update
    override suspend fun update(customer: CustomerEntity)

    @Query("DELETE FROM customer WHERE uid = :uid")
    override suspend fun delete(uid: String)

    @Query("SELECT * FROM customer WHERE uid = :uid")
    override suspend fun getById(uid: String): CustomerEntity?

    @Query("SELECT * FROM customer")
    override fun getAll(): Flow<List<CustomerEntity>>
}
