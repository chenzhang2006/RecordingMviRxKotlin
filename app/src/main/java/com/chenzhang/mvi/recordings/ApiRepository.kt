package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.data.Recording
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit.SECONDS

object ApiRepository {
    fun loadRecordings(): Single<List<Recording>> =
            Observable.fromIterable(listOf(
                    Recording("100", "Hello", "Introduction to ReactiveX"),
                    Recording("101", "World")))
                    .delay(2, SECONDS)
                    .toList()
}