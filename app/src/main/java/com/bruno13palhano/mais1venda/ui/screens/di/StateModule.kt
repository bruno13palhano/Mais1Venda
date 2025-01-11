package com.bruno13palhano.mais1venda.ui.screens.di

import com.bruno13palhano.mais1venda.ui.screens.authentication.create.presenter.CreateAccountState
import com.bruno13palhano.mais1venda.ui.screens.authentication.login.presenter.LoginState
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
    fun bindLoginState() = LoginState()

    @Provides
    @Singleton
    fun bindCreateAccountState() = CreateAccountState()
}
