package com.chenzhang.mvi.injection

import android.app.Application
import com.chenzhang.mvi.data.DataRepository
import com.chenzhang.mvi.recordings.ApiRepository
import com.chenzhang.mvi.recordings.RecordingsViewModelFactory
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
    fun providesRecordingsViewModelFactory(apiRepository: ApiRepository) = RecordingsViewModelFactory(apiRepository)

    @Provides
    @Singleton
    fun providesDataRepository() = DataRepository()

    @Provides
    @Singleton
    fun providesApiRepository(dataRepository: DataRepository) = ApiRepository(dataRepository)

}
