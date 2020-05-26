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
package com.zerowater.environment.ui.login

import androidx.lifecycle.*
import com.zerowater.environment.Event
import com.zerowater.environment.data.Auth
import com.zerowater.environment.data.Result
import com.zerowater.environment.data.UserDevice
import com.zerowater.environment.data.source.Repository
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


/**
 * Environment
 * Class: LoginViewModel
 * Created by ZERO on 2020-05-18.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description: ViewModel for the login screen.
 */
class LoginViewModel @Inject constructor(
        private val repository: Repository
) : ViewModel() {


    /**
     * 로그인 이름 LiveData
     */
    val name = MutableLiveData<String>("restdocsTest")

    /**
     * 로그인 번호 LiveData
     */
    val phone = MutableLiveData<String>("01012341234")

    /**
     * 로그인 버튼 활성 상태 LiveData
     */
    private val _loginEnabled = MediatorLiveData<Boolean>()
    val loginEnabled: LiveData<Boolean> = _loginEnabled

    /**
     * 키보드 열림 상태 LiveData
     */
    private val _openKeyboard = MutableLiveData(false)
    val openKeyboard: LiveData<Boolean> = _openKeyboard

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _navigation = MutableLiveData(Event(LoginNavigation.UNKNOWN))
    val navigation: LiveData<Event<LoginNavigation>> = _navigation

    init {
        _loginEnabled.addSource(name, Observer { s: String? -> _loginEnabled.value = !s.isNullOrBlank() and !phone.value.isNullOrBlank() })
        _loginEnabled.addSource(phone, Observer { s: String? -> _loginEnabled.value = !s.isNullOrBlank() and !name.value.isNullOrBlank() })
    }

    fun setOpenKeyboard(isOpen: Boolean) {
        _openKeyboard.value = isOpen
    }


    fun login() {
        viewModelScope.launch {
            repository.getAuthToken(
                    Auth(name.value!!,
                            phone.value!!,
                            UserDevice(deviceId = repository.getUDID(),
                                    pushId = "token",
                                    telecom = repository.getNetworkOperatorName()
                            )
                    )
            ).run {
                if (this is Result.Success) {
                    Timber.i("Result.Success")
                } else if (this is Result.Error) {
                    val error = this
                    Timber.i("Result.Error $error")
                }
                navigation(LoginNavigation.MAIN)
            }

        }
    }

    fun navigation(navi: LoginNavigation) {
        _navigation.value = Event(navi)
    }

    enum class LoginNavigation {
        UNKNOWN, MAIN
    }

}

