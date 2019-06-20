package com.chenzhang.mvi.usecase

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * CLEAN architecture's "Use Case" layer. Typically, this layer does:
 *
 * - Orchestrate and combine results from underlying Data Repositories; Manage business rules, such as polling, filter, zip, concat...
 * - Handle errors, e.g. propagating, retry, omitting
 * - Stateless and re-usable
 */

interface RecordingsInteractor {

    interface ILoadingInteractor<Void, Return1, Return2> : RecordingsInteractor {

        fun loadRecordings(): Single<Return1>

        fun loadRecorderUsage(): Observable<Return2>
    }

    interface IDeleteInteractor<T> : RecordingsInteractor {
        fun deleteRecording(recording: T): Completable
    }

}