package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.base.MviAction
import com.chenzhang.mvi.data.Recording

/**
 * Model Action types
 */
sealed class RecordingsAction : MviAction {
    object LoadRecordingsAction : RecordingsAction()
    data class DeleteRecordingAction(val recording: Recording) : RecordingsAction()
}
