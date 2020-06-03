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
package com.zerowater.environment.data.source.local

import com.zerowater.environment.data.source.LocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Concrete implementation of a data source as a db.
 */

private const val ACCESS_TOKEN: String = "accessToken"
private const val REFRESH_TOKEN: String = "refreshToken"
private const val ACCESS_TOKEN_EXPIRED_TIME: String = "accessTokenExpiredTime"
private const val UDID_TOKEN: String = "userDeviceIdentification"

class SharedPreferencesDataSource internal constructor(
        private val sharedPreferences: SharedPreferencesCache,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalDataSource {

    override fun getAccessToken(): String? = sharedPreferences.getString(ACCESS_TOKEN)
    override fun putAccessToken(accessToken: String?) {
        if (accessToken.isNullOrBlank()) {
            sharedPreferences.remove(ACCESS_TOKEN)
        } else {
            sharedPreferences.put(ACCESS_TOKEN, accessToken)
        }
    }

    override fun getRefreshToken(): String? = sharedPreferences.getString(REFRESH_TOKEN)
    override fun putRefreshToken(refreshToken: String?) {
        if (refreshToken.isNullOrBlank()) {
            sharedPreferences.remove(REFRESH_TOKEN)
        } else {
            sharedPreferences.put(REFRESH_TOKEN, refreshToken)
        }
    }

    override fun getAccessTokenExpiredTime(): Long? = sharedPreferences.getLong(ACCESS_TOKEN_EXPIRED_TIME)
    override fun putAccessTokenExpiredTime(accessTokenExpiredTime: Long?) = sharedPreferences.put(ACCESS_TOKEN_EXPIRED_TIME, accessTokenExpiredTime)

    override fun getUDID(): String? = sharedPreferences.getString(UDID_TOKEN)
    override fun putUDID(udid: String?) {
        if (udid.isNullOrBlank()) {
            sharedPreferences.remove(UDID_TOKEN)
        } else {
            sharedPreferences.put(UDID_TOKEN, udid)
        }
    }
}
