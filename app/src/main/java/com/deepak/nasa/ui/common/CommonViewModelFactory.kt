package com.deepak.nasa.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.deepak.nasa.data.repository.ApodRepository
import com.deepak.nasa.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 *
 * Provides the SleepDatabaseDao and context to the ViewModel.
 */
class CommonViewModelFactory(
    private val apodRepository: ApodRepository,
    private val compositeDisposable: CompositeDisposable,
    private val schedulerProvider: SchedulerProvider
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommonViewModel::class.java)) {
            return CommonViewModel(apodRepository, compositeDisposable, schedulerProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}