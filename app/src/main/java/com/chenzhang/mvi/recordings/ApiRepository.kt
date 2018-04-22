package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.data.DataRepository
import com.chenzhang.mvi.data.Recording
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit.SECONDS

class ApiRepository(private val dataRepository: DataRepository) {
    private val recordings: List<Recording>
        get() = dataRepository.loadCurrentRecordings()

    fun loadRecordings(): Single<List<Recording>> =
            Observable.fromIterable(recordings)
                    .delay(2, SECONDS)
                    .toList()

    fun deleteRecording(recording: Recording): Completable {
        dataRepository.deleteReocrding(recording)
        return Completable.complete()
    }

    fun loadRecorderUsage(): Observable<Int> = Observable.just(56).delay(1, SECONDS)

}