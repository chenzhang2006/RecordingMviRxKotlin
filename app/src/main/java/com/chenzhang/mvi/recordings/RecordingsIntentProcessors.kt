package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.data.Recording
import com.chenzhang.mvi.recordings.RecordingsAction.DeleteRecordingAction
import com.chenzhang.mvi.recordings.RecordingsAction.LoadRecordingsAction
import com.chenzhang.mvi.recordings.RecordingsResult.DeleteResult
import com.chenzhang.mvi.recordings.RecordingsResult.LoadingResult
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
                            .zipWith(loadingInteractor.loadRecorderUsage())
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
                            .zipWith(loadingInteractor.loadRecorderUsage())
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

