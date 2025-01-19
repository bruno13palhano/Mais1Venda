package com.bruno13palhano.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.data.datasource.local.entity.CompanyEntity

@Dao
internal interface CompanyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(company: CompanyEntity)

    @Update
    suspend fun update(company: CompanyEntity)

    @Query("DELETE FROM company WHERE uid = :uid")
    suspend fun delete(uid: String)

    @Query("SELECT * FROM company WHERE uid = :uid")
    suspend fun getCompany(uid: String): CompanyEntity?
}
