package com.lionparcel.trucking.view.main

import androidx.lifecycle.ViewModelProvider
import com.lionparcel.trucking.databinding.ActivityMainBinding
import com.lionparcel.trucking.view.common.base.BaseSimpleActivity

class MainActivity : BaseSimpleActivity<MainViewModel, ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun buildViewModel(): MainViewModel {
        return ViewModelProvider(this, MainViewModelFactory()).get(MainViewModel::class.java)
    }
}
