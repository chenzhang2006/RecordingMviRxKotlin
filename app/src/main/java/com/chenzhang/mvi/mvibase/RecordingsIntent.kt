package com.chenzhang.mvi.mvibase

import com.chenzhang.mvi.datamodel.Recording

/**
 * Model User-Intent
 */
sealed class RecordingsIntent : MviIntent {
    //InitialIntent for the very first intent when screen starts
    object InitialIntent : RecordingsIntent()

    object RefreshIntent : RecordingsIntent()
    data class DeleteIntent(val recording: Recording) : RecordingsIntent()
}
