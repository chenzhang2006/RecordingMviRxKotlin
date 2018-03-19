package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.data.Recording
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit.SECONDS

class ApiRepository {
    private val recordings: MutableList<Recording> =
            mutableListOf(Recording("100", "Hello", "Introduction to ReactiveX"),
                    Recording("101", "World"))

    fun loadRecordings(): Single<List<Recording>> =
            Observable.fromIterable(recordings)
                    .delay(2, SECONDS)
                    .toList()

    fun deleteRecording(position: Int): Completable {
        recordings.removeAt(position)
        return Completable.complete()
    }
}