package com.chenzhang.mvi.viewmodel

import com.chenzhang.mvi.mvibase.RecordingsAction
import com.chenzhang.mvi.datamodel.Recording
import com.chenzhang.mvi.mvibase.RecordingsAction.DeleteRecordingAction
import com.chenzhang.mvi.mvibase.RecordingsAction.LoadRecordingsAction
import com.chenzhang.mvi.mvibase.RecordingsResult
import com.chenzhang.mvi.mvibase.RecordingsResult.DeleteResult
import com.chenzhang.mvi.mvibase.RecordingsResult.LoadingResult
import com.chenzhang.mvi.usecase.DeletingInteractor
import com.chenzhang.mvi.usecase.LoadingInteractor
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers

/**
 * Holders for Rx Transformers, which are applied by ViewModel
 */
class RecordingsIntentProcessors(private val loadingInteractor: LoadingInteractor,
                                 private val deletingInteractor: DeletingInteractor) {

    val actionProcessor =
            ObservableTransformer<RecordingsAction, RecordingsResult> { actions ->
                //publish to ConnectableObservable so upstream is subscribed only once and multicast to observers
                actions.publish { actionObservable ->
                    Observable.merge<RecordingsResult>(
                            actionObservable.ofType(LoadRecordingsAction::class.java).compose(loadRecordingsProcessor),
                            actionObservable.ofType(DeleteRecordingAction::class.java).compose(deleteRecordingProcessor)
                    )
                }
            }

    private val loadRecordingsProcessor =
            ObservableTransformer<LoadRecordingsAction, LoadingResult> { action ->
                action.flatMap {
                    loadingInteractor.loadRecordings()
                            .toObservable()
                            .zipWith(loadingInteractor.loadRecorderUsage().toObservable())
                            .map { pair: Pair<List<Recording>, Int> ->
                                LoadingResult.LoadingSuccess(pair.first, pair.second)
                            }
                            .cast(LoadingResult::class.java)
                            .onErrorReturn(LoadingResult::LoadingFailure)
                            .startWith(LoadingResult.LoadingInProgress)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
            }

    private val deleteRecordingProcessor =
            ObservableTransformer<DeleteRecordingAction, DeleteResult> { action ->
                action.flatMap { a ->
                    deletingInteractor.deleteRecording(a.recording)
                            .andThen(loadingInteractor.loadRecordings())
                            .toObservable()
                            .zipWith(loadingInteractor.loadRecorderUsage().toObservable())
                            .map { pair: Pair<List<Recording>, Int> ->
                                DeleteResult.DeleteSuccess(pair.first, pair.second)
                            }
                            .cast(DeleteResult::class.java)
                            .onErrorReturn(DeleteResult::DeleteFailure)
                            .startWith(DeleteResult.DeleteInProgress)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
            }

    fun uncleared() {
        //No ops; Cancel callbacks to avoid memory leak, if applicable
    }
}

