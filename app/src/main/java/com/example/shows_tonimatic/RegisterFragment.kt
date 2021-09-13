package com.example.shows_tonimatic

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shows_tonimatic.databinding.FragmentRegisterBinding
import com.example.shows_tonimatic.viewmodel.RegisterViewModel
import com.google.android.material.textfield.TextInputLayout

class RegisterFragment : Fragment() {

    companion object {
        const val MIN_PASS_LENGTH = 6
        const val REGISTRATION_SUCCESSFUL = "Registration successful!"
    }

    private lateinit var binding : FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initEmailAndPassword(binding.emailEdit)
        initEmailAndPassword(binding.passwordEdit)
        initEmailAndPassword(binding.repeatPasswordEdit)

        viewModel.getRegistrationResultLiveData().observe(this.viewLifecycleOwner) {
            isRegisterSuccessful ->
                if (isRegisterSuccessful) {
                    val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
                    if (prefs != null) {
                        with (prefs.edit()) {
                            putBoolean(REGISTRATION_SUCCESSFUL, true)
                            apply()
                        }
                    }
                    findNavController().navigateUp()
                } else {
                    Toast.makeText(context, "Registration fail!", Toast.LENGTH_SHORT).show()
                }
        }

        binding.apply {
            registerButton.setOnClickListener {
                viewModel.register(emailEdit.editText?.text.toString(), passwordEdit.editText?.text.toString(), repeatPasswordEdit.editText?.text.toString())
            }
        }
    }

    private fun initEmailAndPassword(inputLayout: TextInputLayout) {
        val emailEdit = binding.emailEdit.editText
        val passwordEdit = binding.passwordEdit.editText
        val repeatPasswordEdit = binding.repeatPasswordEdit.editText

        inputLayout.editText?.addTextChangedListener {
            val email = emailEdit?.text.toString()
            val password = passwordEdit?.text.toString()
            val repeatedPassword = repeatPasswordEdit?.text.toString()

            if (email.isEmpty() || password.length < MIN_PASS_LENGTH || password != repeatedPassword || !LoginFragment.emailRegex.matcher(email).matches()) {
                binding.registerButton.isEnabled = false
                binding.registerButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.invalid_data))
                binding.registerButton.setTextColor(Color.WHITE)
                Log.d("Error:", "Wrong values!")
            } else {
                binding.registerButton.isEnabled = true
                binding.registerButton.setBackgroundColor(Color.WHITE)
                binding.registerButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.valid_data))
            }
        }
    }
}