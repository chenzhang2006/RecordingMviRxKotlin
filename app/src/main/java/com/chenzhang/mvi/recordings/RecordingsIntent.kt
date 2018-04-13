package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.base.MviIntent
import com.chenzhang.mvi.data.Recording

sealed class RecordingsIntent : MviIntent {
    //InitialIntent for the very first intent when screen starts(what to show on screen first)
    object InitialIntent : RecordingsIntent()
    object RefreshIntent : RecordingsIntent()
    data class DeleteIntent(val recording: Recording) : RecordingsIntent()
    object AddIntent : RecordingsIntent()
}
