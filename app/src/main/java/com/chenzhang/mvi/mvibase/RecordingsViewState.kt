package com.chenzhang.mvi.mvibase

import com.chenzhang.mvi.datamodel.Recording

/**
 * Consolidated ViewState, maintained in ViewModel as "source-of-truth" for View states
 */
data class RecordingsViewState(
        val isLoading: Boolean = false,
        val recordings: List<Recording> = emptyList(),
        val recorderUsage: Int = 0,
        val error: Throwable? = null
) : MviViewState {
    companion object {
        //used for Rx#scan as initial state, which has to be diff from possible normal viewStates
        fun scanInitialState() = RecordingsViewState(recorderUsage = -1)
    }
}