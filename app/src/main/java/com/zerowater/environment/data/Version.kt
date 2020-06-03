package com.zerowater.environment.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Version(
        val isForceUpdate: Boolean? = null,
        val message: String? = null,
        val osType: String? = null,
        val packageId: String? = null,
        val storeUrl: String? = null,
        val version: String? = null
) : Parcelable