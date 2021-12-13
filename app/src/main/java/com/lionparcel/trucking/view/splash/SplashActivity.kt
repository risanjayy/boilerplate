package com.lionparcel.trucking.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.lionparcel.trucking.databinding.SplashActivityBinding
import com.lionparcel.trucking.view.common.base.BaseActivity
import com.lionparcel.trucking.view.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<SplashActivityBinding>() {

    override fun getViewBinding(): SplashActivityBinding {
        return SplashActivityBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            )
        }, 500)
    }
}
