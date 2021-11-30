package com.lionparcel.trucking.view.common.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding2.view.RxView
import com.lionparcel.commonandroid.snackbartoast.ToastType
import com.lionparcel.commonandroid.snackbartoast.showToastBasicNoClose
import com.lionparcel.trucking.R
import com.lionparcel.trucking.data.common.exceptions.NoNetworkException
import com.lionparcel.trucking.databinding.ErrorPageLayoutBinding
import com.lionparcel.trucking.view.common.EditableRxTextView
import com.lionparcel.trucking.view.common.PermissionHelper
import com.lionparcel.trucking.view.common.extensions.hideKeyboard
import com.lionparcel.trucking.view.common.extensions.safeSubscribe
import com.lionparcel.trucking.view.common.module.DaggerFragmentComponent
import com.lionparcel.trucking.view.common.module.FragmentComponent
import com.lionparcel.trucking.view.common.module.FragmentModule
import com.lionparcel.trucking.view.common.validation.Validation
import com.lionparcel.trucking.view.common.validation.ValidationConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.lang.ref.WeakReference
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit


abstract class BaseFragment : Fragment() {

    companion object {
        internal const val DIALOG_LOADING = "DIALOG_LOADING"
        private const val PERMISSION_THROTTLE_TIME_MILLIS = 600L
        private const val PERMISSION_ACTION_STATE_RATIONALE = 1
        private const val PERMISSION_ACTION_STATE_DONE = 2
        private const val PERMISSION_ACTION_STATE_REJECTED = 3
    }

    protected var forceReload: Boolean = false

    private val compositeDisposable = CompositeDisposable()

    private val dialogFragmentMap = mutableMapOf<String, WeakReference<DialogFragment>>()

    val permissionHelper by lazy { PermissionHelper(this) }

    private var resumePermissionPubSubDisposable: Disposable? = null

    private val resumePermissionPubSub = PublishSubject.create<Pair<Int, Any>>()

    private var resumePermissionComplete = false

    protected var parentView: View? = null

    private val parentViewHeight by lazy { getWindowsHeight() }

    private var isKeyboardShown = false

    private val triggerOnTrackScreenOpenedOnce by lazy {
        onTrackScreenOpened()
    }

    private val keyboardLayoutListener: ViewTreeObserver.OnGlobalLayoutListener by lazy {
        ViewTreeObserver.OnGlobalLayoutListener {
            val keyBoardShown = parentViewHeight > getWindowsHeight()
            if ((keyBoardShown && !isKeyboardShown) || (!keyBoardShown && isKeyboardShown)) {
                isKeyboardShown = keyBoardShown
                onKeyboardVisibilityChange(keyBoardShown)
            }
        }
    }

    private fun getWindowsHeight() = parentView?.height ?: 0

    protected val component: FragmentComponent by lazy {
        DaggerFragmentComponent.builder().fragmentModule(
            FragmentModule(this)
        ).build()
    }

    protected abstract fun getContentResource(): Int

    protected open fun onTrackScreenOpened() = Unit

