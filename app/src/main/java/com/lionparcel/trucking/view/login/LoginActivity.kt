package com.lionparcel.trucking.view.login

import com.lionparcel.trucking.databinding.ActivityLoginBinding
import com.lionparcel.trucking.view.common.base.BaseActivity

class LoginActivity: BaseActivity<ActivityLoginBinding>() {

    override fun getViewBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }
}
