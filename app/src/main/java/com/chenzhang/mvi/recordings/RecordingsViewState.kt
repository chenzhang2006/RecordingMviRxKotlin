package com.chenzhang.mvi.recordings

import com.chenzhang.mvi.data.Recording
import com.chenzhang.mvi.base.MviViewState

data class RecordingsViewState(
        val isLoading: Boolean = false,
        val recordings: List<Recording> = emptyList(),
        val error: Throwable? = null
) : MviViewState