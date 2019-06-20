package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.data.DataRepository
import com.chenzhang.mvi.data.Recording
import io.reactivex.Completable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeletingInteractor @Inject constructor(private val dataRepository: DataRepository)
    : IDeleteInteractor<Recording> {

    override fun deleteRecording(recording: Recording): Completable {
        dataRepository.deleteRecording(recording)
        return Completable.complete()
    }

}