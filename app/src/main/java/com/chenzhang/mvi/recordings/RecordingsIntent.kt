package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.base.MviIntent

sealed class RecordingsIntent : MviIntent {
    object LoadRecordingsIntent : RecordingsIntent()
}
