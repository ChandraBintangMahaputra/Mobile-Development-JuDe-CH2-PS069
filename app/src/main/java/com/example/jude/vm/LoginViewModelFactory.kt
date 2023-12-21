package com.example.jude.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jude.service.AuthService
import com.example.jude.vm.LoginViewModel

class LoginViewModelFactory(private val authService: AuthService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
