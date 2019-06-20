package com.chenzhang.mvi.usecase

import com.chenzhang.mvi.datarepository.DataRepository
import com.chenzhang.mvi.datamodel.Recording
import io.reactivex.Completable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeletingInteractor @Inject constructor(private val dataRepository: DataRepository)
    : RecordingsInteractor.IDeleteInteractor<Recording> {

    override fun deleteRecording(recording: Recording): Completable {
        dataRepository.deleteRecording(recording)
        return Completable.complete()
    }

}