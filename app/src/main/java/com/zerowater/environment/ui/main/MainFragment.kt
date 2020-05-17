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
package com.zerowater.environment.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.zerowater.environment.R
import com.zerowater.environment.databinding.MainFragBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * Main UI for the main screen.
 */
class MainFragment : DaggerFragment() {
    private lateinit var viewDataBinding: MainFragBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MainViewModel> { viewModelFactory }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.main_frag, container, false)
        viewDataBinding = MainFragBinding.bind(view).apply {
            viewmodel = viewModel
            viewmodel = viewModel
            lifecycleOwner = this@MainFragment.viewLifecycleOwner
        }

        setHasOptionsMenu(true)
        return view
    }

}
