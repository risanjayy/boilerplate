package com.lionparcel.trucking.view.common.exceptions

import com.lionparcel.trucking.data.common.entity.ErrorResponse

class BadRequestAlgoErrorException(response: ErrorResponse? = null) :
    Exception(response?.getErrorMessage())
