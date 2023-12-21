package com.example.jude

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.jude.databinding.ActivityRegisterBinding
import com.example.jude.ext.isEmailValid
import com.example.jude.remote.ApiResponse
import com.example.jude.response.RegisterResponse
import com.example.jude.service.ApiConfig
import com.example.jude.service.AuthService
import com.example.jude.vm.RegisterViewModel
import com.example.jude.vm.RegisterViewModelFactory
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private var _activityRegisterBinding: ActivityRegisterBinding? = null
    private val binding get() = _activityRegisterBinding!!
    private val authService: AuthService = ApiConfig.authService
    private val registerViewModel: RegisterViewModel by viewModels { RegisterViewModelFactory(authService) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        _activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(_activityRegisterBinding?.root)

        val backgroundImage: ImageView = binding.registerImg

        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide)
        backgroundImage.startAnimation(slideAnimation)

        val genderOptions = resources.getStringArray(R.array.gender)
        val genderAdapter = ArrayAdapter(this, R.layout.dropdown, genderOptions)
        binding.autoCompleteTextView.setAdapter(genderAdapter)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvDateofBirth.setOnClickListener {
            showDatePickerDialog()
        }

        binding.autoCompleteTextView.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.autoCompleteTextView.showDropDown()
            }
        }

        binding.btnSignUp.setOnClickListener {
            val username = binding.tvUsername.text.toString().trim()
            val email = binding.tvEmail.text.toString().trim()
            val password = binding.tvPassword.text.toString().trim()
            val fullname = binding.tvName.text.toString().trim()
            val phone = binding.tvPhoneNumber.text.toString().trim()
            val gender = binding.autoCompleteTextView.text.toString().trim()
            val dateOfBirth = binding.tvDateofBirth.text.toString().trim()

            registerViewModel.registerUser(username, email, password, fullname, phone, gender, dateOfBirth)
        }

        // Observe the registration response
        registerViewModel.registerResponse.observe(this) { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is ApiResponse.Error -> {
                    Toast.makeText(this, response.errorMessage, Toast.LENGTH_SHORT).show()
                }
                is ApiResponse.Loading -> {
                    // Handle loading state if needed
                }
                else -> {
                    // Handle other states if needed
                }
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.tvDateofBirth.setText(selectedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
}
