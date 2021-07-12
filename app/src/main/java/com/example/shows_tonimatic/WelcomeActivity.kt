package com.example.shows_tonimatic

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.shows_tonimatic.databinding.ActivityLoginBinding
import com.example.shows_tonimatic.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityWelcomeBinding

    companion object {
        private const val EXTRA_EMAIL = "EXTRA_EMAIL"

        fun buildIntent(activity: Activity, email: String) : Intent {
            val intent = Intent(activity, WelcomeActivity::class.java)
            intent.putExtra(EXTRA_EMAIL, email)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.extras?.getString(EXTRA_EMAIL)

        val text = binding.welcomeText.text.toString() + username + "!"
        binding.welcomeText.text = text
    }
}