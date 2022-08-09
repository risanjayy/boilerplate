package com.lionparcel.trucking.view.login

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.lionparcel.trucking.databinding.LoginFragmentBinding
import com.lionparcel.trucking.view.common.Resource
import com.lionparcel.trucking.view.common.Status
import com.lionparcel.trucking.view.common.base.BaseSimpleFragment
import com.lionparcel.trucking.view.main.MainActivity

class LoginFragment : BaseSimpleFragment<LoginViewModel, LoginFragmentBinding>() {

    override fun buildViewModel(): LoginViewModel {
        return ViewModelProvider(this, LoginViewModelFactory()).get(LoginViewModel::class.java)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): LoginFragmentBinding {
        return LoginFragmentBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        super.initViews()
        with(binding) {
            btnLogin.setOnClickListener {
                viewModel.login()
            }
        }
    }

    override fun initLiveDataObservers() {
        super.initLiveDataObservers()
        viewModel.loginLiveData.observe(viewLifecycleOwner) { handleLogin(it) }
    }

    private fun handleLogin(resource: Resource<Unit>) {
        when (resource.status) {
            Status.SUCCESS -> {
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            else -> {
            }
        }
    }
}
