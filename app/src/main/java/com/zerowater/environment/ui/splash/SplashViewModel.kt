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
package com.zerowater.environment.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zerowater.environment.BuildConfig
import com.zerowater.environment.Event
import com.zerowater.environment.data.Result.Success
import com.zerowater.environment.data.Version
import com.zerowater.environment.data.source.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Environment
 * Class: SplashViewModel
 * Created by ZERO on 2020-05-18.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description: ViewModel for the Splash screen.
 */
class SplashViewModel @Inject constructor(
        private val repository: Repository
) : ViewModel() {

    private val _version = MutableLiveData<Version>()
    val version: LiveData<Version> = _version

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _navigation = MutableLiveData(Event(SplashNavigation.UNKNOWN))
    val navigation: LiveData<Event<SplashNavigation>> = _navigation

    fun getVersion() {
        viewModelScope.launch {
            repository.getVersion().run {
                val version = if (this is Success) this.data else null
                version?.let {
                    if (BuildConfig.VERSION_NAME.compareTo(it.version ?: "0") < 0) {
                        _version.value = version
                    } else {
                        navigationToNext()
                    }
                }
                delay(2000)
                navigationToNext()
            }
        }
    }

    fun navigationToNext() {
        navigation(
                if (repository.getAccessToken().isNullOrBlank())
                    SplashNavigation.LOGIN
                else
                    SplashNavigation.MAIN
        )
    }

    private fun navigation(navi: SplashNavigation) {
        _navigation.value = Event(navi)
    }

    enum class SplashNavigation {
        UNKNOWN, LOGIN, MAIN
    }
}


