package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.data.DataRepository
import com.chenzhang.mvi.data.Recording
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit.SECONDS

class ApiRepository(private val dataRepository: DataRepository) {

    fun loadRecordings(): Single<List<Recording>> =
            Observable.fromIterable(dataRepository.loadCurrentRecordings())
                    .delay(2, SECONDS)
                    .toList()

    fun deleteRecording(recording: Recording): Completable {
        dataRepository.deleteRecording(recording)
        return Completable.complete()
    }

    fun loadRecorderUsage(): Observable<Int> = Observable.just(dataRepository.getRecorderUsage()).delay(1, SECONDS)

}