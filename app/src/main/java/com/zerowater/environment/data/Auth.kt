package com.zerowater.environment.data

import android.os.Build
import com.zerowater.environment.BuildConfig

/**
 * Environment
 * Class: Auth
 * Created by ZERO on 2020-05-25.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description:
 */

data class Token(
        val accessToken: String? = null,
        val accessTokenExpiredTime: Long? = null,
        val refreshToken: String? = null
)

data class Auth(
        val name: String,
        val phoneNo: String,
        val isUpdateDevice: Boolean = true,
        val userDevice: UserDevice
)

data class UserDevice(
        val currentOsVersion: String = Build.VERSION.RELEASE,
        val deviceId: String?,
        val model: String = Build.DEVICE,
        val osType: Int = 1,
        val packageId: String = BuildConfig.APPLICATION_ID,
        val pushId: String?,
        val telecom: String?
)