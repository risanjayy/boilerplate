package com.lionparcel.trucking.domain.common.exceptions

import com.lionparcel.trucking.data.common.entity.ErrorResponse

open class LionParcelHttpException(private val response: ErrorResponse, message: String? = null) :
    Exception(message) {

    override val message: String?
        get() = response.getErrorMessage()

    class BadRequestHttpException(response: ErrorResponse) :
        LionParcelHttpException(response, "Bad Request")

    class UnauthorizedHttpException(response: ErrorResponse) :
        LionParcelHttpException(response, "Unauthorized")

    class ForbiddenHttpException(response: ErrorResponse) :
        LionParcelHttpException(response, "Forbidden")

    class UnprocessableEntityHttpException(response: ErrorResponse) :
        LionParcelHttpException(response, "Unprocessable Entity")

    class InternalServerErrorHttpException(response: ErrorResponse) :
        LionParcelHttpException(response, "Internal Server Error")

    class NotFoundErrorHttpException(response: ErrorResponse) :
        LionParcelHttpException(response, "Not Found")
}
