package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.data.Recording
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun Observable<RecordingsIntent>.processRecordingIntent(): Observable<RecordingsResult> =
    ofType(RecordingsIntent.LoadRecordingsIntent::class.java).loadRecordings()


fun Observable<RecordingsIntent.LoadRecordingsIntent>.loadRecordings(): Observable<RecordingsResult> =
        ApiRepository.loadRecordings()
                .toObservable()
                .map { t: List<Recording> ->
                    RecordingsResult.LoadingSuccess(t)
                }
                .cast(RecordingsResult::class.java)
                .onErrorReturn(RecordingsResult::LoadingFailure)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(RecordingsResult.LoadingInProgress)

