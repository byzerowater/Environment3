package com.zerowater.environment.data.source.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zerowater.environment.Event
import com.zerowater.environment.data.Result
import com.zerowater.environment.data.source.LiveDataSource

/**
 * IntentLiveDataSource
 * Class: ResourceDataSource
 * Created by ZERO on 2020-05-27.
 * zero company Ltd
 * byzerowater@gmail.com
 * Description:
 */

class IntentLiveDataSource internal constructor(
        private val mutableLiveData: MutableLiveData<Event<Result<String>>>
) : LiveDataSource {

    override fun getIntentLiveData(): LiveData<Event<Result<String>>> {
        return mutableLiveData
    }

    override fun postIntentLiveData(data: Result<String>) {
        mutableLiveData.postValue(Event(data))
    }
}