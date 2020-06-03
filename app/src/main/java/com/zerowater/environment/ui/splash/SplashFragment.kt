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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.zerowater.environment.BuildConfig
import com.zerowater.environment.NavGraphDirections
import com.zerowater.environment.R
import com.zerowater.environment.data.Dialog
import com.zerowater.environment.databinding.SplashFragBinding
import com.zerowater.environment.ui.dialog.DialogNavigation
import com.zerowater.environment.ui.dialog.DialogType
import com.zerowater.environment.ui.dialog.DialogViewModel
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

/**
 * Environment
 * Class: SplashFragment
 * Created by ZERO on 2020-05-18.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description: Main for the Splash screen.
 */
class SplashFragment : DaggerFragment() {
    private lateinit var viewDataBinding: SplashFragBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<SplashViewModel> { viewModelFactory }
    private val dialogViewModel by activityViewModels<DialogViewModel> { viewModelFactory }

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
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.navigation.observe(viewLifecycleOwner, Observer {
            when (it.getContentIfNotHandled()) {
                SplashViewModel.SplashNavigation.LOGIN -> navigateToLogin()
                SplashViewModel.SplashNavigation.MAIN -> navigateToMain()
            }
        })

        viewModel.version.observe(viewLifecycleOwner, Observer {
            val action = NavGraphDirections.actionDialog(
                    DialogType.GENERAL,
                    Dialog(it.message ?: getString(R.string.text_update_guide),
                            if (it.isForceUpdate == true) getString(R.string.text_immediately_update) else getString(R.string.text_late),
                            if (it.isForceUpdate == false) getString(R.string.text_update) else null)
            )
            findNavController().navigate(action)
        })

        dialogViewModel.navigation.observe(viewLifecycleOwner, Observer {
            Timber.i("dialogViewModel navigation %s", it)
            val version = viewModel.version.value
            when (it.getContentIfNotHandled()) {
                DialogNavigation.LEFT -> {
                    Timber.i("left ${dialogViewModel.getType()}")
                    if (version!!.isForceUpdate == true) apkUpdate(version.storeUrl) else viewModel.navigationToNext()
                }
                DialogNavigation.RIGHT -> {
                    Timber.i("right ${dialogViewModel.getType()}")
                    apkUpdate(version!!.storeUrl)
                }
                else -> Timber.i("navigation else")
            }

        })
        viewModel.getVersion()
    }

    private fun navigateToLogin() {
        val action = SplashFragmentDirections
                .actionLogin()
        findNavController().navigate(action)
    }

    private fun navigateToMain() {
        val action = SplashFragmentDirections
                .actionMain()
        findNavController().navigate(action)
    }

    /**
     * 앱 다운로드
     *
     * @param url 다운로드 URL
     */
    private fun apkUpdate(url: String?) {
        var url: String? = url

        if (url.isNullOrBlank()) {
            url = "market://details?id=" + BuildConfig.APPLICATION_ID
        }
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
        activity!!.finish()
    }

}
