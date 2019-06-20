package com.chenzhang.mvi.mvibase

import com.chenzhang.mvi.datamodel.Recording

/**
 * Model Action types
 */
sealed class RecordingsAction : MviAction {
    object LoadRecordingsAction : RecordingsAction()
    data class DeleteRecordingAction(val recording: Recording) : RecordingsAction()
}
