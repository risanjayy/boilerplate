package com.lionparcel.trucking.data.common.exceptions

class MissingFieldsException(missingFields: List<String>) :
    Exception("Some fields are missing: $missingFields")
