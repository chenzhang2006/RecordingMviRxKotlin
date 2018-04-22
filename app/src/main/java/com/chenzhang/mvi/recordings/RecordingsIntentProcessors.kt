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

class RecordingsIntentProcessors(private val apiRepository: ApiRepository) {

    val actionProcessor =
            ObservableTransformer<RecordingsAction, RecordingsResult> { actions ->
                //publish to ConnectableObservable so upstream is subscribed only once and multicast to observers
                actions.publish { actionObserable ->
                    Observable.merge<RecordingsResult>(
                            actionObserable.ofType(RecordingsAction.LoadRecordingsAction::class.java).compose(loadRecordingsProcessor),
                            actionObserable.ofType(RecordingsAction.DeleteRecordingAction::class.java).compose(deleteRecordingProcessor)
                    )
                }
            }

    private val loadRecordingsProcessor =
            ObservableTransformer<LoadRecordingsAction, LoadingResult> { action ->
                action.flatMap {
                    apiRepository.loadRecordings()
                            .toObservable()
                            .zipWith(apiRepository.loadRecorderUsage())
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
                    apiRepository.deleteRecording(a.recording)
                            .andThen(apiRepository.loadRecordings())
                            .toObservable()
                            .map { r: List<Recording> ->
                                DeleteResult.DeleteSuccess(r)
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

