package com.example.jude

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.jude.databinding.ActivityLoginBinding
import com.example.jude.ext.Value
import com.example.jude.ext.gone
import com.example.jude.ext.show
import com.example.jude.remote.ApiResponse
import com.example.jude.response.LoginResponse
import com.example.jude.response.LoginResult
import com.example.jude.service.ApiConfig
import com.example.jude.service.AuthService
import com.example.jude.vm.LoginViewModel
import com.example.jude.vm.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {

    private var _activityLoginBinding: ActivityLoginBinding? = null
    private val binding get() = _activityLoginBinding!!
    private val authService: AuthService = ApiConfig.authService
    private val loginViewModel: LoginViewModel by viewModels { LoginViewModelFactory(authService) }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        _activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(_activityLoginBinding?.root)

        val backgroundImage: ImageView = binding.loginImg

        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide)
        backgroundImage.startAnimation(slideAnimation)

        binding.imgBack2.setOnClickListener {
            onBackPressed()
        }

        binding.btnLogin.setOnClickListener {
            val identifier = binding.rvEmail.text.toString().trim()
            val password = binding.rvPassword.text.toString().trim()

            if (identifier.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                loginViewModel.login(identifier, password)
            }
        }

        // Observe the login response
        loginViewModel.loginResponse.observe(this) { response ->
            when (response) {
                is ApiResponse.Success -> handleLoginSuccess(response.data)
                is ApiResponse.Error -> handleLoginError(response.errorMessage)
                is ApiResponse.Loading -> showLoading()
                else -> hideLoading()
            }
        }

    }

    private fun handleLoginSuccess(loginResponse: LoginResponse?) {
        if (loginResponse != null) {
            // Handle successful login
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

            // Access accessToken directly from loginResponse
            val sharedPreferences = getSharedPreferences(Value.PREFS_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(Value.KEY_TOKEN, loginResponse.accessToken)
            editor.putBoolean(Value.KEY_IS_LOGIN, true)
            editor.apply()

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            // Handle case when loginResponse is null or login is unsuccessful
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleLoginError(errorMessage: String) {
        // Handle login error, show an error message to the user
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()

        // Optionally, you can also clear any sensitive information from SharedPreferences
        val sharedPreferences = getSharedPreferences(Value.PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear() // Clear all data
        editor.apply()
    }

    private fun showLoading() {
        // Show loading indicator, if needed
        binding.progressBar.show()
        binding.btnLogin.gone()
    }

    private fun hideLoading() {
        // Hide loading indicator, if needed
        binding.progressBar.gone()
        binding.btnLogin.show()
    }
}
