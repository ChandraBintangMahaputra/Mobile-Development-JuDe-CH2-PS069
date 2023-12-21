package com.example.jude.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jude.remote.ApiResponse
import com.example.jude.response.LoginResponse
import com.example.jude.service.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel(private val authService: AuthService) : ViewModel() {

    private val _loginResponse = MutableLiveData<ApiResponse<LoginResponse>>()
    val loginResponse: LiveData<ApiResponse<LoginResponse>> get() = _loginResponse

    fun login(identifier: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loginResponse.postValue(ApiResponse.Loading)

            try {
                val requestBody = mapOf(
                    "identifier" to identifier,
                    "password" to password
                )

                val response: Response<LoginResponse> = authService.userLogin(requestBody)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        _loginResponse.postValue(ApiResponse.Success(loginResponse))
                    } else {
                        _loginResponse.postValue(ApiResponse.Error("Invalid login response"))
                    }
                } else {
                    _loginResponse.postValue(ApiResponse.Error("Error: ${response.message()}"))
                }

            } catch (e: Exception) {
                _loginResponse.postValue(ApiResponse.Error(e.message ?: "An error occurred"))
            }
        }
    }
}
