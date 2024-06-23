package com.guzelgimadieva.tasky.di

import android.content.SharedPreferences
import com.guzelgimadieva.tasky.core.data.local.UserPreferences
import com.guzelgimadieva.tasky.core.data.local.UserPreferencesImpl
import dagger.Provides
import javax.inject.Singleton

@Provides
@Singleton
fun provideUserPreferences(sharedPreferences: SharedPreferences): UserPreferences {
    return UserPreferencesImpl(sharedPreferences)
}