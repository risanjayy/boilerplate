package com.lionparcel.trucking.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.lionparcel.trucking.databinding.SplashActivityBinding
import com.lionparcel.trucking.view.common.base.BaseActivity
import com.lionparcel.trucking.view.login.LoginActivity
import com.lionparcel.trucking.view.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<SplashActivityBinding>() {

    override fun getViewBinding(): SplashActivityBinding {
        return SplashActivityBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
        }, 500)
    }
}
