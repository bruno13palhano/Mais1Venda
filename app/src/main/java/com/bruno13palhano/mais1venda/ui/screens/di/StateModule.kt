package com.bruno13palhano.mais1venda.ui.screens.di

import com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter.CreateAccountState
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.presenter.LoginState
import com.bruno13palhano.mais1venda.ui.screens.home.presenter.HomeState
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
}
