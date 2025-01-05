package com.bruno13palhano.data.di

import com.bruno13palhano.data.repository.CompanyRepository
import com.bruno13palhano.data.repository.CompanyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

annotation class CompanyRep

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    @CompanyRep
    @Singleton
    abstract fun bindCompanyRepository(companyRepositoryImpl: CompanyRepositoryImpl): CompanyRepository
}
