package com.deepak.nasa.di

import javax.inject.Scope


@Scope
@Retention(AnnotationRetention.SOURCE)
annotation class FragmentScope

@Scope
@Retention(AnnotationRetention.SOURCE)
annotation class ActivityScope