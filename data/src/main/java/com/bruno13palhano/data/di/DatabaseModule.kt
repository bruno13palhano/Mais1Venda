package com.bruno13palhano.data.di

import android.content.Context
import androidx.room.Room
import com.bruno13palhano.data.datasource.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    fun provideCompanyDao(appDatabase: AppDatabase) = appDatabase.companyDao()

    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_db",
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
