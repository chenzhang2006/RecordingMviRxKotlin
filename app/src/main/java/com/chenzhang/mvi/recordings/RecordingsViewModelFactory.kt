package com.chenzhang.mvi.recordings

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class RecordingsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            RecordingsViewModel::class.java ->
                    RecordingsViewModel(RecordingsIntentProcessors(ApiRepository())) as T
            else ->
                    throw IllegalArgumentException("Unknown ViewModel type")
        }
    }

}