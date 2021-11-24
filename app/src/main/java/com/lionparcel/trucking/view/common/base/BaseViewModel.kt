package com.lionparcel.trucking.view.common.base

import androidx.annotation.CallSuper
import androidx.annotation.RestrictTo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lionparcel.trucking.view.common.Resource
import com.lionparcel.trucking.view.common.Status
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlin.random.Random

abstract class BaseViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val pageLoadMutableEvent = MutableLiveData<Resource<Unit>>()

    internal val pageLoadEvent: LiveData<Resource<Unit>> get() = pageLoadMutableEvent

    private var pageLoadEventSingles = mutableMapOf<Int, Single<Any>>()

    private var firstLoad = true

    private var enablePageLoadEvent = true

    fun loadPage() = registerPageEventSources {
        if (firstLoad) initPage() else resume()
        refreshPage()
        if (enablePageLoadEvent) {
            runPage()
        }

    }

    open fun reloadPage() = registerPageEventSources {
        initPage()
        refreshPage()
        if (enablePageLoadEvent) {
            runPage()
        }
    }

    /*
    * A callback which is called when the page refreshes
    *
    * Invoked every time the page shows up
    * */
    @CallSuper
    protected open fun refreshPage() = Unit

    /*
    * A callback which is called when the page needs to load initial condition
    *
    * Invoked only the first time the page is shown and whenever the page is reloaded
    * */
    @CallSuper
    protected open fun initPage() = Unit

    /*
    * A callback which is called when the page resumes
    *
    * Invoked every time except the first time the page is shown
    * */
    @CallSuper
    protected open fun resume() = Unit

    protected fun registerPageEventSources(callback: () -> Unit) {
        pageLoadEventSingles.clear()
        callback()
    }

    protected fun runPage() {
        pageLoadMutableEvent.postValue(Resource(Status.LOADING))
        firstLoad = false
        if (pageLoadEventSingles.isNotEmpty()) {
            val combinedSingles =
                Observable.fromIterable(pageLoadEventSingles.entries).flatMapSingle { entry ->
                    entry.value.map { entry.key to it }
                        .onErrorReturn { entry.key to onMapPageErrorEvent(entry.key, it) }
                }.toList().map { it.toMap() }
                    .map { onMergePageLoadEvents(it) }
                    .doOnSuccess { map ->
                        map.values.find { it is Throwable }?.let { throw it as Throwable }
                    }

            combinedSingles.subscribe({
                pageLoadMutableEvent.postValue(Resource(Status.SUCCESS))
            }, {
                pageLoadMutableEvent.postValue(Resource(Status.ERROR, it))
            }).collect()
        } else {
            pageLoadMutableEvent.postValue(Resource(Status.SUCCESS))
        }
    }

    protected open fun onMergePageLoadEvents(events: Map<Int, Any>): Map<Int, Any> = events

    protected open fun onMapPageErrorEvent(event: Int, throwable: Throwable): Throwable = throwable

    protected open fun <T> Single<T>.asPageLoadEventSource(eventId: Int = Random.nextInt()): Single<T> {
        var emitter: SingleEmitter<Any>? = null
        val single = Single.create<Any> { emitter = it }
        pageLoadEventSingles[eventId] = single
        return this.doOnSuccess { emitter?.onSuccess(it as Any) }
            .doOnError { emitter?.tryOnError(it) }
    }

    protected fun Disposable.collect() = compositeDisposable.add(this)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun onPageLoading() {
        pageLoadMutableEvent.value = Resource(Status.LOADING)
    }

    fun onPageSuccess() {
        pageLoadMutableEvent.value = Resource(Status.SUCCESS)
    }

    fun onPageError() {
        pageLoadMutableEvent.value = Resource(Status.ERROR)
    }

    fun setPageLoadEventEnable(isEnable: Boolean) {
        enablePageLoadEvent = isEnable
    }

    fun clearCompositeDisposable() {
        compositeDisposable.clear()
    }
    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getCompositeDisposable() = compositeDisposable
}
