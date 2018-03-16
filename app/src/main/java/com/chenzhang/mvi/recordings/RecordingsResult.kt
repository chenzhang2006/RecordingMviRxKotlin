package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.base.MviResult
import com.chenzhang.mvi.data.Recording

sealed class RecordingsResult : MviResult {
    sealed class LoadingResult : RecordingsResult() {
        object LoadingInProgress : LoadingResult()
        data class LoadingSuccess(val recordings: List<Recording>) : LoadingResult()
        data class LoadingFailure(val error: Throwable) : LoadingResult()
    }
}
