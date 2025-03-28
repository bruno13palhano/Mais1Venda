package com.bruno13palhano.mais1venda.ui.screens.di

import com.bruno13palhano.mais1venda.ui.screens.ads.presenter.AdState
import com.bruno13palhano.mais1venda.ui.screens.ads.presenter.AdsState
import com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter.CreateAccountState
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.presenter.LoginState
import com.bruno13palhano.mais1venda.ui.screens.home.presenter.HomeState
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.CustomersState
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.NewOrderState
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.NewOrdersState
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.OrdersDashboardState
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.OrdersState
import com.bruno13palhano.mais1venda.ui.screens.orders.presenter.OrdersStatusState
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.ProductState
import com.bruno13palhano.mais1venda.ui.screens.products.presenter.ProductsState
import com.bruno13palhano.mais1venda.ui.screens.settings.presenter.SettingsState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object StateModule {
    @Provides
    @Singleton
    fun provideHomeState() = HomeState()

    @Provides
    @Singleton
    fun provideLoginState() = LoginState()

    @Provides
    @Singleton
    fun provideCreateAccountState() = CreateAccountState()

    @Provides
    @Singleton
    fun provideSettingsState() = SettingsState()

    @Provides
    @Singleton
    fun provideProductsState() = ProductsState()

    @Provides
    @Singleton
    fun provideNewProductState() = ProductState()

    @Provides
    @Singleton
    fun provideOrdersStatusState() = OrdersStatusState()

    @Provides
    @Singleton
    fun provideNewOrdersState() = NewOrdersState()

    @Provides
    @Singleton
    fun provideOrdersState() = OrdersState()

    @Provides
    @Singleton
    fun provideNewOrderState() = NewOrderState()

    @Provides
    @Singleton
    fun provideCustomersState() = CustomersState()

    @Provides
    @Singleton
    fun provideOrdersDashboardState() = OrdersDashboardState()

    @Provides
    @Singleton
    fun provideStoreState() = AdsState()

    @Provides
    @Singleton
    fun provideAdState() = AdState()
}
