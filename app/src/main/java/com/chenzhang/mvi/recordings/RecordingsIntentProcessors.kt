package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.data.Recording
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun Observable<RecordingsAction>.processRecordingIntent(): Observable<RecordingsResult> =
    ofType(RecordingsAction.LoadRecordingsAction::class.java).loadRecordings()


fun Observable<RecordingsAction.LoadRecordingsAction>.loadRecordings(): Observable<RecordingsResult> =
        flatMap {
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
        }

