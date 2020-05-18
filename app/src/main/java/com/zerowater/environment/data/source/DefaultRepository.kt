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

import com.zerowater.environment.data.Result
import com.zerowater.environment.data.Version
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Default implementation of [Repository]. Single entry point for managing data.
 */
class DefaultRepository @Inject constructor(
        private val remoteDataSource: NetworkDataSource,
        private val localPreferencesDataSource: PreferencesDataSource,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : Repository {
    override suspend fun getVersion(): Result<Version> {
        return remoteDataSource.getVersion()
    }

    override fun getAuthToken(): String {
        return localPreferencesDataSource.getAuthToken()
    }

    override fun putAuthToken(authToken: String) {
        localPreferencesDataSource.putAuthToken(authToken)
    }


}
