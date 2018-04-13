package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.data.Recording
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit.SECONDS

class ApiRepository {
    private val recordings: MutableList<Recording> =
            mutableListOf(Recording("101", "Recording 1", "Movie 1"),
                    Recording("102", "Recording 2", "Movie 2"),
                    Recording("103", "Recording 3", "Movie 3"))

    fun loadRecordings(): Single<List<Recording>> =
            Observable.fromIterable(recordings)
                    .delay(2, SECONDS)
                    .toList()

    fun deleteRecording(recording: Recording): Completable {
        recordings.remove(recording)
        return Completable.complete()
    }

    fun loadRecorderUsage(): Observable<Int> = Observable.just(56).delay(1, SECONDS)
}