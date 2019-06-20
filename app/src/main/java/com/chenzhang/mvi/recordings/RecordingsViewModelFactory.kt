package com.chenzhang.mvi.recordings

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

/**
 * Factory for ViewModels
 */
@Suppress("UNCHECKED_CAST")
class RecordingsViewModelFactory(private val loadingInteractor: LoadingInteractor,
                                 private val deletingInteractor: DeletingInteractor) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            RecordingsViewModel::class.java ->
                RecordingsViewModel(RecordingsIntentProcessors(loadingInteractor = loadingInteractor,
                        deletingInteractor = deletingInteractor)) as T
            else ->
                throw IllegalArgumentException("Unknown ViewModel type")
        }
    }

}
