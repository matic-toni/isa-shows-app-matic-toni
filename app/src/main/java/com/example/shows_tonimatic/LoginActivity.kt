package com.example.shows_tonimatic

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.shows_tonimatic.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    companion object {
        const val MIN_PASS_LENGTH = 6
    }

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        initEmailAndPassword(binding.emailEdit)
        initEmailAndPassword(binding.passwordEdit)
        initLoginButton()
    }

    private fun initLoginButton() {
        binding.loginButton.setOnClickListener {
            val intent =  ShowsActivity.buildIntent(this)
            startActivity(intent)
        }
    }

    private fun initEmailAndPassword(inputLayout: TextInputLayout) {
        val emailEdit = binding.emailEdit.editText
        val passwordEdit = binding.passwordEdit.editText

        inputLayout.editText?.addTextChangedListener {
            val email = emailEdit?.text.toString()
            val password = passwordEdit?.text.toString()

            if (email.isEmpty() || password.length < MIN_PASS_LENGTH) {
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
    }
}