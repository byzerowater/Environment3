package com.zerowater.environment.data


/**
 * Environment
 * Class: MoreModule
 * Created by ZERO on 2020-06-04.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description:
 */
data class IntentExtra(val type: ExtraType, val data: String)

enum class ExtraType {
    FCM
}