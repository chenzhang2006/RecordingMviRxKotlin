package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.base.MviResult
import com.chenzhang.mvi.data.Recording

sealed class RecordingsResult : MviResult {
    object LoadingInProgress : RecordingsResult()
    data class LoadingSuccess(val recordings: List<Recording>) : RecordingsResult()
    data class LoadingFailure(val error: Throwable) : RecordingsResult()
}
