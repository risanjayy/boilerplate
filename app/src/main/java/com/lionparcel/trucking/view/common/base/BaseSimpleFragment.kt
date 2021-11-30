package com.lionparcel.trucking.view.common.base

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.google.firebase.messaging.RemoteMessage
import com.lionparcel.trucking.R
import com.lionparcel.trucking.data.common.exceptions.NoNetworkException
import com.lionparcel.trucking.databinding.ErrorPageLayoutBinding
import com.lionparcel.trucking.view.common.Resource
import com.lionparcel.trucking.view.common.Status
import com.lionparcel.trucking.view.common.notification.NotificationHelper
import com.lionparcel.trucking.view.common.notification.NotificationLiveData
import java.net.ConnectException
import java.net.UnknownHostException

abstract class BaseSimpleFragment<T : BaseViewModel, V: ViewBinding> : BaseFragment<V>() {

    protected val viewModel by lazy { buildViewModel() }

    protected open fun findPageLoadingView(): View? = null

    protected open fun findPageContentView(): View? = null

    protected open fun findPageErrorView(): ErrorPageLayoutBinding? = null

    protected abstract fun buildViewModel(): T

    protected val notificationHelper by lazy { NotificationHelper() }

    private val notificationLiveData by lazy { NotificationLiveData(requireContext()) }

    private val notificationLiveDataObserver by lazy {
        Observer<RemoteMessage> { if (userVisibleHint) handleNotification(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLiveDataObservers()
    }

    override fun resumeWithPermissions() {
        if (forceReload) {
            viewModel.reloadPage()
            forceReload = false
        } else {
            viewModel.loadPage()
        }
    }

    override fun initViews() {
        super.initViews()
        initPageLoadState()
    }

    private fun initPageLoadState() {
        findPageLoadingView()?.isVisible = false
        findPageContentView()?.isVisible = true
        findPageErrorView()?.root?.isVisible = false
    }

    @CallSuper
    protected open fun initLiveDataObservers() {
        viewModel.pageLoadEvent.observe(
            viewLifecycleOwner, { it?.let(this::handlePageLoadEvent) })
        notificationLiveData.observe(viewLifecycleOwner, notificationLiveDataObserver)
    }

    protected open fun handleNotification(remoteMessage: RemoteMessage) {
        notificationHelper.showNotification(requireContext(), remoteMessage)
    }

    @CallSuper
    protected open fun destroyLiveDataObservers() = Unit

    private fun handlePageLoadEvent(resource: Resource<Unit>) {
        when (resource.status) {
            Status.SUCCESS -> handlePageSuccess(findPageContentView())
            Status.LOADING -> handlePageLoading()
            Status.ERROR -> resource.throwable?.let { handlePageError(findPageErrorView()?.root, it) }
        }
    }

    @CallSuper
    protected open fun handlePageSuccess(pageContentView: View? = findPageContentView()) {
        findPageLoadingView()?.isVisible = false
        pageContentView?.isVisible = true
        findPageErrorView()?.root?.isVisible = false
    }

    protected open fun handlePageLoading() {
        findPageLoadingView()?.isVisible = true
        findPageContentView()?.isVisible = false
        findPageErrorView()?.root?.isVisible = false
    }

    @CallSuper
    protected open fun handlePageError(pageErrorView: View?, throwable: Throwable) {
        findPageLoadingView()?.isVisible = false
        findPageContentView()?.isVisible = false
        pageErrorView?.isVisible = true
    }

    protected open fun handleGenericPageError(
        pageErrorView: ErrorPageLayoutBinding?,
        throwable: Throwable,
        callBackRetry: (() -> Unit)? = null
    ) {
        when (throwable) {
            is UnknownHostException,
            is NoNetworkException,
            is ConnectException -> pageErrorView?.let { handleNoConnectionPageError(it, callBackRetry) }
            else -> pageErrorView?.let { handleUnknownPageError(it, callBackRetry) }
        }
    }

    override fun handleGenericError(throwable: Throwable, isNewDesign: Boolean) {
        val isPageErrorShow = findPageErrorView()?.root?.isVisible ?: false
        val isPageContentShow = findPageContentView()?.isVisible ?: true
        if (isPageContentShow && !isPageErrorShow) {
            super.handleGenericError(throwable, isNewDesign)
        }
    }

    open fun handleNoConnectionPageError(
        pageErrorView: ErrorPageLayoutBinding,
        callBackRetry: (() -> Unit)? = null
    ) {
        pageErrorView.btnReload.isVisible = true
        pageErrorView.btnReload.setOnClickListener {
            callBackRetry?.invoke() ?: viewModel.reloadPage()
        }
        pageErrorView.txtErrorPageMessageTitle.isVisible = true
        pageErrorView.txtErrorPageMessageTitle.text =
            getString(R.string.general_no_internet_error_title)
        pageErrorView.txtErrorPageMessage.text =
            getString(R.string.general_no_internet_error_message)
        context?.let(Glide::with)?.load(R.drawable.ic_no_connection)
            ?.into(pageErrorView.imgErrorPageIcon)
    }

    private fun handleUnknownPageError(
        pageErrorView: ErrorPageLayoutBinding,
        callBackRetry: (() -> Unit)?
    ) {
        pageErrorView.btnReload.isVisible = true
        pageErrorView.txtErrorPageMessageTitle.isVisible = true
        pageErrorView.txtErrorPageMessageTitle.text = getString(R.string.general_error_message)
        pageErrorView.txtErrorPageMessage.text = getString(R.string.general_error_server_message)
        context?.let(Glide::with)?.load(R.drawable.ic_something_went_wrong)
            ?.into(pageErrorView.imgErrorPageIcon)
        pageErrorView.btnReload.setOnClickListener {
            callBackRetry?.invoke() ?: viewModel.reloadPage()
        }
    }

    open fun handleRetryPage(throwable: Throwable, callback: () -> Unit) {
        findPageErrorView()?.root?.isVisible = true
        findPageContentView()?.isVisible = false
        findPageErrorView()?.let {
            with(it) {
                txtErrorPageMessageTitle.isVisible = true
                txtErrorPageMessageTitle.text =
                    getString(R.string.general_no_internet_error_title)
                txtErrorPageMessage.text = throwable.localizedMessage
                Glide.with(it.root).load(R.drawable.ic_no_connection).into(imgErrorPageIcon)
                btnReload.isVisible = true
                btnReload.setOnClickListener { callback() }
            }
        }
    }
}