    protected open fun onBackPressed() {
        try {
            if (!findNavController().navigateUp()) {
                requireActivity().finish()
            }
        } catch (e: IllegalStateException) {
            Log.d(BaseFragment::class.java.simpleName, e.message ?: String())
            requireActivity().finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parentView = inflater.inflate(getContentResource(), container, false)
        return parentView
    }

    /**
     * function to configure individual toolbar in a fragment.
     * If all the fragments in one navigation group have the same configuration,
     * better set up the toolbar in the parent activity by overriding
     * [BaseActivity.configureActionBar] and [BaseActivity.getActionToolbar] methods
     *
     * @param toolbar the toolbar in the fragment xml
     * @param hasMenu determine if the toolbar has menus
     * @param showTitle determine if the toolbar should show screen title
     * @param showBackButton determine if the toolbar should show back arrow button
     */
    protected fun initToolbar(
        toolbar: Toolbar,
        hasMenu: Boolean = true,
        showTitle: Boolean = true,
        showBackButton: Boolean = true
    ) {
        setHasOptionsMenu(hasMenu)
        val navHostFragment = findNavController()
        NavigationUI.setupWithNavController(toolbar, navHostFragment)
        with(activity as BaseActivity<*>) {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setHomeButtonEnabled(showBackButton)
                setDisplayHomeAsUpEnabled(showBackButton)
                setDisplayShowTitleEnabled(showTitle)
            }
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (resumePermissionPubSubDisposable == null) {
            resumePermissionPubSubDisposable = resumePermissionPubSub.throttleFirst(
                PERMISSION_THROTTLE_TIME_MILLIS,
                TimeUnit.MILLISECONDS
            ).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    handlePermissionCases(it)
                }, {
                    showResumePermissionDialogIfNecessary()
                })
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            }
        )
        initViews()
    }

    override fun onResume() {
        super.onResume()
        showResumePermissionDialogIfNecessary()
    }

    override fun onStart() {
        super.onStart()
        triggerOnTrackScreenOpenedOnce
    }

    private fun showResumePermissionDialogIfNecessary() {
        if (resumePermissionComplete) {
            resumePermissionPubSub.onNext(PERMISSION_ACTION_STATE_DONE to false)
        } else {
            permissionHelper.executeWithAllPermissions(
                permissions = buildPagePermissions().toTypedArray(),
                executable = {
                    // All permissions are granted
                    resumePermissionPubSub.onNext(PERMISSION_ACTION_STATE_DONE to it)
                },
                rationaleExecutable = {
                    // A permission is rejected (this block might be called multiple times
                    // depending on the permissions count)
                    if (isHandlingRationale())
                        resumePermissionPubSub.onNext(PERMISSION_ACTION_STATE_RATIONALE to it)
                },
                denyAction = {
                    // All permissions are responded by user, but some of them are rejected
                    // with "don't ask again" checkbox checked
                    resumePermissionPubSub.onNext(PERMISSION_ACTION_STATE_REJECTED to it)
                }
            )
        }
    }

    private fun handlePermissionCases(case: Pair<Int, Any>) {
        when (case.first) {
            PERMISSION_ACTION_STATE_DONE -> {
                resumeWithPermissions()
                resumePermissionComplete = true
            }
            PERMISSION_ACTION_STATE_RATIONALE -> resumeWithRationaleForPermissions(case.second as String)
            PERMISSION_ACTION_STATE_REJECTED -> {
                @Suppress("UNCHECKED_CAST")
                resumeWithoutPermissions(case.second as List<String>)
                resumePermissionComplete = true
            }
        }
    }

    protected open fun buildPagePermissions(): List<String> = emptyList()

    protected open fun resumeWithPermissions() = Unit

    /*
    * A flag to decide whether to handle resume permission rationale or not. Set this flag to false
    * means the permission dialog will show continuously until the user choose to accept permissions
    * or deny with "don't ask again" checkbox checked
    * */
    protected open fun isHandlingRationale() = false

    protected open fun resumeWithRationaleForPermissions(permission: String) = Unit

    protected open fun resumeWithoutPermissions(deniedPermissions: List<String>) = Unit

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) = permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)

    @CallSuper
    protected open fun initViews() {
        (view as? ViewGroup)?.let(this::makeFocusable)
        parentView?.viewTreeObserver?.addOnGlobalLayoutListener(keyboardLayoutListener)
    }

    private fun makeFocusable(viewGroup: ViewGroup) {
        viewGroup.children.filter { it is ViewGroup }.map { it as ViewGroup }
            .forEach(this::makeFocusable)
        if (viewGroup.tag == getString(R.string.non_translatable_tag_focus_holder)) {
            RxView.focusChanges(viewGroup).safeSubscribe {
                if (it) hideKeyboard()
            }.collect()
        }
    }

    //TODO: Refactor all focus clearance with this method
    protected fun resetFocus() {
        (view as? ViewGroup)?.findViewWithTag<View>(getString(R.string.non_translatable_tag_focus_holder))
            ?.requestFocus()
    }

    protected fun onSingleEventValidation(validation: Validation<*>, isRealtime: Boolean? = false) {
        val textInputLayout =
            validation.data?.get(ValidationConstants.ERROR_INPUT_LAYOUT) as? TextInputLayout?
        val isCounterEnable = textInputLayout?.isCounterEnabled
        val defaultMessage = validation.data?.get(ValidationConstants.DEFAULT_MESSAGE) as? String?
        val validMessage = validation.data?.get(ValidationConstants.VALID_MESSAGE) as? String?
        val invalidMessage = validation.data?.get(ValidationConstants.INVALID_MESSAGE) as? String?
        val underlineColor = ContextCompat.getColorStateList(requireContext(), R.color.shades2)
        textInputLayout?.helperText = defaultMessage
        if (validation.isViewValid()) {
            textInputLayout?.helperText = validMessage
            val color = ContextCompat.getColorStateList(requireContext(), R.color.shades3)
            textInputLayout?.isCounterEnabled = isCounterEnable ?: false
            textInputLayout?.setHelperTextColor(color)
            textInputLayout?.editText?.backgroundTintList = underlineColor
        } else {
            textInputLayout?.let {
                it.helperText = invalidMessage
                val color = ContextCompat.getColorStateList(requireContext(), R.color.interpack6)
                it.setHelperTextColor(color)
                it.editText?.backgroundTintList = color
                isRealtime?.let { _ ->
                    if (!isRealtime) {
                        it.editText?.let(EditableRxTextView::textChanges)
                            ?.skipInitialValue()?.take(1)
                            ?.safeSubscribe {
                                val helperTextColor =
                                    ContextCompat.getColorStateList(
                                        requireContext(),
                                        R.color.shades4
                                    )
                                textInputLayout.setHelperTextColor(helperTextColor)
                                textInputLayout.helperText = defaultMessage
                                textInputLayout.editText?.backgroundTintList = underlineColor
                                textInputLayout.isCounterEnabled = isCounterEnable ?: false
                            }?.collect()
                    }
                }
            } ?: run {
                (view as? ViewGroup)?.let {
                    requireContext().showToastBasicNoClose(
                        it,
                        invalidMessage ?: getString(R.string.general_error_message),
                        ToastType.ERROR
                    )
                }
            }
        }
        isRealtime?.let {
            if (!it) textInputLayout?.editText?.clearFocus()
        }
    }

    protected open fun handleGenericError(throwable: Throwable, isNewDesign: Boolean = false) {
        val pageErrorView: View? = requireView().rootView.findViewById(R.id.clErrorPageContainer)
        val errorMessage = when (throwable) {
            is NoNetworkException,
            is UnknownHostException,
            is ConnectException -> {
                if (forceReload) return
                if (!isThisTopFragment()) {
                    forceReload = true
                    return
                }
                if (pageErrorView == null) {
                    injectErrorViewToScreen(
                        requireView().rootView as ViewGroup,
                        getString(R.string.general_no_internet_error_title),
                        getString(R.string.general_no_internet_error_message),
                        R.drawable.ic_no_connection
                    )
                } else {
                    configureErrorView(
                        pageErrorView,
                        getString(R.string.general_no_internet_error_title),
                        getString(R.string.general_no_internet_error_message),
                        R.drawable.ic_no_connection
                    )
                }
                null
            }
            else -> throwable.localizedMessage.takeIf { !it.isNullOrBlank() }
                ?: getString(R.string.general_error_message)
        }
        errorMessage?.let {
            if (isNewDesign) {
                if (pageErrorView == null) {
                    injectErrorViewToScreen(
                        requireView().rootView as ViewGroup,
                        getString(R.string.general_error_message),
                        getString(R.string.general_error_server_message),
                        R.drawable.ic_something_went_wrong
                    )
                } else {
                    configureErrorView(
                        pageErrorView,
                        getString(R.string.general_error_message),
                        getString(R.string.general_error_server_message),
                        R.drawable.ic_something_went_wrong
                    )
                }
            } else {
                (view as? ViewGroup)?.let {
                    requireContext().showToastBasicNoClose(it, errorMessage, ToastType.ERROR)
                }
            }
        }
    }

    private fun isThisTopFragment(): Boolean {
        val currentFragment: BaseFragment = this
        val parent = currentFragment.parentFragment
        return (parent == null || parent is NavHostFragment)
    }

    private fun injectErrorViewToScreen(
        baseView: ViewGroup,
        errorTitle: String,
        errorMessage: String,
        imageDrawable: Int
    ): View {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.error_page_layout, baseView, false).apply {
                configureErrorView(
                    this,
                    errorTitle,
                    errorMessage,
                    imageDrawable
                )
                baseView.addView(this)
            }
    }

    private fun configureErrorView(
        view: View,
        errorTitle: String,
        errorMessage: String,
        imageDrawable: Int
    ) {
        val binder = ErrorPageLayoutBinding.bind(view)
        with(binder) {
            root.visibility = View.VISIBLE
            root.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.white, null))
            root.bringToFront()
            btnReload.isVisible = true
            btnReload.setOnClickListener {
                root.isVisible = false
                reloadViewAndViewModel()
            }
            txtErrorPageMessageTitle.isVisible = true
            txtErrorPageMessageTitle.text = errorTitle
            txtErrorPageMessage.text = errorMessage
            context?.let(com.bumptech.glide.Glide::with)?.load(imageDrawable)
                ?.into(imgErrorPageIcon)
        }
    }

    protected open fun reloadViewAndViewModel() {
        forceReload = true
        parentFragmentManager.beginTransaction()
            .detach(this@BaseFragment)
            .attach(this@BaseFragment).commit()
    }

    protected fun showCustomDialog(key: String, dialogFragment: DialogFragment) =
        showCustomDialog(childFragmentManager, key, dialogFragment)

    protected fun showCustomDialog(
        fragmentManager: FragmentManager,
        key: String,
        dialogFragment: DialogFragment
    ) {
        fragmentManager.let { manager ->
            dialogFragmentMap[key]?.get()?.dismissAllowingStateLoss()
            dialogFragmentMap[key] = WeakReference(dialogFragment)
            dialogFragmentMap[key]?.get()?.show(manager, key)
        }
    }

    @Suppress("SameParameterValue")
    protected fun showingCustomDialog(key: String): Boolean {
        return dialogFragmentMap[key]?.get()?.isVisible == true
    }

    override fun onDestroyView() {
        dialogFragmentMap.clear()
        super.onDestroyView()
    }

    protected fun isCustomDialogShowingByTag(key: String): Boolean {
        return isCustomDialogShowingByTag(key, activity?.supportFragmentManager)
                || isCustomDialogShowingByTag(key, childFragmentManager)
    }

    private fun isCustomDialogShowingByTag(
        key: String,
        fragmentManager: FragmentManager?
    ): Boolean {
        val dialogFragment = fragmentManager?.findFragmentByTag(key) as? DialogFragment
        return dialogFragment?.dialog?.isShowing ?: false
    }

    protected fun hideCustomDialog(key: String) {
        dialogFragmentMap[key]?.get()?.dismissAllowingStateLoss()
        dialogFragmentMap.remove(key)
    }

    protected fun hideLoadingDialog() = hideCustomDialog(DIALOG_LOADING)

    fun Disposable.collect() = compositeDisposable.add(this)

    protected fun cleanDisposable() {
        compositeDisposable.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        cleanDisposable()
        resumePermissionPubSubDisposable?.dispose()
        parentView?.viewTreeObserver?.removeOnGlobalLayoutListener(keyboardLayoutListener)
    }

    protected open fun onKeyboardVisibilityChange(isShown: Boolean) {}
}
