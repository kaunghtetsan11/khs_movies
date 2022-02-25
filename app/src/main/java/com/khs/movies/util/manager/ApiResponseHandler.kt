package com.khs.movies.util.manager

import androidx.annotation.StringRes
import com.khs.movies.R
import com.khs.movies.model.common.*

abstract class ApiResponseHandler<ViewState, Data>(
    private val response: ApiResult<Data?>,
    private val stateEvent: StateEvent?,
    private val errorUIComponentType: UIComponentType = UIComponentType.Dialog(),
    private val emptyUIComponentType: UIComponentType = errorUIComponentType,
    @StringRes private val emptyResultText: Int = R.string.error_empty_result
) {

    suspend fun getResult() = when (response) {

        is ApiResult.GenericError -> {
            handleError(
                errorCode = response.code,
                errorMessage = response.errorMessage
            )
        }

        is ApiResult.NetworkError -> {
            DataState.error(
                response = Response(
                    message = R.string.error_poor_connection,
                    uiComponentType = errorUIComponentType,
                    messageType = MessageType.Error
                ),
                stateEvent = stateEvent
            )
        }

        is ApiResult.Success -> {
            if (response.value == null) {
                DataState.error(
                    response = Response(
                        message = emptyResultText,
                        uiComponentType = emptyUIComponentType,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            } else {
                handleSuccess(resultObj = response.value)
            }
        }
    }

    protected abstract suspend fun handleSuccess(resultObj: Data): DataState<ViewState>

    protected open suspend fun handleError(
        errorCode: Int?,
        @StringRes errorMessage: Int
    ): DataState<ViewState> = DataState.error(
        response = Response(
            formatArgs = errorCode?.let {
                arrayOf(it)
            },
            message = errorMessage,
            uiComponentType = errorUIComponentType,
            messageType = MessageType.Error
        ),
        stateEvent = stateEvent
    )
}