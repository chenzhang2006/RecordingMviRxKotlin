package com.chenzhang.mvi.usecase

import com.chenzhang.mvi.datarepository.DataRepository
import com.chenzhang.mvi.datamodel.Recording
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This is a simplified version of Use Case layer in CLEAN architecture. In reality,
 *
 * - it should handle Error case: e.g. propagate, retry, type mapping
 * - if "recording" and "usage" loading always happen all together, the combination of observables should happen here
 *
 * See comment in "RecordingsInteractor" to see more details about "Use Case" layer
 */
@Singleton
class LoadingInteractor @Inject constructor(private val dataRepository: DataRepository)
    : RecordingsInteractor.ILoadingInteractor<Void, List<Recording>, Int> {

    override fun loadRecordings(): Single<List<Recording>> =
            dataRepository.loadCurrentRecordings()
                    //simulating loading latency
                    .delay(2, SECONDS)

    override fun loadRecorderUsage(): Single<Int> =
            dataRepository.getRecorderUsage()
                    .map {
                        when {
                            it > 100 -> 100
                            it < 0 -> 0
                            else -> it
                        }
                    }
                    .delay(1, SECONDS)

}