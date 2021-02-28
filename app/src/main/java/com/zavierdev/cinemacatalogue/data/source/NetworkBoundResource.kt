package com.zavierdev.cinemacatalogue.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.zavierdev.cinemacatalogue.data.source.remote.StatusResponse
import com.zavierdev.cinemacatalogue.data.source.remote.ApiResponse
import com.zavierdev.cinemacatalogue.utils.AppExecutors
import com.zavierdev.cinemacatalogue.vo.Resource

abstract class NetworkBoundResource<ResultType, RequestType>(private val mAppExecutors: AppExecutors) {
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        fetchFromNetwork()
    }

    protected open fun onFetchFailed() {}

    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    protected abstract fun provideResult(data: RequestType): LiveData<ResultType>

    private fun fetchFromNetwork() {
        val apiResponse = createCall()

        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)

            when (response.status) {
                StatusResponse.SUCCESS -> {
                    val resultData = provideResult(response.body)
                    result.addSource(resultData) { newData ->
                        result.removeSource(resultData)
                        result.value = Resource.success(newData)
                    }
                }

                StatusResponse.EMPTY -> {
                    val resultData = provideResult(response.body)
                    result.addSource(resultData) { newData ->
                        result.removeSource(resultData)
                        result.value = Resource.success(newData)
                    }
                }

                StatusResponse.ERROR -> {
                    onFetchFailed()
                    val resultData = provideResult(response.body)
                    result.addSource(resultData) { newData ->
                        result.removeSource(resultData)
                        result.value = Resource.error(response.message, newData)
                    }
                }
            }
        }
    }

    fun asLiveData(): LiveData<Resource<ResultType>> = result
}