package com.deepak.nasa.di.module

import androidx.lifecycle.ViewModelProvider
import com.deepak.nasa.data.repository.ApodRepository
import com.deepak.nasa.ui.BaseFragment
import com.deepak.nasa.ui.common.CommonViewModel
import com.deepak.nasa.ui.common.CommonViewModelFactory
import com.deepak.nasa.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class FragmentModule(private val fragment: BaseFragment) {

    @Provides
    fun providesCommonViewModel(
        compositeDisposable: CompositeDisposable,
        apodRepository: ApodRepository,
        schedulerProvider: SchedulerProvider
    ): CommonViewModel = ViewModelProvider(fragment.requireActivity(), CommonViewModelFactory(apodRepository, compositeDisposable, schedulerProvider)).get(CommonViewModel::class.java)

}