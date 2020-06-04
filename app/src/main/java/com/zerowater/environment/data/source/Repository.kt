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

/**
 * Interface to the data layer.
 */
interface Repository {
    suspend fun getVersion(): Result<Version>

    suspend fun login(auth: Auth): Result<Token>

    suspend fun refreshToken(): Result<Token>

    suspend fun getSchedule(): Result<Schedule>

    fun getAccessToken(): String?

    fun getNetworkOperatorName(): String?

    fun getUDID(): String?

    fun isIgnoringBatteryOptimizations(): Boolean

    fun checkSelfPermission(): Boolean

    fun getIntentLiveData(): LiveData<Event<Result<IntentExtra>>>

    fun postIntentLiveData(data: Result<IntentExtra>)
}
