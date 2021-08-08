package com.example.shows_tonimatic

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shows_tonimatic.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {

    companion object {
        const val MIN_PASS_LENGTH = 6
        const val REMEMBER_ME = "remember me"
    }

    private lateinit var binding : FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initEmailAndPassword(binding.emailEdit)
        initEmailAndPassword(binding.passwordEdit)

        binding.loginButton.setOnClickListener {
            val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
            if (prefs != null) {
                with (prefs.edit()) {
                    putBoolean(REMEMBER_ME, binding.rememberMe.isChecked)
                    apply()
                }
            }
            navigateToShows()
        }

        val prefs = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val clickedRememberMe = prefs.getBoolean( REMEMBER_ME,false)
        if (clickedRememberMe) {
            navigateToShows()
        }

    }

    private fun navigateToShows() {
        val action = LoginFragmentDirections.actionLoginToShows(binding.emailEdit.editText?.text.toString().split("@").toTypedArray()[0])
        findNavController().navigate(action)
    }

    private fun initEmailAndPassword(inputLayout: TextInputLayout) {
        val emailEdit = binding.emailEdit.editText
        val passwordEdit = binding.passwordEdit.editText

        inputLayout.editText?.addTextChangedListener {
            val email = emailEdit?.text.toString()
            val password = passwordEdit?.text.toString()
            val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
            if (prefs != null) {
                with (prefs.edit()) {
                    putString("email", email)
                    apply()
                }
            }

            if (email.isEmpty() || password.length < MIN_PASS_LENGTH) {
                binding.loginButton.isEnabled = false
                binding.loginButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.invalid_data))
                binding.loginButton.setTextColor(Color.WHITE)
                Log.d("Error:", "Wrong values!")
            } else {
                binding.loginButton.isEnabled = true
                binding.loginButton.setBackgroundColor(Color.WHITE)
                binding.loginButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.valid_data))
            }
        }
    }
}