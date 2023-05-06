package com.example.chatapplication.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.databinding.LoginActivityBinding
import com.example.chatapplication.presentation.screens.auth.LoginStage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginActivityBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpObservers()
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        binding.loginButton.setOnClickListener {
            viewModel.beginSignIn(binding.countryCodeSpinner.selectedItem.toString() + binding.mobileNumberInput.text.toString(), this)
        }
        binding.verifyButton.setOnClickListener {
            viewModel.verifyOTP(binding.otpInput.text.toString())
        }
        binding.saveButton.setOnClickListener {
            viewModel.addNewUser(binding.nameInput.text.toString())
        }
    }

    private fun setUpObservers() {
        viewModel.state.observe(this) {
            binding.phoneLayout.visibility = View.GONE
            binding.otpLayout.visibility = View.GONE
            binding.nameLayout.visibility = View.GONE


            when (it.stage) {
                is LoginStage.LoggedOut -> binding.phoneLayout.visibility = View.VISIBLE
                is LoginStage.OTPVerifcation -> binding.otpLayout.visibility = View.VISIBLE
                is LoginStage.SignUp -> binding.nameLayout.visibility = View.VISIBLE
                is LoginStage.SignedIn -> {
                    Log.i(TAG, "setUpObservers: success login")
                    startActivity(Intent(this, PagerActivity::class.java))
                    finish()
                }
            }
        }
    }


}