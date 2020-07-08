package com.deepak.nasa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.deepak.nasa.NasaApplication
import com.deepak.nasa.di.component.DaggerFragmentComponent
import com.deepak.nasa.di.component.FragmentComponent
import com.deepak.nasa.di.module.FragmentModule
import com.deepak.nasa.ui.common.CommonViewModel
import com.deepak.nasa.utils.rx.SchedulerProvider
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseFragment: Fragment() {

    @Inject
    lateinit var picasso: Picasso

    @Inject
    lateinit var commonViewModel: CommonViewModel

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(buildFragmentComponent())
        super.onCreate(savedInstanceState)
    }

    private fun buildFragmentComponent() =
        DaggerFragmentComponent
            .builder()
            .applicationComponent((context?.applicationContext as NasaApplication).applicationComponent)
            .fragmentModule(FragmentModule(this))
            .build()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(provideLayoutId(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    @LayoutRes
    protected abstract fun provideLayoutId(): Int

    protected abstract fun injectDependencies(fragmentComponent: FragmentComponent)

    protected abstract fun setupView(view: View)

}