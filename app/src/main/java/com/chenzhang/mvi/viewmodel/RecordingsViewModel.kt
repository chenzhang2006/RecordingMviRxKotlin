package com.chenzhang.mvi.viewmodel

import android.arch.lifecycle.ViewModel
import com.chenzhang.mvi.mvibase.*
import com.chenzhang.mvi.mvibase.RecordingsAction.DeleteRecordingAction
import com.chenzhang.mvi.mvibase.RecordingsIntent.DeleteIntent
import com.chenzhang.mvi.mvibase.RecordingsResult.DeleteResult
import com.chenzhang.mvi.mvibase.RecordingsResult.DeleteResult.DeleteFailure
import com.chenzhang.mvi.mvibase.RecordingsResult.DeleteResult.DeleteInProgress
import com.chenzhang.mvi.mvibase.RecordingsResult.DeleteResult.DeleteSuccess
import com.chenzhang.mvi.mvibase.RecordingsResult.LoadingResult
import com.chenzhang.mvi.mvibase.RecordingsResult.LoadingResult.LoadingFailure
import com.chenzhang.mvi.mvibase.RecordingsResult.LoadingResult.LoadingInProgress
import com.chenzhang.mvi.mvibase.RecordingsResult.LoadingResult.LoadingSuccess
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import org.apache.log4j.Logger

/**
 * ViewModel in MVVM(Model View ViewModel)
 */
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
    private var latestViewState: RecordingsViewState? = null
    private val logger = Logger.getLogger(this::class.java)

    private fun bindIntent(): Observable<RecordingsViewState> {
        return intentsSubject
                .doOnNext { logger.debug("Tracking raw intent $it") }
                .initialFilter()
                .doOnNext { logger.debug("Tracking filtered intent $it") }
                .map(this::actionMappedFromIntent)
                .compose(recordingsIntentProcessors.actionProcessor)
                .scan(RecordingsViewState.scanInitialState(), reducer)
                .doOnNext {
                    latestViewState = it
                    logger.debug("Tracking viewState: $it")
                }
                .replay(1)
                .autoConnect()
    }

    //Take only the 1st initial intent to avoid reloading data on config change(because hotObservable.replay(1) already)
    private fun Observable<RecordingsIntent>.initialFilter() =
            publish { intent ->
                Observable.merge(
                        intent.ofType(RecordingsIntent.InitialIntent::class.java).take(1),
                        intent.filter { !RecordingsIntent.InitialIntent::class.java.isInstance(it) }
                )
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
            is DeleteIntent ->
                DeleteRecordingAction(intent.recording)
        }
    }

    override fun onCleared() {
        recordingsIntentProcessors.uncleared()
    }

    companion object {
        private val reducer = BiFunction { previousState: RecordingsViewState, result: RecordingsResult ->
            when (result) {
                is LoadingResult -> when (result) {
                    is LoadingSuccess -> previousState.copy(isLoading = false, recordings = result.recordings, recorderUsage = result.recorderUsage)
                    is LoadingFailure -> previousState.copy(isLoading = false, error = result.error)
                    is LoadingInProgress -> previousState.copy(isLoading = true)
                }
                is DeleteResult -> when (result) {
                    is DeleteSuccess -> previousState.copy(isLoading = false, recordings = result.recordings, recorderUsage = result.recorderUsage)
                    is DeleteFailure -> previousState.copy(isLoading = false, error = result.error)
                    is DeleteInProgress -> previousState.copy(isLoading = true)
                }
            }
        }
    }

}
