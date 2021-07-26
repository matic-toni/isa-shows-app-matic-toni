package com.example.shows_tonimatic

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shows_tonimatic.databinding.FragmentLoginBinding
import com.example.shows_tonimatic.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {

    companion object {
        const val MIN_PASS_LENGTH = 6
        const val REMEMBER_ME = "remember me"
        const val REGISTRATION_SUCCESSFUL = "Registration successful!"
        const val LOGIN = "Login"
    }

    private lateinit var binding : FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        initRegisterButton()

        initEmailAndPassword(binding.emailEdit)
        initEmailAndPassword(binding.passwordEdit)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLoginResultLiveData().observe(this.viewLifecycleOwner) { isLoginSuccessful ->
            if (isLoginSuccessful) {
                navigateToShows()
            } else {
                Toast.makeText(context, "Login failed!", Toast.LENGTH_SHORT).show()
            }
        }

        setPrefs()


        val prefs = activity?.getPreferences(Context.MODE_PRIVATE) ?: return

        val clickedRememberMe = prefs.getBoolean( REMEMBER_ME,false)
        val registrationSuccessful = prefs.getBoolean(REGISTRATION_SUCCESSFUL, false)

        if (registrationSuccessful) {
            binding.loginText.text = REGISTRATION_SUCCESSFUL
            binding.registerButton.isVisible = false
        } else {
            binding.loginText.text = LOGIN
            binding.registerButton.isVisible = true
        }

        if (clickedRememberMe) {
            navigateToShows()
        }
    }

    private fun setPrefs() {
        binding.loginButton.setOnClickListener {
            val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
            if (prefs != null) {
                with (prefs.edit()) {
                    putBoolean(REMEMBER_ME, binding.rememberMe.isChecked)
                    apply()
                }
            }
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

            if (email.isEmpty() || password.length < MIN_PASS_LENGTH) {
                binding.loginButton.isEnabled = false
                binding.loginButton.setBackgroundColor(Color.parseColor("#BBBBBB"))
                binding.loginButton.setTextColor(Color.WHITE)
                Log.d("Error:", "Wrong values!")
            } else {
                binding.loginButton.isEnabled = true
                binding.loginButton.setBackgroundColor(Color.WHITE)
                binding.loginButton.setTextColor(Color.parseColor("#52368C"))

                binding.loginButton.setOnClickListener {
                    viewModel.login(
                        binding.emailEdit.editText?.text.toString(),
                        binding.passwordEdit.editText?.text.toString()
                    )
                }

            }
        }
    }

    private fun initRegisterButton() {
        binding.registerButton.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginToRegister()
            findNavController().navigate(action)
        }
    }

}
