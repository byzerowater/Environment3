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

import android.content.Context
import com.zerowater.environment.EnvironmentApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Environment
 * Class: ApplicationComponent
 * Created by ZERO on 2020-05-18.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description: Main component for the application.
 */
@Singleton
@Component(
        modules = [
            ApplicationModule::class,
            AndroidSupportInjectionModule::class,
            DialogModule::class,
            SplashModule::class,
            LoginModule::class,
            MainModule::class,
            PermissionDialogModule::class,
            HomeModule::class,
            HistoryModule::class,
            MoreModule::class
        ])
interface ApplicationComponent : AndroidInjector<EnvironmentApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}
