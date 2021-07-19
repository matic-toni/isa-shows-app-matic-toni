package com.example.shows_tonimatic

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shows_tonimatic.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {

    companion object {
        const val MIN_PASS_LENGTH = 6
    }

    private lateinit var binding : FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        initEmailAndPassword(binding.emailEdit)
        initEmailAndPassword(binding.passwordEdit)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginToShows(binding.emailEdit.editText?.text.toString().split("@").toTypedArray()[0])
            findNavController().navigate(action)
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