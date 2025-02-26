package com.bruno13palhano.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.data.datasource.local.entity.AdEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface AdDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ad: AdEntity): Long

    @Update
    suspend fun update(ad: AdEntity): Int

    @Query("DELETE FROM ad WHERE id = :id")
    suspend fun delete(id: Long): Int

    @Query("SELECT * FROM ad WHERE id = :id")
    suspend fun getById(id: Long): AdEntity?

    @Query("SELECT * FROM ad")
    fun getAll(): Flow<List<AdEntity>>
}
