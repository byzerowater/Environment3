package com.zerowater.environment.ui.permission

import android.Manifest
import android.os.Build

/**
 * Environment
 * Class: PermissionConst
 * Created by ZERO on 2020-05-26.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description:
 */

const val REQUEST_PERMISSION_CODE = 0x15

val APP_PERMISSION =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        } else {
            arrayOf(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
        }