package com.khs.movies.util.manager


import androidx.annotation.StringRes
import com.khs.movies.model.common.*
import com.khs.movies.util.ext.safeApiCall
import com.khs.movies.util.ext.safeCacheCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class NetworkBoundResource<NetworkObj, CacheObj, ViewState>
constructor(
    private val dispatcher: CoroutineDispatcher,
    private val stateEvent: StateEvent?,
    private val api: Pair<@ApiMethod Int, suspend () -> NetworkObj?>,
    protected val cacheCall: suspend () -> CacheObj?,
    private val apiErrorUIComponentType: UIComponentType = UIComponentType.Dialog(),
    private val cacheErrorUIComponentType: UIComponentType = UIComponentType.Dialog()
) {
    val result: Flow<DataState<ViewState>> = flow {
//        delay(1000)
        if (shouldCallNetwork()) {
            val apiResult = safeApiCall(
                apiMethod = api.first,
                dispatcher = dispatcher
            ) {
                api.second()
            }
            emit(
                object : ApiResponseHandler<ViewState, NetworkObj>(
                    response = apiResult,
                    stateEvent = stateEvent,
                    errorUIComponentType = apiErrorUIComponentType
                ) {
                    override suspend fun handleSuccess(resultObj: NetworkObj): DataState<ViewState> =
                        handleNetworkSuccess(resultObj)

                    override suspend fun handleError(
                        errorCode: Int?,
                        errorMessage: Int
                    ): DataState<ViewState> {
                        return handleNetworkError(errorCode, errorMessage)
                    }
                }.getResult()
            )
        } else {
            val cacheResult =
                safeCacheCall(dispatcher) {
                    cacheCall()
                }
            emit(
                object : CacheResponseHandler<ViewState, CacheObj>(
                    response = cacheResult,
                    stateEvent = stateEvent,
                    errorUIComponentType = cacheErrorUIComponentType
                ) {
                    override suspend fun handleSuccess(resultObj: CacheObj): DataState<ViewState> =
                        handleCacheSuccess(resultObj)
                }.getResult()
            )
        }
    }

    protected abstract fun shouldCallNetwork(): Boolean

    protected abstract suspend fun handleCacheSuccess(resultObj: CacheObj): DataState<ViewState>

    protected abstract suspend fun handleNetworkSuccess(resultObj: NetworkObj): DataState<ViewState>

    protected open suspend fun handleNetworkError(
        errorCode: Int?,
        @StringRes errorMessage: Int
    ): DataState<ViewState> = DataState.error(
        response = Response(
            formatArgs = errorCode?.let {
                arrayOf(it)
            },
            message = errorMessage,
            uiComponentType = apiErrorUIComponentType,
            messageType = MessageType.Error
        ),
        stateEvent = stateEvent
    )

}