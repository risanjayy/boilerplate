package com.lionparcel.trucking.domain.common

import com.google.gson.Gson
import com.lionparcel.trucking.domain.common.exceptions.LionParcelHttpException
import com.lionparcel.trucking.data.common.entity.AlgoErrorResponse
import com.lionparcel.trucking.data.common.entity.ErrorResponse
import com.lionparcel.trucking.data.common.entity.FallbackErrorResponse
import com.lionparcel.trucking.view.common.exceptions.BadRequestAlgoErrorException
import retrofit2.HttpException
import javax.inject.Inject

class ErrorHelper @Inject constructor(private val gson: Gson) {

    fun buildThrowable(throwable: Throwable): Throwable {
        return if (throwable is HttpException) {
            val body = throwable.response()?.errorBody()?.string()
            val errorResponse =
                body?.let(this::buildErrorResponse) ?: FallbackErrorResponse("Null Body")
            when (errorResponse.getId()) {
                AlgoErrorId.BAD_REQUEST_ALGO_ERROR -> BadRequestAlgoErrorException(errorResponse)
                else -> when (throwable.code()) {
                    HttpStatusCodes.BAD_REQUEST ->
                        LionParcelHttpException.BadRequestHttpException(errorResponse)
                    HttpStatusCodes.UNAUTHORIZED ->
                        LionParcelHttpException.UnauthorizedHttpException(errorResponse)
                    HttpStatusCodes.FORBIDDEN ->
                        LionParcelHttpException.ForbiddenHttpException(errorResponse)
                    HttpStatusCodes.UNPROCESSABLE_ENTITY ->
                        LionParcelHttpException.UnprocessableEntityHttpException(errorResponse)
                    HttpStatusCodes.INTERNAL_SERVER_ERROR ->
                        LionParcelHttpException.InternalServerErrorHttpException(errorResponse)
                    HttpStatusCodes.NOT_FOUND ->
                        LionParcelHttpException.NotFoundErrorHttpException(errorResponse)
                    else -> LionParcelHttpException(errorResponse)
                }
            }
        } else throwable
    }

    private fun buildErrorResponse(body: String): ErrorResponse {
        return try {
            gson.fromJson(body, AlgoErrorResponse::class.java)
        } catch (e: Throwable) {
            FallbackErrorResponse("Unknown Error")
        }
    }
}
