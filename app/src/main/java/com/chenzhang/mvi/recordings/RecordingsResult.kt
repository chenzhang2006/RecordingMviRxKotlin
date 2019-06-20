package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.base.MviResult
import com.chenzhang.mvi.data.Recording

/**
 * Model Result types, such as Loading, ResultData, Failure
 */
sealed class RecordingsResult : MviResult {
    sealed class LoadingResult : RecordingsResult() {
        object LoadingInProgress : LoadingResult()
        data class LoadingSuccess(val recordings: List<Recording>, val recorderUsage: Int) : LoadingResult()
        data class LoadingFailure(val error: Throwable) : LoadingResult()
    }

    sealed class DeleteResult : RecordingsResult() {
        data class DeleteSuccess(val recordings: List<Recording>, val recorderUsage: Int) : DeleteResult()
        data class DeleteFailure(val error: Throwable) : DeleteResult()
        object DeleteInProgress : DeleteResult()
    }
}
