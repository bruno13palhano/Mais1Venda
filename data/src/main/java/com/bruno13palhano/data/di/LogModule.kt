package com.bruno13palhano.data.di

import com.bruno13palhano.data.log.AppLog
import com.bruno13palhano.data.log.AppLogImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object LogModule {
    @Provides
    @Singleton
    fun provideAppLog(): AppLog = AppLogImpl()
}
