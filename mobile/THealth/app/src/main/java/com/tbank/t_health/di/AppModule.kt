package com.tbank.t_health.di

import android.content.Context
import com.tbank.t_health.data.local.ActiveStorage
import com.tbank.t_health.data.local.StepCounterService
import com.tbank.t_health.data.repository.ActivityRepository
import com.tbank.t_health.domain.usecase.*
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideStepCounterService(@ApplicationContext context: Context) = StepCounterService(context)

    @Provides
    fun provideActiveStorage(@ApplicationContext context: Context) = ActiveStorage(context)

    @Provides
    fun provideActivityRepository(@ApplicationContext context: Context) = ActivityRepository(context)

    @Provides
    fun provideGetTodayStatsUseCase(service: StepCounterService, storage: ActiveStorage) =
        GetTodayStatsUseCase(service, storage)

    @Provides
    fun provideGetYesterdayStatsUseCase(service: StepCounterService) =
        GetYesterdayStatsUseCase(service)

    @Provides
    fun provideSyncActivitiesUseCase(
        repo: ActivityRepository,
        stepService: StepCounterService,
        activeStorage: ActiveStorage
    ) = SyncActivitiesUseCase(repo, stepService, activeStorage)

    @Provides
    fun provideObserveStepsUseCase(service: StepCounterService) = ObserveStepsUseCase(service)
}
