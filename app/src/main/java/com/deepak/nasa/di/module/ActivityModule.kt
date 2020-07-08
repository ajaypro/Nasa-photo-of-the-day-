package com.deepak.nasa.di.module

import androidx.lifecycle.ViewModelProvider
import com.deepak.nasa.data.repository.ApodRepository
import com.deepak.nasa.ui.common.CommonViewModel
import com.deepak.nasa.ui.common.CommonViewModelFactory
import com.deepak.nasa.ui.common.MainActivity
import com.deepak.nasa.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class ActivityModule(private val activity: MainActivity) {

    @Provides
    fun providesCommonViewModel(
        compositeDisposable: CompositeDisposable,
        apodRepository: ApodRepository,
        schedulerProvider: SchedulerProvider
    ): CommonViewModel = ViewModelProvider(activity, CommonViewModelFactory(apodRepository, compositeDisposable, schedulerProvider)).get(
        CommonViewModel::class.java)
}