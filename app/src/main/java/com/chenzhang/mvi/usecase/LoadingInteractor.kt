package com.chenzhang.mvi.usecase

import com.chenzhang.mvi.datarepository.DataRepository
import com.chenzhang.mvi.datamodel.Recording
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadingInteractor @Inject constructor(private val dataRepository: DataRepository)
    : RecordingsInteractor.ILoadingInteractor<Void, List<Recording>, Int> {

    override fun loadRecordings(): Single<List<Recording>> =
            Observable.fromIterable(dataRepository.loadCurrentRecordings())
                    //simulating loading latency
                    .delay(2, SECONDS)
                    .toList()

    override fun loadRecorderUsage(): Single<Int> = Single.just(dataRepository.getRecorderUsage()).delay(1, SECONDS)

}