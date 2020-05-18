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
package com.zerowater.environment.util

/**
 * Extension functions and Binding Adapters.
 */

import androidx.fragment.app.Fragment

/**
 * Fragment 추가
 *
 * @param contentFrame Fragment를 추가할 영역
 * @param addFragment  추가 할 Fragment
 * @param manager      Fragment 관리자
 */
fun Fragment.addFragment(contentFrame: Int, addFragment: Fragment) {
    val fragment: Fragment? = childFragmentManager.findFragmentById(contentFrame)
    if (fragment == null) {
        childFragmentManager.beginTransaction()
                .add(contentFrame, addFragment)
                .commit()
    } else {
        childFragmentManager.beginTransaction()
                .replace(contentFrame, addFragment)
                .commit()
    }
}

/**
 * Fragment 추가
 *
 * @param contentFrame Fragment를 추가할 영역
 * @param addFragment  추가 할 Fragment
 * @param manager      Fragment 관리자
 */
fun Fragment.addBackStackFragment(contentFrame: Int, addFragment: Fragment) {
    val fragment: Fragment? = childFragmentManager.findFragmentById(contentFrame)
    if (fragment == null) {
        childFragmentManager.beginTransaction()
                .add(contentFrame, addFragment)
                .addToBackStack(null)
                .commit()
    } else {
        childFragmentManager.beginTransaction()
                .replace(contentFrame, addFragment)
                .addToBackStack(null)
                .commit()
    }
}

/**
 * Fragment 가져오기
 *
 * @param contentFrame Fragment를 확인 할 영역
 * @param manager      Fragment 관리자
 * @return Fragment
 */
fun Fragment.getFragment(contentFrame: Int): Fragment? {
    return childFragmentManager.findFragmentById(contentFrame)
}
