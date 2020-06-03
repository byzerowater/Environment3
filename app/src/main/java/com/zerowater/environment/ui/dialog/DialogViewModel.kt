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
package com.zerowater.environment.ui.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zerowater.environment.Event
import com.zerowater.environment.data.source.Repository
import java.util.*
import javax.inject.Inject

/**
 * Environment
 * Class: DialogViewModel
 * Created by ZERO on 2020-05-24.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description: ViewModel for the dialog screen.
 */
class DialogViewModel @Inject constructor(
        private val repository: Repository
) : ViewModel() {

    private val _navigation = MutableLiveData(Event(DialogNavigation.UNKNOWN))
    val navigation: LiveData<Event<DialogNavigation>> = _navigation

    private val _type = Stack<DialogType>()

    fun navigation(navi: DialogNavigation) {
        _navigation.value = Event(navi)
    }

    fun getType(): DialogType {

        var type = DialogType.UNKNOWN
        if (_type.size > 0) {
            type = _type.pop()
        }
        return type
    }

    fun addType(type: DialogType) {
        _type.add(type)
    }
}

enum class DialogNavigation {
    UNKNOWN, LEFT, RIGHT
}
