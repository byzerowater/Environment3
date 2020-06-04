package com.zerowater.environment.data.source.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zerowater.environment.Event
import com.zerowater.environment.data.IntentExtra
import com.zerowater.environment.data.Result
import com.zerowater.environment.data.source.LiveDataSource

/**
 * IntentLiveDataSource
 * Class: IntentLiveDataSource
 * Created by ZERO on 2020-05-27.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description:
 */

class IntentLiveDataSource internal constructor(
        private val mutableLiveData: MutableLiveData<Event<Result<IntentExtra>>>
) : LiveDataSource {

    override fun getIntentLiveData(): LiveData<Event<Result<IntentExtra>>> {
        return mutableLiveData
    }

    override fun postIntentLiveData(data: Result<IntentExtra>) {
        mutableLiveData.postValue(Event(data))
    }
}