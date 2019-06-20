package com.chenzhang.mvi.recordings

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface RecordingsInteractor

interface ILoadingInteractor<Void, Return1, Return2> : RecordingsInteractor {

    fun loadRecordings(): Single<Return1>

    fun loadRecorderUsage(): Observable<Return2>
}

interface IDeleteInteractor<T>: RecordingsInteractor {
    fun deleteRecording(recording: T): Completable
}