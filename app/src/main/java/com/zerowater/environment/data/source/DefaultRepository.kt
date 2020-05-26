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

import com.zerowater.environment.data.Auth
import com.zerowater.environment.data.Result
import com.zerowater.environment.data.Token
import com.zerowater.environment.data.Version
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
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : Repository {
    override suspend fun getVersion(): Result<Version> {
        return remoteDataSource.getVersion()
    }

    override suspend fun getAuthToken(auth: Auth): Result<Token> {
        return remoteDataSource.getAuthToken(auth).apply {
            if (this is Result.Success) {
                localDataSource.putAuthToken(this.data.accessToken)
            }
        }
    }

    override fun getAuthToken(): String? {
        return localDataSource.getAuthToken()
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

}
