package com.chenzhang.mvi.injection

import android.app.Application
import com.chenzhang.mvi.recordings.ApiRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val application: Application) {

    @Provides
    @Singleton
    fun providesApplication() = application

    @Provides
    @Singleton
    fun providesApiRepository() = ApiRepository
}
