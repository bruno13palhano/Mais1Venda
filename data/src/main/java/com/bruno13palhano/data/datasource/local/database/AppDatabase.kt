package com.bruno13palhano.data.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bruno13palhano.data.datasource.local.dao.AdDao
import com.bruno13palhano.data.datasource.local.dao.CompanyDao
import com.bruno13palhano.data.datasource.local.dao.CustomerDao
import com.bruno13palhano.data.datasource.local.dao.OrderDao
import com.bruno13palhano.data.datasource.local.dao.ProductDao
import com.bruno13palhano.data.datasource.local.entity.AdEntity
import com.bruno13palhano.data.datasource.local.entity.CompanyEntity
import com.bruno13palhano.data.datasource.local.entity.CustomerEntity
import com.bruno13palhano.data.datasource.local.entity.OrderEntity
import com.bruno13palhano.data.datasource.local.entity.ProductEntity

@Database(
    entities = [
        CompanyEntity::class,
        CustomerEntity::class,
        ProductEntity::class,
        OrderEntity::class,
        AdEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(
    value = [
        AddressConverter::class,
        SellerListConverter::class,
        SocialMediaConverter::class,
        CategoryConverter::class,
        OrderListConverter::class,
    ],
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun companyDao(): CompanyDao
    abstract fun customerDao(): CustomerDao
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun adDao(): AdDao
}
