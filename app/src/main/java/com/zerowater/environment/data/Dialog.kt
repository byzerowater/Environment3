package com.zerowater.environment.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Environment
 * Class: Dialog
 * Created by ZERO on 2020-05-24.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description:
 */
@Parcelize
data class Dialog(val message: String, val left: String, val right: String?) : Parcelable