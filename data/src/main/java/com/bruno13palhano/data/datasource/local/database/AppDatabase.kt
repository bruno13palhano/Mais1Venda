package com.bruno13palhano.data.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bruno13palhano.data.datasource.local.dao.CompanyDao
import com.bruno13palhano.data.datasource.local.entity.CompanyEntity

@Database(
    entities = [
        CompanyEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(
    value = [
        SellerListConverter::class,
        SocialMediaConverter::class,
    ]
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun companyDao(): CompanyDao
}
