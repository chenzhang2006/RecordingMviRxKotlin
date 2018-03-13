package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.data.Recording
import com.chenzhang.mvi.base.MviResult

sealed class RecordingsResult : MviResult {
    data class LoadingSuccess(val recordings: List<Recording>) : RecordingsResult()
    data class LoadingFailure(val error: Throwable) : RecordingsResult()
}
