/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zerowater.environment.data.source

import androidx.lifecycle.LiveData
import com.zerowater.environment.Event
import com.zerowater.environment.data.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Default implementation of [Repository]. Single entry point for managing data.
 */
class DefaultRepository @Inject constructor(
        private val remoteDataSource: RemoteDataSource,
        private val localDataSource: LocalDataSource,
        private val resourceDataSource: ResourceDataSource,
        private val liveDataSource: LiveDataSource,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : Repository {
    override suspend fun getVersion(): Result<Version> {
        return remoteDataSource.getVersion()
    }

    override suspend fun login(auth: Auth): Result<Token> {
        return remoteDataSource.login(auth).apply {
            if (this is Result.Success) {
                localDataSource.putAccessToken(this.data.accessToken)
                localDataSource.putRefreshToken(this.data.refreshToken)
                localDataSource.putAccessTokenExpiredTime(this.data.accessTokenExpiredTime)
            }
        }
    }

    override suspend fun refreshToken(): Result<Token> {
        return remoteDataSource.refreshToken(Token(accessToken = localDataSource.getAccessToken(), refreshToken = localDataSource.getRefreshToken())).apply {
            if (this is Result.Success) {
                localDataSource.putAccessToken(this.data.accessToken)
                localDataSource.putAccessTokenExpiredTime(this.data.accessTokenExpiredTime)
            }
        }
    }

    override suspend fun getSchedule(): Result<Schedule> {
        val token = refreshTokenExpiredTime()

        return if (token is Result.Success)
            remoteDataSource.getSchedule()
        else
            Result.Error(Exception("expired time"))
    }

    override fun getAccessToken(): String? {
        return localDataSource.getAccessToken()
    }

    override fun getNetworkOperatorName(): String? {
        return resourceDataSource.getNetworkOperatorName()
    }

    override fun getUDID(): String? {
        if (localDataSource.getUDID().isNullOrBlank()) {
            resourceDataSource.getUDID()?.let {
                localDataSource.putUDID(it)
            }
        }
        return localDataSource.getUDID()
    }

    override fun isIgnoringBatteryOptimizations(): Boolean {
        return resourceDataSource.isIgnoringBatteryOptimizations()
    }

    override fun checkSelfPermission(): Boolean {
        return resourceDataSource.checkSelfPermission()
    }

    override fun getIntentLiveData(): LiveData<Event<Result<String>>> {
        return liveDataSource.getIntentLiveData()
    }

    override fun postIntentLiveData(data: Result<String>) {
        liveDataSource.postIntentLiveData(data)
    }

    private suspend fun refreshTokenExpiredTime(): Result<Token> {
        localDataSource.getAccessTokenExpiredTime()?.let {
            if (it - System.currentTimeMillis() < REFRESH_TOKEN_EXPIRED_TIME) {
                return refreshToken()
            }
        }

        return Result.Success(Token(localDataSource.getAccessToken(), localDataSource.getAccessTokenExpiredTime(), localDataSource.getRefreshToken()))
    }
}

const val REFRESH_TOKEN_EXPIRED_TIME = 10 * 60 * 1000
