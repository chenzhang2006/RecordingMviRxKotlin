package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.data.Recording
import com.chenzhang.mvi.data.RecordingTime
import com.chenzhang.mvi.data.RecordingType.MOVIE
import com.chenzhang.mvi.data.RecordingType.SPORT
import com.chenzhang.mvi.data.RecordingType.TV
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*
import java.util.concurrent.TimeUnit.SECONDS

class ApiRepository {
    private val recordings: MutableList<Recording> =
            mutableListOf(Recording("101", "Recording 1", MOVIE, "Movie 1", RecordingTime(Date(), Date())),
                    Recording("102", "Recording 2", TV, "Movie 2"),
                    Recording("103", "Recording 3", SPORT, "Movie 3"))

    fun loadRecordings(): Single<List<Recording>> =
            Observable.fromIterable(recordings)
                    .delay(2, SECONDS)
                    .toList()

    fun deleteRecording(recording: Recording): Completable {
        recordings.remove(recording)
        return Completable.complete()
    }

    fun loadRecorderUsage(): Observable<Int> = Observable.just(56).delay(1, SECONDS)

    fun addRecordingThenLoad(): Single<List<Recording>> {
        addNewRecording()
        return Observable.fromIterable(recordings)
                .delay(2, SECONDS)
                .toList()
    }

    private fun addNewRecording() {
        val intId = Random().nextInt(100) + 104
        recordings += Recording(intId.toString(), "Recording $intId", MOVIE,"Movie $intId")
    }
}