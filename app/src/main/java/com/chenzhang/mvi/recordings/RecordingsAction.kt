package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.base.MviAction

sealed class RecordingsAction : MviAction {
    object LoadRecordingsAction : RecordingsAction()
}
