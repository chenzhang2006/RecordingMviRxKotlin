package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.data.DataRepository
import com.chenzhang.mvi.data.Recording
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject
import javax.inject.Singleton

/**
 * CLEAN architecture's "Use Case" layer. Typically, this layer does:
 *
 * - Orchestrate and combine results from underlying Data Repositories; Manage business rules, such as polling, filter, zip, concat...
 * - Handle errors, e.g. propagating, retry, omitting
 * - Stateless and re-usable
 */

@Singleton
class LoadingInteractor @Inject constructor(private val dataRepository: DataRepository)
    : ILoadingInteractor<Void, List<Recording>, Int> {

    override fun loadRecordings(): Single<List<Recording>> =
            Observable.fromIterable(dataRepository.loadCurrentRecordings())
                    //simulating loading latency
                    .delay(2, SECONDS)
                    .toList()

    override fun loadRecorderUsage(): Observable<Int> = Observable.just(dataRepository.getRecorderUsage()).delay(1, SECONDS)

}