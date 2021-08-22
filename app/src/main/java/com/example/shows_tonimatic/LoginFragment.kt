package com.example.shows_tonimatic

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shows_tonimatic.databinding.FragmentLoginBinding
import com.example.shows_tonimatic.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern
import java.util.regex.Pattern.compile

class LoginFragment : Fragment() {

    companion object {
        const val MIN_PASS_LENGTH = 6
        const val REMEMBER_ME = "remember me"
        const val LOGIN = "Login"
        val emailRegex: Pattern = compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
    }

    private lateinit var binding : FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRegisterButton()

        initEmailAndPassword(binding.emailEdit)
        initEmailAndPassword(binding.passwordEdit)

        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val registrationSuccessful = prefs?.getBoolean(RegisterFragment.REGISTRATION_SUCCESSFUL, false)

        if (registrationSuccessful == true) {
            binding.loginText.text = RegisterFragment.REGISTRATION_SUCCESSFUL
            binding.registerButton.isVisible = false

            with(prefs.edit()) {
                putBoolean(RegisterFragment.REGISTRATION_SUCCESSFUL, false)
                apply()
            }
        } else {
            binding.loginText.text = LOGIN
            binding.registerButton.isVisible = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
        if (prefs != null) {
            with (prefs.edit()) {
                putBoolean(RegisterFragment.REGISTRATION_SUCCESSFUL, false)
                apply()
            }
        }
    }

    private fun navigateToShows() {
        val action = LoginFragmentDirections.actionLoginToShows()
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

            if (email.isEmpty() || password.length < MIN_PASS_LENGTH || !emailRegex.matcher(email).matches()) {
                binding.loginButton.isEnabled = false
                binding.loginButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.invalid_data))
                binding.loginButton.setTextColor(Color.WHITE)
                Log.d("Error:", "Wrong values!")
            } else {
                binding.loginButton.isEnabled = true
                binding.loginButton.setBackgroundColor(Color.WHITE)
                binding.loginButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.valid_data))

                binding.loginButton.setOnClickListener {

                    viewModel.getLoginResultLiveData().observe(this.viewLifecycleOwner) { isLoginSuccessful ->
                        if (isLoginSuccessful) {
                            if (prefs != null) {
                                with(prefs.edit()) {
                                    putBoolean(REMEMBER_ME, binding.rememberMe.isChecked)
                                    apply()
                                }
                            }
                            navigateToShows()
                        } else {
                            binding.incorrectData.isVisible = true
                        }
                    }

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
