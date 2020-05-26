package com.zerowater.environment.data.source.remote

import com.zerowater.environment.BuildConfig
import com.zerowater.environment.data.Auth
import com.zerowater.environment.data.Token
import com.zerowater.environment.data.Version
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NetworkService {

    /**
     * 앱 버전
     *
     * @return 버전
     */
    @GET("app/version?appPackage=${BuildConfig.APPLICATION_ID}&osType=1")
    suspend fun getVersion(): Version?

    /**
     * 앱 버전
     *
     * @return 버전
     */
    @POST("auth/login")
    suspend fun getAuthToken(@Body auth: Auth): Token?
}