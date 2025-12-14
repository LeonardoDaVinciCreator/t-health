package com.tbank.t_health.di

import com.tbank.t_health.data.remote.HealthApiService
import com.tbank.t_health.data.remote.RetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHealthApiService(): HealthApiService {
        return RetrofitInstance.api
    }
}
