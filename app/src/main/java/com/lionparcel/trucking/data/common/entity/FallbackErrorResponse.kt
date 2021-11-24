package com.lionparcel.trucking.data.common.entity

import com.google.gson.annotations.SerializedName

class FallbackErrorResponse(
    @SerializedName(SERIALIZED_NAME_MESSAGE) val message: String
) : ErrorResponse {

    override fun getErrorMessage(): String = message

    companion object {
        private const val SERIALIZED_NAME_MESSAGE = "message"
    }

}
