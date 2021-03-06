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

package com.zerowater.environment.di

import androidx.lifecycle.ViewModel
import com.zerowater.environment.ui.dialog.DialogFragment
import com.zerowater.environment.ui.dialog.DialogViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Environment
 * Class: DialogModule
 * Created by ZERO on 2020-05-23.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description: Dagger module for the History Dialog feature.
 */
@Module
abstract class DialogModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun dialogFragment(): DialogFragment

    @Binds
    @IntoMap
    @ViewModelKey(DialogViewModel::class)
    abstract fun bindViewModel(viewmodel: DialogViewModel): ViewModel
}
