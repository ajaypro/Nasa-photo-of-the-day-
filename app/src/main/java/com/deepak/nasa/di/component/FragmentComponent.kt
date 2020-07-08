package com.deepak.nasa.di.component

import com.deepak.nasa.di.FragmentScope
import com.deepak.nasa.di.module.FragmentModule
import com.deepak.nasa.ui.common.HomeFragment
import com.deepak.nasa.ui.photo.PhotoFragment
import com.deepak.nasa.ui.video.VideoFragment
import dagger.Component

@FragmentScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(fragment: HomeFragment)

    fun inject(fragment: PhotoFragment)

    fun inject(fragment: VideoFragment)

}