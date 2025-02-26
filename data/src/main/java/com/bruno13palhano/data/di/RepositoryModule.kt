package com.bruno13palhano.data.di

import com.bruno13palhano.data.repository.AdRepository
import com.bruno13palhano.data.repository.AdRepositoryImpl
import com.bruno13palhano.data.repository.CompanyRepository
import com.bruno13palhano.data.repository.CompanyRepositoryImpl
import com.bruno13palhano.data.repository.CustomerRepository
import com.bruno13palhano.data.repository.CustomerRepositoryImpl
import com.bruno13palhano.data.repository.OrderRepository
import com.bruno13palhano.data.repository.OrderRepositoryImpl
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.data.repository.ProductRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCompanyRepository(
        companyRepositoryImpl: CompanyRepositoryImpl,
    ): CompanyRepository

    @Binds
    @Singleton
    abstract fun bindOrderRepository(orderRepositoryImpl: OrderRepositoryImpl): OrderRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl,
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindCustomerRepository(
        customerRepositoryImpl: CustomerRepositoryImpl,
    ): CustomerRepository

    @Binds
    @Singleton
    abstract fun bindAdRepository(adRepositoryImpl: AdRepositoryImpl): AdRepository
}
