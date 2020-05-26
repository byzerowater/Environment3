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
import com.zerowater.environment.ui.permission.PermissionDialogFragment
import com.zerowater.environment.ui.permission.PermissionDialogViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Environment
 * Class: PermissionDialogModule
 * Created by ZERO on 2020-05-26.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description: Dagger module for the Permission Dialog feature.
 */
@Module
abstract class PermissionDialogModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun permissionDialogFragment(): PermissionDialogFragment

    @Binds
    @IntoMap
    @ViewModelKey(PermissionDialogViewModel::class)
    abstract fun bindViewModel(viewmodel: PermissionDialogViewModel): ViewModel
}
