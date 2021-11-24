package com.lionparcel.trucking.domain.common

object AlgoErrorId {
    const val UNBINDIND_DANA: Long = 301
    const val UNIQUE_ERROR: Long = 444
    const val BAD_REQUEST_ALGO_ERROR: Long = 400
    const val CHECK_TARIFF_MAX_WEIGHT: Long = 703
    const val CHECK_TARIFF_ORIGIN: Long = 701
    const val CHECK_TARIFF_DESTINATION: Long = 702
    const val CHECK_TARIFF_GENERAL_ERROR: Long = 704
    const val ALGO_ERROR_445: Long = 445
    const val PACKAGE_ALREADY_CLAIMED_ERROR: Long = 555
    /*
     * example use of error code 7:
     * Outstanding Payment Exist
     * Product Invalid
     * Search query less than minimum
     */
    const val SHOP_OR_SHIPMENT_INVALID_ERROR: Long = 7
    const val SHOP_BRAND_OR_MERCHANT_DEACTIVATED: Long = 8
    const val ALGO_ERROR_446: Long = 446
    const val ALGO_ERROR_440: Long = 440
    const val VALIDATION_ERROR: Long = 422
    const val LIMIT_EXCEEDED_ERROR: Long = 409
}
