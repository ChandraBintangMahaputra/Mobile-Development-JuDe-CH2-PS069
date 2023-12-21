package com.example.jude.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jude.service.AuthService
import com.example.jude.vm.RegisterViewModel

class RegisterViewModelFactory(private val authService: AuthService) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(authService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
