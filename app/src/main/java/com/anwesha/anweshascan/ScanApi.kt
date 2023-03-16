package com.anwesha.anweshascan


import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

import retrofit2.http.Query

const val BASE_URL = "https://backend.anwesha.live"

interface ScanApi {


    @GET("/event/allevents")
    fun getAllEvents() : Call<MutableList<EventsList>>

    @POST("/event/checkeventregistration/")
    suspend fun getUserById(@Body scanApiData: ScanApiData): Response<ScanApiResponse>

    @POST("/event/updateentrystatus/")
    suspend fun postUserCurrentInfo(@Body currUserStatus: CurrentUserStatus): Response<CurrentUserResponse>
}

object QRScanResult {
    val scanapi: ScanApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        scanapi = retrofit.create(ScanApi::class.java)
    }
}