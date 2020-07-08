package com.deepak.nasa.data.repository

import com.deepak.nasa.BuildConfig.API_KEY
import com.deepak.nasa.data.network.NetworkService
import com.deepak.nasa.data.network.response.Apod
import io.reactivex.Single

class ApodRepository(private val networkService: NetworkService) {

    fun getApod(date : String?) : Single<Apod> = networkService.doApodCall(API_KEY,date ?: "2018-12-07")

    fun getDefaultApod() : Single<Apod> = networkService.doDefaultApodCall(API_KEY)

}