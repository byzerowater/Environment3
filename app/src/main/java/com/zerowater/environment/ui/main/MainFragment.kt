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
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.zerowater.environment.NavGraphDirections
import com.zerowater.environment.R
import com.zerowater.environment.databinding.MainFragBinding
import com.zerowater.environment.ui.history.HistoryFragment
import com.zerowater.environment.ui.home.HomeFragment
import com.zerowater.environment.ui.main.MainViewModel.MainNavigation
import com.zerowater.environment.ui.more.MoreFragment
import com.zerowater.environment.ui.permission.APP_PERMISSION
import com.zerowater.environment.ui.permission.PermissionDialogViewModel
import com.zerowater.environment.ui.permission.REQUEST_PERMISSION_CODE
import com.zerowater.environment.util.addFragment
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

/**
 * Environment
 * Class: MainFragment
 * Created by ZERO on 2020-05-18.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description: Main UI for the main screen.
 */
class MainFragment : DaggerFragment() {
    private lateinit var viewDataBinding: MainFragBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MainViewModel> { viewModelFactory }
    private val dialogViewModel by activityViewModels<PermissionDialogViewModel> { viewModelFactory }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Timber.i("onCreateView")
        val view = inflater.inflate(R.layout.main_frag, container, false)
        viewDataBinding = MainFragBinding.bind(view).apply {
            viewmodel = viewModel
            lifecycleOwner = this@MainFragment.viewLifecycleOwner
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.permission.observe(viewLifecycleOwner, Observer {
            val isPermission = it.getContentIfNotHandled()
            isPermission?.let {
                if (!isPermission) {
                    findNavController().navigate(NavGraphDirections.actionPermission())
                }
            }
        })

        viewModel.navigation.observe(viewLifecycleOwner, Observer {
            Timber.i("navigation %s", it)
            when (it.getContentIfNotHandled()) {
                MainNavigation.HOME -> addFragment(R.id.container, HomeFragment())
                MainNavigation.HISTORY -> addFragment(R.id.container, HistoryFragment())
                MainNavigation.MORE -> addFragment(R.id.container, MoreFragment())
                else -> Timber.i("navigation else")
            }
        })

        dialogViewModel.navigation.observe(viewLifecycleOwner, Observer {
            Timber.i("dialogViewModel navigation %s", it)
            when (it.getContentIfNotHandled()) {
                PermissionDialogViewModel.PersmissionDialogNavigation.CONFIRM -> ActivityCompat.requestPermissions(activity!!, APP_PERMISSION, REQUEST_PERMISSION_CODE)
                else -> Timber.i("navigation else")
            }

        })
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("onDestroyView")
    }
}
