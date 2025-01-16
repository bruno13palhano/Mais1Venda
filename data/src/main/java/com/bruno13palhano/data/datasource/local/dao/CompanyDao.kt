package com.bruno13palhano.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.data.datasource.local.data.CompanyLocalData
import com.bruno13palhano.data.datasource.local.entity.CompanyEntity

@Dao
interface CompanyDao : CompanyLocalData<CompanyEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(company: CompanyEntity)

    @Update
    override suspend fun update(company: CompanyEntity)

    @Query("DELETE FROM company WHERE uid = :uid")
    override suspend fun delete(uid: String)

    @Query("SELECT * FROM company WHERE uid = :uid")
    override suspend fun getCompany(uid: String): CompanyEntity?
}
