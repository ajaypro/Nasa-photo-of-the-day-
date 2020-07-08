package com.deepak.nasa.di.component

import com.deepak.nasa.di.ActivityScope
import com.deepak.nasa.di.module.ActivityModule
import com.deepak.nasa.ui.common.MainActivity
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(activity: MainActivity)
}