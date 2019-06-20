package com.chenzhang.mvi.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.chenzhang.mvi.usecase.DeletingInteractor
import com.chenzhang.mvi.usecase.LoadingInteractor

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
