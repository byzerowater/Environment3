package com.zerowater.environment.data.source.remote

import com.zerowater.environment.BuildConfig
import com.zerowater.environment.data.Auth
import com.zerowater.environment.data.Schedule
import com.zerowater.environment.data.Token
import com.zerowater.environment.data.Version
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NetworkService {


    @GET("app/version?packageId=${BuildConfig.APPLICATION_ID}&osType=1")
    suspend fun getVersion(): Version?

    @POST("auth/login")
    suspend fun login(@Body auth: Auth): Token?

    @POST("auth/refresh")
    suspend fun refreshToken(@Body token: Token): Token?

    @GET("commute/schedule")
    suspend fun getSchedule(): Schedule?
}