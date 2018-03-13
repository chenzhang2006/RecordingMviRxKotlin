package com.chenzhang.mvi.recordings

import android.arch.lifecycle.ViewModel
import com.chenzhang.mvi.base.MviViewModel
import com.chenzhang.mvi.data.Recording
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit.SECONDS

class RecordingsViewModel : ViewModel(), MviViewModel<RecordingsIntent, RecordingsViewState> {

    /**
     * Proxy subject used to keep the stream alive even after the UI gets recycled.
     * This is basically used to keep ongoing events and the last cached State alive
     * while the UI disconnects and reconnects on config changes.
     */
    private val intentsSubject: PublishSubject<RecordingsIntent> = PublishSubject.create()
    private val stateObservable: Observable<RecordingsViewState> = bindIntent()

    private fun bindIntent(): Observable<RecordingsViewState> {
        return intentsSubject
                .map { RecordingsViewState(recordings = listOf(Recording("100", "Hello", "Introduction to ReactiveX"))) }
                .delay(2, SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun processIntents(intents: Observable<RecordingsIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<RecordingsViewState> = stateObservable

}
