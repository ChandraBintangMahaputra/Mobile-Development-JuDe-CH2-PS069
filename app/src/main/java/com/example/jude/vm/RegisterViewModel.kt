package com.example.jude.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jude.ext.isEmailValid
import com.example.jude.remote.ApiResponse
import com.example.jude.response.RegisterResponse
import com.example.jude.service.AuthService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val authService: AuthService) : ViewModel() {

    private val _registerResponse = MutableLiveData<ApiResponse<RegisterResponse>>()
    val registerResponse: LiveData<ApiResponse<RegisterResponse>> get() = _registerResponse

    fun registerUser(
        username: String,
        email: String,
        password: String,
        fullname: String,
        phone: String,
        gender: String,
        dateOfBirth: String
    ) {
        viewModelScope.launch {

            Log.d("RegisterViewModel", "Register user initiated")

            // Validate password length and email validity
            if (password.length < 8 || !email.isEmailValid()) {
                Log.d("RegisterViewModel", "Invalid password or email")
                _registerResponse.postValue(ApiResponse.Error("Invalid password or email"))
                return@launch
            }

            Log.d("RegisterViewModel", "Password and email are valid")

            _registerResponse.postValue(ApiResponse.Loading)

            try {
                val requestBody = mapOf(
                    "username" to username,
                    "email" to email,
                    "password" to password,
                    "fullname" to fullname,
                    "phone" to phone,
                    "gender" to gender,
                    "date_of_birth" to dateOfBirth
                )

                // Panggil fungsi userRegister
                val response: Response<RegisterResponse> = authService.userRegister(requestBody)

                // Lakukan eksekusi
                if (response.isSuccessful) {
                    Log.d("RegisterViewModel", "Registration successful")
                    _registerResponse.postValue(ApiResponse.Success(response.body()!!))
                } else {
                    Log.d("RegisterViewModel", "Registration failed: ${response.message()}")
                    _registerResponse.postValue(ApiResponse.Error("Error: ${response.message()}"))
                }
            } catch (e: Exception) {
                Log.d("RegisterViewModel", "Exception during registration: ${e.message}")
                _registerResponse.postValue(ApiResponse.Error("Registration failed"))
            }
        }

    }

}

