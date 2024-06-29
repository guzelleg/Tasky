package com.guzelgimadieva.tasky.di

import android.content.Context
import android.content.SharedPreferences
import com.guzelgimadieva.tasky.core.data.local.UserPreferences
import com.guzelgimadieva.tasky.core.data.local.UserPreferencesImpl
import com.guzelgimadieva.tasky.core.network.TaskyService
import com.guzelgimadieva.tasky.core.network.TaskyServiceImpl
import com.guzelgimadieva.tasky.core.util.Auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {


    @Provides
    @Singleton
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideUserPreferences(sharedPreferences: SharedPreferences): UserPreferences {
        return UserPreferencesImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideTaskyService(
        prefs: UserPreferences
    ): TaskyService {
        return TaskyServiceImpl(prefs)
    }
}