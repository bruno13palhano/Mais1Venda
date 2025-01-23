package com.bruno13palhano.data.di

import com.bruno13palhano.data.BuildConfig
import com.bruno13palhano.data.datasource.remote.service.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
internal object ApiServiceModule {
    @Provides
    @Singleton
    fun provideService(): ApiService {
        val serverUrl = BuildConfig.serverUrl

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(serverUrl)
            .client(client)
            .build()

        val apiService: ApiService by lazy { retrofit.create(ApiService::class.java) }

        return apiService
    }
}
