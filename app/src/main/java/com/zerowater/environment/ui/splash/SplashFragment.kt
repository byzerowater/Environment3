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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.zerowater.environment.R
import com.zerowater.environment.databinding.SplashFragBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * Main UI for the splash screen.
 */
class SplashFragment : DaggerFragment() {
    private lateinit var viewDataBinding: SplashFragBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<SplashViewModel> { viewModelFactory }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.splash_frag, container, false)
        viewDataBinding = SplashFragBinding.bind(view).apply {
            viewmodel = viewModel
            lifecycleOwner = this@SplashFragment.viewLifecycleOwner
        }
        setHasOptionsMenu(true)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navigateToLogin()
    }

    private fun navigateToLogin() {
        val action = SplashFragmentDirections
                .actionLogin()
        findNavController().navigate(action)
    }

}
