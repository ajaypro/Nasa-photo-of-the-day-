package com.deepak.nasa.data.network

import com.deepak.nasa.data.network.Endpoints.APOD_ENDPOINT
import com.deepak.nasa.data.network.response.Apod
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface NetworkService {

    @GET(APOD_ENDPOINT)
    fun doDefaultApodCall(@Query("api_key") apiKey: String): Single<Apod>

    @GET(APOD_ENDPOINT)
    fun doApodCall(
        @Query("api_key") apiKey: String,
        @Query("date") date: String): Single<Apod>

}