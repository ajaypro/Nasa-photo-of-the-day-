package com.deepak.nasa.di.module

import android.app.Application
import android.content.Context
import com.deepak.nasa.BuildConfig
import com.deepak.nasa.NasaApplication
import com.deepak.nasa.data.network.NetworkService
import com.deepak.nasa.data.network.Networking
import com.deepak.nasa.data.repository.ApodRepository
import com.deepak.nasa.di.ApplicationContext
import com.deepak.nasa.utils.rx.RxSchedulerProvider
import com.deepak.nasa.utils.rx.SchedulerProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: NasaApplication) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    @ApplicationContext
    fun provideContext(): Context = application

    @Provides
    @Singleton
    fun provideNetworkService(): NetworkService =
        Networking.create(
            BuildConfig.API_KEY,
            BuildConfig.BASE_URL,
            application.cacheDir,
            10 * 1024 * 1024 // 10MB
        )

    @Provides
    fun providePicasso(@ApplicationContext context: Context): Picasso {
        return Picasso.Builder(context)
            .loggingEnabled(true)
            .downloader(OkHttp3Downloader(context))
            .build()
    }

    @Provides
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
        return gsonBuilder.create()
    }

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun providesApodRepository(networkService: NetworkService): ApodRepository = ApodRepository(networkService)

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = RxSchedulerProvider()

}