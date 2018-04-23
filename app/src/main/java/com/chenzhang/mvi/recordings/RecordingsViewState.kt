package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.base.MviViewState
import com.chenzhang.mvi.data.Recording

data class RecordingsViewState(
        val isLoading: Boolean = false,
        val recordings: List<Recording> = emptyList(),
        val recorderUsage: Int = 0,
        val error: Throwable? = null
) : MviViewState {
    companion object {
        //used for Rx#scan as initial state, which has to be diff from possible normal viewStates
        fun initial() = RecordingsViewState(recorderUsage = -1)
    }
}