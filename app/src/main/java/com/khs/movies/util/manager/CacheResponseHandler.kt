package com.khs.movies.util.manager

import androidx.annotation.StringRes
import com.khs.movies.R
import com.khs.movies.model.common.*


abstract class CacheResponseHandler<ViewState, Data>(
    private val response: CacheResult<Data?>,
    private val stateEvent: StateEvent?,
    private val errorUIComponentType: UIComponentType = UIComponentType.Dialog(),
    private val emptyUIComponentType: UIComponentType = errorUIComponentType,
    @StringRes private val emptyResultText: Int = R.string.error_empty_result,
    private val messageType: MessageType = MessageType.Error
) {
    suspend fun getResult(): DataState<ViewState> {

        return when (response) {

            is CacheResult.GenericError -> {
                DataState.error(
                    response = Response(
                        message = response.errorMessage,
                        uiComponentType = errorUIComponentType,
                        messageType = messageType
                    ),
                    stateEvent = stateEvent
                )
            }

            is CacheResult.Success -> {
                if (response.value == null) {
                    DataState.error(
                        response = Response(
                            message = emptyResultText,
                            uiComponentType = emptyUIComponentType,
                            messageType = messageType
                        ),
                        stateEvent = stateEvent
                    )
                } else {
                    handleSuccess(resultObj = response.value)
                }
            }
        }
    }

    protected abstract suspend fun handleSuccess(resultObj: Data): DataState<ViewState>
}