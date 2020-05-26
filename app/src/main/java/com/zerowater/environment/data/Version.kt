package com.zerowater.environment.data

data class Version(
        val appPackage: String,
        val isForceUpdate: Boolean,
        val osType: String,
        val storeUrl: String,
        val version: String
)