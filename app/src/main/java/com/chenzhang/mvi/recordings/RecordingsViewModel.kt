package com.chenzhang.mvi.recordings

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.chenzhang.mvi.base.MviViewModel
import com.chenzhang.mvi.recordings.RecordingsResult.LoadingResult
import com.chenzhang.mvi.recordings.RecordingsResult.LoadingResult.LoadingFailure
import com.chenzhang.mvi.recordings.RecordingsResult.LoadingResult.LoadingInProgress
import com.chenzhang.mvi.recordings.RecordingsResult.LoadingResult.LoadingSuccess
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class RecordingsViewModel(
        private val recordingsIntentProcessors: RecordingsIntentProcessors
) : ViewModel(), MviViewModel<RecordingsIntent, RecordingsViewState> {

    /**
     * Proxy subject used to keep the stream alive even after the UI gets recycled.
     * This is basically used to keep ongoing events and the last cached State alive
     * while the UI disconnects and reconnects on config changes.
     */
    private val intentsSubject: PublishSubject<RecordingsIntent> = PublishSubject.create()
    private val stateObservable: Observable<RecordingsViewState> = bindIntent()

    private fun bindIntent(): Observable<RecordingsViewState> {
        return intentsSubject
                .map(this::actionMappedFromIntent)
                .compose(recordingsIntentProcessors.actionProcessor)
                .doOnNext { Log.d("RecordingViewModel","track $it") }
                .scan(RecordingsViewState.initial(), reducer)
                .replay(1)
                .autoConnect()
    }

    override fun processIntents(intents: Observable<RecordingsIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<RecordingsViewState> = stateObservable

    /**
     * Map MviIntent to Business Logic's MviAction
     */
    private fun actionMappedFromIntent(intent: RecordingsIntent): RecordingsAction {
        return when (intent) {
            is RecordingsIntent.InitialIntent, is RecordingsIntent.RefreshIntent ->
                    RecordingsAction.LoadRecordingsAction
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: RecordingsViewState, result: RecordingsResult ->
            when (result) {
                is LoadingResult -> when (result) {
                    is LoadingSuccess -> previousState.copy(false, result.recordings)
                    is LoadingFailure -> previousState.copy(false, error = result.error)
                    is LoadingInProgress -> previousState.copy(true)
                }
            }
        }
    }

}
