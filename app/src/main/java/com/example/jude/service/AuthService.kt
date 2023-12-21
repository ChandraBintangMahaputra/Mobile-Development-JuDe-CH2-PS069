package com.example.jude.service


import com.example.jude.response.LoginResponse
import com.example.jude.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers

interface AuthService {

    @POST("auth/login")
    suspend fun userLogin(
        @Body requestBody: Map<String, String>
    ): Response<LoginResponse>


    @POST("auth/register")
    suspend fun userRegister(
        @Body requestBody: Map<String, String>
    ): Response<RegisterResponse>

}
