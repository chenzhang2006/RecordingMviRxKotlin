package com.chenzhang.mvi.injection

import android.app.Application
import com.chenzhang.mvi.recordings.DeletingInteractor
import com.chenzhang.mvi.recordings.LoadingInteractor
import com.chenzhang.mvi.recordings.RecordingsViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Dagger modules where dependencies are provided; Other dependencies are annotated with inline @Injected
 */
@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun providesApplication() = application

    @Provides
    @Singleton
    fun providesRecordingsViewModelFactory(loadingInteractor: LoadingInteractor, deletingInteractor: DeletingInteractor)
            = RecordingsViewModelFactory(loadingInteractor, deletingInteractor)

}
