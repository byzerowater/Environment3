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
package com.zerowater.environment.ui.permission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zerowater.environment.Event
import com.zerowater.environment.data.source.Repository
import javax.inject.Inject

/**
 * Environment
 * Class: PermissionDialogViewModel
 * Created by ZERO on 2020-05-26.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description: ViewModel for the permission dialog screen.
 */
class PermissionDialogViewModel @Inject constructor(
        private val repository: Repository
) : ViewModel() {

    private val _navigation = MutableLiveData<Event<PersmissionDialogNavigation>>()
    val navigation: LiveData<Event<PersmissionDialogNavigation>> = _navigation


    fun navigation(navi: PersmissionDialogNavigation) {
        _navigation.value = Event(navi)
    }

    enum class PersmissionDialogNavigation {
        CONFIRM
    }

}




