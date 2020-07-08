package com.deepak.nasa

import android.app.Application
import com.deepak.nasa.di.component.ApplicationComponent
import com.deepak.nasa.di.component.DaggerApplicationComponent
import com.deepak.nasa.di.module.ApplicationModule

class NasaApplication: Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        injectDependencies()
    }

    private fun injectDependencies() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
    }

}