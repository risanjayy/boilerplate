package com.lionparcel.trucking.view.common.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.view.WindowInsetsController
import androidx.annotation.CallSuper
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.lionparcel.trucking.view.common.LocaleHelper
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity<V : ViewBinding> : AppCompatActivity() {

    companion object {
        private const val DEFAULT_LANGUAGE = "en"
    }

    lateinit var binding: V

    private val compositeDisposable = CompositeDisposable()

    protected val navController by lazy { buildNavController() }

    protected open fun buildNavController(): NavController {
        return NavController(this)
    }

    override fun attachBaseContext(base: Context?) {
        val newBaseContext =
            base?.let { LocaleHelper.onAttach(it, DEFAULT_LANGUAGE) }
        super.attachBaseContext(newBaseContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        handleNotification()
        setContentView(binding.root)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setSupportActionBar(getActionToolbar())
        supportActionBar?.let(this::configureActionBar)
        getNavHostFragment()?.let { navHostFragment ->
            val inflater = navHostFragment.navController.navInflater
            val graph = inflater.inflate(getNavGraphResource())
            configureNavGraph(graph)
            navHostFragment.navController.graph = graph
        }
        initViews()
    }

    abstract fun getViewBinding(): V

    @CallSuper
    protected open fun initViews() = Unit

    protected open fun handleNotification() = Unit

    protected open fun getActionToolbar(): Toolbar? = null

    protected open fun configureActionBar(actionBar: ActionBar) = Unit

    protected open fun getNavHostFragment(): NavHostFragment? = null

    protected open fun getNavGraphResource(): Int = -1

    protected open fun configureNavGraph(navGraph: NavGraph) = Unit

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    @Suppress("DEPRECATION")
    fun setSystemUiLightStatusBar(isLightStatusBar: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val systemUiAppearance = if (isLightStatusBar) {
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            } else {
                0
            }
            window.insetsController?.setSystemBarsAppearance(
                systemUiAppearance,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val systemUiVisibilityFlags = if (isLightStatusBar) {
                window.decorView.systemUiVisibility or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                window.decorView.systemUiVisibility and SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            window.decorView.systemUiVisibility = systemUiVisibilityFlags
        }
    }
}
