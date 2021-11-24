package com.lionparcel.trucking.view.common

open class BaseResource<T, S>(open val status: S) {

    var item: T? = null

    var throwable: Throwable? = null

    constructor(status: S, item: T) : this(status) {
        this.item = item
    }

    constructor(status: S, throwable: Throwable) : this(status) {
        this.throwable = throwable
    }
}

class Resource<T>(override val status: Status) : BaseResource<T, Status>(status) {

    constructor(status: Status, item: T) : this(status) {
        this.item = item
    }

    constructor(status: Status, throwable: Throwable) : this(status) {
        this.throwable = throwable
    }
}
