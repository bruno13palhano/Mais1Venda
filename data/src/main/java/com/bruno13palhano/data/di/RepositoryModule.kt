package com.bruno13palhano.data.di

import com.bruno13palhano.data.repository.CompanyRepository
import com.bruno13palhano.data.repository.CompanyRepositoryImpl
import com.bruno13palhano.data.repository.OrderRepository
import com.bruno13palhano.data.repository.OrderRepositoryImpl
import com.bruno13palhano.data.repository.ProductRepository
import com.bruno13palhano.data.repository.ProductRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

annotation class CompanyRep

annotation class OrderRep

annotation class ProductRep

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    @CompanyRep
    @Singleton
    abstract fun bindCompanyRepository(
        companyRepositoryImpl: CompanyRepositoryImpl,
    ): CompanyRepository

    @Binds
    @OrderRep
    @Singleton
    abstract fun bindOrderRepository(orderRepositoryImpl: OrderRepositoryImpl): OrderRepository

    @Binds
    @ProductRep
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl,
    ): ProductRepository
}
