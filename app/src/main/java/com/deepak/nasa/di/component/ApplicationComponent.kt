package com.deepak.nasa.di.component

import android.app.Application
import android.content.Context
import com.deepak.nasa.NasaApplication
import com.deepak.nasa.data.network.NetworkService
import com.deepak.nasa.data.repository.ApodRepository
import com.deepak.nasa.di.ApplicationContext
import com.deepak.nasa.di.module.ApplicationModule
import com.deepak.nasa.utils.rx.SchedulerProvider
import com.squareup.picasso.Picasso
import dagger.Component
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(application: NasaApplication)

    fun getApplication(): Application

    @ApplicationContext
    fun getContext(): Context

    fun getNetworkService(): NetworkService

    fun getPicasso(): Picasso

    fun getCompositeDisposable(): CompositeDisposable

    fun getApodRepository(): ApodRepository

    fun getSchedulerProvider(): SchedulerProvider

}