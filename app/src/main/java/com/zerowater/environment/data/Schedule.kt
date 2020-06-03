package com.zerowater.environment.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Environment
 * Class: Schedule
 * Created by ZERO on 2020-05-27.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description:
 */

@Parcelize
data class Schedule(
        val idx: Int = 0,
        val scheduleIn: Boolean = true
) : Parcelable