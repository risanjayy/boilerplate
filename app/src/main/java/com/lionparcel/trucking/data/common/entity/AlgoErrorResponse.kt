package com.lionparcel.trucking.data.common.entity

import com.google.gson.annotations.SerializedName

class AlgoErrorResponse(
    @SerializedName(SERIALIZED_NAME_ERROR_ID) val id: Int?,
    @SerializedName(SERIALIZED_NAME_MESSAGE) val message: ErrorMessageResponse?
) : ErrorResponse {

    override fun getId(): Long? = id?.toLong()

    override fun getErrorMessage(): String? = message?.indonesianMessage

    class ErrorMessageResponse(
        @SerializedName(SERIALIZED_NAME_ID_MESSAGE) val indonesianMessage: String?
    )

    companion object {
        private const val SERIALIZED_NAME_ERROR_ID = "error_id"
        private const val SERIALIZED_NAME_MESSAGE = "message"
        private const val SERIALIZED_NAME_ID_MESSAGE = "id"
    }
}
