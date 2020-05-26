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
package com.zerowater.environment.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.zerowater.environment.NavGraphDirections
import com.zerowater.environment.R
import com.zerowater.environment.data.Dialog
import com.zerowater.environment.databinding.HistoryFragBinding
import com.zerowater.environment.ui.dialog.DialogViewModel
import com.zerowater.environment.ui.dialog.DialogViewModel.DialogNavigation
import com.zerowater.environment.ui.history.HistoryViewModel.HistoryNavigation
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

/**
 * Environment
 * Class: HistoryFragment
 * Created by ZERO on 2020-05-18.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description: Main UI for the history screen.
 */
class HistoryFragment : DaggerFragment() {
    private lateinit var viewDataBinding: HistoryFragBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<HistoryViewModel> { viewModelFactory }
    private val dialogViewModel by activityViewModels<DialogViewModel> { viewModelFactory }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Timber.i("onCreateView")
        val view = inflater.inflate(R.layout.history_frag, container, false)
        viewDataBinding = HistoryFragBinding.bind(view).apply {
            viewmodel = viewModel
            lifecycleOwner = this@HistoryFragment.viewLifecycleOwner
            adapter = HistoryAdapter(viewModel)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.navigation.observe(viewLifecycleOwner, Observer {
            Timber.i("viewModel navigation %s", it)
            when (it.getContentIfNotHandled()) {
                HistoryNavigation.DIALOG -> navigateToDialog()
                else -> Timber.i("navigation else")
            }
        })

        dialogViewModel.navigation.observe(viewLifecycleOwner, Observer {
            Timber.i("dialogViewModel navigation %s", it)
            when (it.getContentIfNotHandled()) {
                DialogNavigation.LEFT -> Timber.i("left")
                DialogNavigation.RIGHT -> Timber.i("right")
                else -> Timber.i("navigation else")
            }
        })
    }

    private fun navigateToDialog() {
        val action = NavGraphDirections.actionDialog(Dialog("test", "test", "right"))
        findNavController().navigate(action)
    }
}