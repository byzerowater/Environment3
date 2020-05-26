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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.zerowater.environment.R
import com.zerowater.environment.databinding.LoginFragBinding
import dagger.android.support.DaggerFragment
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import timber.log.Timber
import javax.inject.Inject

/**
 * Environment
 * Class: LoginFragment
 * Created by ZERO on 2020-05-18.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description: Main UI for the login screen.
 */
class LoginFragment : DaggerFragment() {
    private lateinit var viewDataBinding: LoginFragBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<LoginViewModel> { viewModelFactory }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.login_frag, container, false)
        viewDataBinding = LoginFragBinding.bind(view).apply {
            viewmodel = viewModel
            lifecycleOwner = this@LoginFragment.viewLifecycleOwner
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setEventListener(activity!!,
                viewLifecycleOwner,
                object : KeyboardVisibilityEventListener {
                    override fun onVisibilityChanged(isOpen: Boolean) {
                        viewModel.setOpenKeyboard(isOpen)
                    }
                })

        viewModel.navigation.observe(viewLifecycleOwner, Observer {
            when (it.getContentIfNotHandled()) {
                LoginViewModel.LoginNavigation.MAIN -> navigateToMain()
            }
        })
    }

    private fun navigateToMain() {
        val action = LoginFragmentDirections
                .actionMain()
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onCreateView")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.i("onCreateView")
    }

}