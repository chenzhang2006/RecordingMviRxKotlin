package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.base.MviIntent

sealed class RecordingsIntent : MviIntent {
    //InitialIntent for the very first intent when screen starts(what to show on screen first)
    object InitialIntent : RecordingsIntent()
    object RefreshIntent : RecordingsIntent()
}
