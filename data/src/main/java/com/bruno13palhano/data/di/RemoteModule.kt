package com.bruno13palhano.data.di

import com.bruno13palhano.data.datasource.remote.service.ApiService
import com.bruno13palhano.data.datasource.remote.source.CompanyRemoteData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RemoteModule {
    @Provides
    @Singleton
    fun provideCompanyRemoteDataSource(api: ApiService): CompanyRemoteData =
        CompanyRemoteData(api = api)
}
