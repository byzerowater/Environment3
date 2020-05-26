package com.zerowater.environment.data.source.resource

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import com.zerowater.environment.data.source.ResourceDataSource
import com.zerowater.environment.ui.permission.APP_PERMISSION
import pub.devrel.easypermissions.EasyPermissions
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * Environment
 * Class: ResourceDataSource
 * Created by ZERO on 2020-05-26.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description:
 */

class ContextDataSource internal constructor(
        private val context: Context
) : ResourceDataSource {
    override fun getNetworkOperatorName(): String? {
        return (context.getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager).networkOperatorName
    }

    override fun getUDID(): String? {
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        return if ("9774d56d682e549c" != androidId) {
            UUID.nameUUIDFromBytes(androidId.toByteArray(StandardCharsets.UTF_8)).toString()
        } else {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) !== PackageManager.PERMISSION_GRANTED) {
                UUID.randomUUID().toString()
            } else {
                val deviceId = (context.getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager).deviceId
                if (deviceId != null) UUID.nameUUIDFromBytes(deviceId.toByteArray(StandardCharsets.UTF_8)).toString() else UUID.randomUUID().toString()
            }
        }
    }

    /**
     * 배터리 화이트 리스트 등록 여부
     *
     * @param context Context
     * @return 배터리 화이트 리스트 등록 여부
     */
    override fun isIgnoringBatteryOptimizations(): Boolean {
        var isIgnoringBatteryOptimizations = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            isIgnoringBatteryOptimizations = powerManager.isIgnoringBatteryOptimizations(context.packageName)
        }
        return isIgnoringBatteryOptimizations
    }

    override fun checkSelfPermission(): Boolean {
        return EasyPermissions.hasPermissions(context, *APP_PERMISSION)
    }
}
