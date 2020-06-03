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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zerowater.environment.R
import com.zerowater.environment.databinding.DialogDiaBinding
import com.zerowater.environment.util.initDialog
import dagger.android.support.DaggerDialogFragment
import timber.log.Timber
import javax.inject.Inject

/**
 * Environment
 * Class: DialogFragment
 * Created by ZERO on 2020-05-24.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description: Main for the dialog screen.
 */
class DialogFragment : DaggerDialogFragment() {
    private lateinit var viewDataBinding: DialogDiaBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val args: DialogFragmentArgs by navArgs()

    private val viewModel by viewModels<DialogViewModel> { viewModelFactory }
    private val activityViewModel by activityViewModels<DialogViewModel> { viewModelFactory }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Timber.i("onCreateView")
        initDialog()
        val view = inflater.inflate(R.layout.dialog_dia, container, false)
        viewDataBinding = DialogDiaBinding.bind(view).apply {
            viewmodel = viewModel
            data = args.data
            lifecycleOwner = this@DialogFragment
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.navigation.observe(viewLifecycleOwner, Observer {
            when (it.getContentIfNotHandled()) {
                DialogNavigation.LEFT, DialogNavigation.RIGHT -> {
                    findNavController().navigateUp()
                    activityViewModel.addType(args.type)
                    activityViewModel.navigation(it.peekContent())
                }
            }
        })
    }

}
