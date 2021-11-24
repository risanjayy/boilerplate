package com.lionparcel.trucking.data.common.entity

interface ErrorResponse {
    fun getId(): Long? = null
    fun getErrorMessage(): String?
}
