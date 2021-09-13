package com.example.shows_tonimatic

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shows_tonimatic.databinding.ActivityMainBinding
import com.example.shows_tonimatic.networking.ApiModule

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val prefs = this.getPreferences(Context.MODE_PRIVATE)
        ApiModule.initRetrofit(prefs)
    }
}
