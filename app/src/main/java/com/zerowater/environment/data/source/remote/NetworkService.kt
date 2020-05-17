package com.zerowater.environment.data.source.remote

import com.zerowater.environment.data.Version
import retrofit2.http.GET

interface NetworkService {

    /**
     * 앱 버전
     *
     * @return 버전
     */
    @GET("comm/version")
    suspend fun getVersion(): Version
}