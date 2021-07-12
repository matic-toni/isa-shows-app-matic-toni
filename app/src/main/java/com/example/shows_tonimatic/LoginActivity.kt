package com.example.shows_tonimatic

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import com.example.shows_tonimatic.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.emailEdit.editText?.addTextChangedListener {
            val email = binding.emailEdit.editText?.text.toString()
            val password = binding.passwordEdit.editText?.text.toString()

            if (email.isEmpty() || password.length < 6) {
                binding.loginButton.isEnabled = false
                binding.loginButton.setBackgroundColor(Color.parseColor("#BBBBBB"))
                binding.loginButton.setTextColor(Color.WHITE)
                Log.d("Error:", "Wrong values!")
            } else {
                binding.loginButton.isEnabled = true
                binding.loginButton.setBackgroundColor(Color.WHITE)
                binding.loginButton.setTextColor(Color.parseColor("#52368C"))
            }
        }

        binding.passwordEdit.editText?.addTextChangedListener {
            val email = binding.emailEdit.editText?.text.toString()
            val password = binding.passwordEdit.editText?.text.toString()

            if (email.isEmpty() || password.length < 6) {
                binding.loginButton.isEnabled = false
                binding.loginButton.setBackgroundColor(Color.parseColor("#BBBBBB"))
                binding.loginButton.setTextColor(Color.WHITE)
                Log.d("Error:", "Wrong values!")
            } else {
                binding.loginButton.isEnabled = true
                binding.loginButton.setBackgroundColor(Color.WHITE)
                binding.loginButton.setTextColor(Color.parseColor("#52368C"))
            }
        }

        initExplicitIntent()
    }

    private fun initExplicitIntent() {
        binding.loginButton.setOnClickListener {
            val intent =  WelcomeActivity.buildIntent(this, binding.emailEdit.editText?.text.toString().split("@").toTypedArray()[0])
            startActivity(intent)
        }
    }
}