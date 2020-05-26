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
package com.zerowater.environment.data.source.remote

import com.zerowater.environment.data.Auth
import com.zerowater.environment.data.Result
import com.zerowater.environment.data.Token
import com.zerowater.environment.data.Version
import com.zerowater.environment.data.source.RemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of the data source that adds a latency simulating network.
 */
class NetworkDataSource internal constructor(
        private val networkService: NetworkService,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RemoteDataSource {

    override suspend fun getVersion(): Result<Version> = withContext(ioDispatcher) {
        try {
            val version = networkService.getVersion()
            if (version != null) {
                return@withContext Result.Success(version)
            } else {
                return@withContext Result.Error(Exception("version is null"))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    override suspend fun getAuthToken(auth: Auth): Result<Token> = withContext(ioDispatcher) {
        try {
            val token = networkService.getAuthToken(auth)
            if (token != null) {
                return@withContext Result.Success(token)
            } else {
                return@withContext Result.Error(Exception("token is null"))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }
}
