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

import com.zerowater.environment.data.source.PreferencesDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Concrete implementation of a data source as a db.
 */
class LocalPreferencesDataSource internal constructor(
        private val sharedPreferences: SharedPreferencesCache,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PreferencesDataSource