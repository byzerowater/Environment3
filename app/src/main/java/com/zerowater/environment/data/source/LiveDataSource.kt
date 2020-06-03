package com.zerowater.environment.data.source

import androidx.lifecycle.LiveData
import com.zerowater.environment.Event
import com.zerowater.environment.data.Result


/**
 * Environment
 * Class: LiveDataSource
 * Created by ZERO on 2020-05-27.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description:
 */
interface LiveDataSource {
    fun getIntentLiveData(): LiveData<Event<Result<String>>>
    fun postIntentLiveData(data: Result<String>)
}